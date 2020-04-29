package com.iait.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Table;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.MappingException;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.model.relational.QualifiedName;
import org.hibernate.boot.model.relational.QualifiedNameParser;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.jdbc.AbstractReturningWork;
import org.hibernate.jdbc.WorkExecutorVisitable;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

public class HashKeyGenerator implements PersistentIdentifierGenerator, Configurable {

    public static final String COMPOSITE_KEY = "compositeKey";
    public static final String ID_FIELD = "idField";
    public static final String USE_HASH = "useHash";
    public static final String TABLE_PARAM = "table_name";
    public static final String CATALOG = "catalog";
    public static final String SCHEMA = "schema";
    public static final String VALUE_COLUMN_PARAM = "value_column_name";
    public static final String SEGMENT_COLUMN_PARAM = "segment_column_name";
    
    private boolean isCompositeKey;
    private String idField;
    private String capitalizedIdField;
    private Type identifierType;
    private boolean useHash;
    
    private String valueColumnName;
    private String segmentColumnName;
    
    private String tableName;
    private String catalog;
    private String schema;
    private QualifiedName qualifiedTableName;
    
    private String selectQuery;
    private String insertQuery;
    private String updateQuery;
    
    @Override
    public void configure(
            Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        
        final JdbcEnvironment jdbcEnvironment = serviceRegistry.getService(JdbcEnvironment.class);
        
        this.isCompositeKey = "true".equals(params.getProperty(COMPOSITE_KEY));
        this.useHash = "true".equals(params.getProperty(USE_HASH));
        this.idField = params.getProperty(ID_FIELD);
        this.identifierType = type;
        this.capitalizedIdField = idField == null || idField.isEmpty() ? idField 
                : idField.substring(0, 1).toUpperCase() + idField.substring(1);
        
        this.tableName = ConfigurationHelper.getString(TABLE_PARAM, params, "id_gen");
        this.catalog = params.getProperty(CATALOG);
        this.schema = params.getProperty(SCHEMA);
        if (tableName.contains(".")) {
            qualifiedTableName = QualifiedNameParser.INSTANCE.parse(tableName);
        } else {
            Identifier catalogId = jdbcEnvironment.getIdentifierHelper().toIdentifier(catalog);
            Identifier schemaId = jdbcEnvironment.getIdentifierHelper().toIdentifier(schema);
            Identifier tableNameId = jdbcEnvironment.getIdentifierHelper().toIdentifier(tableName);
            qualifiedTableName = new QualifiedNameParser.NameParts(
                    catalogId, schemaId, tableNameId);
        }
        
        this.valueColumnName = ConfigurationHelper.getString(
                VALUE_COLUMN_PARAM, params, "gen_value");
        this.segmentColumnName = ConfigurationHelper.getString(
                SEGMENT_COLUMN_PARAM, params, "gen_key");
    }

    @Override
    public Serializable generate(
            SharedSessionContractImplementor session, Object object) throws HibernateException {

        WorkExecutorVisitable<Serializable> work = new AbstractReturningWork<Serializable>() {

            @Override
            public Serializable execute(Connection connection) throws SQLException {
                if (isCompositeKey) {
                    return compositeKey(connection, object);
                } else {
                    return simpleKey(connection, object);
                }
            }
        };
        return session.getTransactionCoordinator().createIsolationDelegate()
                .delegateWork(work, true);
    }
    
    @Override
    public void registerExportables(Database database) {
        Dialect dialect = database.getJdbcEnvironment().getDialect();
        
        String select = "select tbl." + valueColumnName +
                " from " + tableName + " tbl where tbl." + segmentColumnName + "=?";
        LockOptions lockOptions = new LockOptions(LockMode.PESSIMISTIC_WRITE);
        lockOptions.setAliasSpecificLockMode("tbl", LockMode.PESSIMISTIC_WRITE);
        Map<String, String[]> updateTargetColumnsMap = Collections.singletonMap(
                "tbl", new String[] {valueColumnName});
        selectQuery = dialect.applyLocksToSql(select, lockOptions, updateTargetColumnsMap);
        
        updateQuery = "update " + tableName +
                " set " + valueColumnName + "=? " +
                " where " + segmentColumnName + "=?";
        
        insertQuery = "insert into " + tableName 
                + " (" + segmentColumnName + ", " + valueColumnName + ") " + " values (?,?)";
    }
    
    @Override
    @Deprecated
    public String[] sqlCreateStrings(Dialect dialect) throws HibernateException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    @Deprecated
    public String[] sqlDropStrings(Dialect dialect) throws HibernateException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Object generatorKey() {
        return qualifiedTableName.render();
    }
    
