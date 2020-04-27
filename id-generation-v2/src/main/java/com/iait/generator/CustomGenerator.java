package com.iait.generator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

public class CustomGenerator implements PersistentIdentifierGenerator, Configurable {
    
    public static final String ALLOCATION_SIZE = "allocationSize";
    public static final String INITIAL_VALUE = "initialValue";
    public static final String TABLE = "table";
    public static final String PK_COLUMN_NAME = "pkColumnName";
    public static final String PK_COLUMN_VALUE = "pkColumnValue";
    public static final String VALUE_COLUMN_NAME = "valueColumnName";
    public static final String COMPOSITE_KEY = "compositeKey";
    public static final String ID_FIELD = "idField";
    
    private static final String TARGET_TABLE = "target_table";
    private static final Map<String, String> propertiesMap;
    
    static {
        propertiesMap = new HashMap<>();
        propertiesMap.put(ALLOCATION_SIZE, "increment_size");
        propertiesMap.put(INITIAL_VALUE, "initial_value");
        propertiesMap.put(TABLE, "table_name");
        propertiesMap.put(PK_COLUMN_NAME, "segment_column_name");
        propertiesMap.put(PK_COLUMN_VALUE, "segment_value");
        propertiesMap.put(VALUE_COLUMN_NAME, "value_column_name");
    }
    
    private PersistentIdentifierGenerator generator;
    
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) 
            throws HibernateException {
        return generator.generate(session, object);
    }
    
    @Override
    public void registerExportables(Database database) {
        generator.registerExportables(database);
    }
    
    @Override
    @Deprecated
    public String[] sqlCreateStrings(Dialect dialect) throws HibernateException {
        return generator.sqlCreateStrings(dialect);
    }
    
    @Override
    @Deprecated
    public String[] sqlDropStrings(Dialect dialect) throws HibernateException {
        return generator.sqlDropStrings(dialect);
    }
    
    @Override
    public Object generatorKey() {
        return generator.generatorKey();
    }
    
    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) 
            throws MappingException {
        
        String tableName = params.getProperty(TARGET_TABLE);
        params.setProperty(propertiesMap.get(ALLOCATION_SIZE), 
                params.getProperty(ALLOCATION_SIZE, "1"));
        params.setProperty(propertiesMap.get(INITIAL_VALUE), 
                params.getProperty(INITIAL_VALUE, "1"));
        params.setProperty(propertiesMap.get(TABLE), 
                params.getProperty(TABLE, "id_gen"));
        params.setProperty(propertiesMap.get(PK_COLUMN_NAME), 
                params.getProperty(PK_COLUMN_NAME, "gen_key"));
        params.setProperty(propertiesMap.get(PK_COLUMN_VALUE), 
                params.getProperty(PK_COLUMN_VALUE, tableName));
        params.setProperty(propertiesMap.get(VALUE_COLUMN_NAME), 
                params.getProperty(VALUE_COLUMN_NAME, "gen_value"));
        
        CustomTableGenerator generator = new CustomTableGenerator();
        generator.configure(type, params, serviceRegistry);
        this.generator = generator;
    }
}