    private Serializable simpleKey(Connection connection, Object object) 
            throws HibernateException {
        
        Table table = object.getClass().getAnnotation(Table.class);
        String tableName = table.name();
        
        try {
            Long value = getNextValue(tableName, connection);
            return value;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
    private Serializable compositeKey(Connection connection, Object object) 
            throws HibernateException {

        Class<?> clazz = object.getClass();
        Table table = clazz.getAnnotation(Table.class);
        String tableName = table.name();

        String keyName = compositeKeyName(tableName, object);
        
        try {
            Long value = getNextValue(keyName, connection);
            
            Serializable pk = extractPkField(object);
            Class<?> type = pk.getClass().getDeclaredField(idField).getType();
            pk.getClass().getMethod("set" + capitalizedIdField, type).invoke(pk, value);
        
            updateIdField(object, pk, value);
            
            return pk;
            
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException 
                | NoSuchMethodException | SecurityException | NoSuchFieldException 
                | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static boolean delay = true;
    
    private Long getNextValue(String keyName, Connection connection) 
            throws UnsupportedEncodingException {
        
        String key = useHash 
                ? SerializationUtils.calcularHash(keyName.getBytes("UTF-8")) : keyName;
        
        try (PreparedStatement stmt = connection.prepareStatement(selectQuery)) {
            
            boolean exists = false;
            Long value = 1L;
            
            stmt.setString(1, key);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                value = rs.getLong(1) + 1L;
                exists = true;
            }
            try {
                if (delay) {
                    delay = false;
                    TimeUnit.SECONDS.sleep(10);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            stmt.close();
            
            if (!exists) {
                
                PreparedStatement stmtInsert = connection.prepareStatement(insertQuery);
                stmtInsert.setString(1, key);
                stmtInsert.setLong(2, value);
                
                stmtInsert.executeUpdate();
                stmtInsert.close();
            
            } else {
                
                PreparedStatement stmtUpdate = connection.prepareStatement(updateQuery);
                stmtUpdate.setLong(1, value);
                stmtUpdate.setString(2, key);
                
                stmtUpdate.executeUpdate();
                stmtUpdate.close();
            }
            
            return value;
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
//    private Long ultValor(String keyName, Connection conn) 
//            throws UnsupportedEncodingException {
//
//        String key = useHash 
//                ? SerializationUtils.calcularHash(keyName.getBytes("UTF-8")) : keyName;
//        
//        String query = "SELECT ult_valor FROM sequence_table WHERE query_id = ?";
//        
//        try {
//            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
//            PreparedStatement stmtSelect = conn.prepareStatement(query);
//            stmtSelect.setString(1, key);
//            
//            ResultSet rs = stmtSelect.executeQuery();
//            long ultValor = 0;
//            
//            if (rs.next()) {
//                ultValor = rs.getLong(1);
//            }
//            
//            try {
//                if (delay) {
//                    delay = false;
//                    TimeUnit.SECONDS.sleep(10);
//                }
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            
//            stmtSelect.close();
//            
//            if (ultValor == 0) {
//                
//                ultValor++;
//                String insert = "INSERT INTO sequence_table (query_id, ult_valor) VALUES (?, ?)";
//                
//                PreparedStatement stmtInsert = conn.prepareStatement(insert);
//                stmtInsert.setString(1, key);
//                stmtInsert.setLong(2, ultValor);
//                
//                stmtInsert.executeUpdate();
//                stmtInsert.close();
//            
//            } else {
//                
//                ultValor++;
//                String update = "UPDATE sequence_table SET ult_valor = ? WHERE query_id = ?";
//                
//                PreparedStatement stmtUpdate = conn.prepareStatement(update);
//                stmtUpdate.setLong(1, ultValor);
//                stmtUpdate.setString(2, key);
//                
//                stmtUpdate.executeUpdate();
//                stmtUpdate.close();
//            }
//            
//            return ultValor;
//            
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
    
    private String compositeKeyName(String tableName, Object object) throws HibernateException {
        
        try {
            
            final Serializable pk = extractPkField(object);
                    
            String condition = Arrays.stream(pk.getClass().getMethods())
                    .filter(m -> m.getName().startsWith("get") 
                            && !m.getName().equals("get" + capitalizedIdField)
                            && !m.getName().equals("getClass"))
                    .map(m -> {
                        try {
                            return String.format("%s=%s", 
                                    m.getName().substring(3), m.invoke(pk).toString());
                        } catch (IllegalAccessException | IllegalArgumentException 
                                | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.joining(","));
            
            String keyName = String.format("%s,%s", tableName, condition);
            
            return keyName;
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private Serializable extractPkField(Object object) 
            throws IllegalArgumentException, IllegalAccessException {
        
        Serializable pk = null;
        Field[] fields = object.getClass().getDeclaredFields();
        Class<?> type = identifierType.getReturnedClass();
        
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType().equals(type)) {
                
                pk = (Serializable) field.get(object);
                field.setAccessible(false);
                break;
            }
        }
        
        return pk;
    }

    private void updateIdField(Object object, Serializable pk, Long ultValor) 
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException {
        
        Field[] pkFields = pk.getClass().getDeclaredFields();
        String dbColName = null;
        
        for (Field field : pkFields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null && field.getName().equals(idField)) {
                dbColName = column.name(); 
            }
        }
        
        Field[] fields = object.getClass().getDeclaredFields();
        
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null && column.name().equals(dbColName)) {
            
                Class<?> type = field.getType();
                String setterName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                
                object.getClass().getMethod(setterName, type).invoke(object, ultValor);
                
                break;
            }
        }
    }
}
