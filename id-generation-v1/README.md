Ejemplo de generador de ids personalizado que dependiendo del dialecto sql hace la generación con secuencias o con tablas.

Para crear las bases de datos se pueden usar los scripts: `script-mssql.sql` para SQL Server y `script-mysql.sql` para MySQL.
El primero genera una secuencia por cada una de las dos tablas de entidades y el segundo genera una sola tabla de generación de ids (ya que MySQL no posee la funcionalidad de secuencias).

Se pueden usar los mismos parámetros que usan las anotaciones `@SequenceGenerator` y `@TableGenerator`. 
Si no se proporcionan se toman los mismos predeterminados que dichas anotaciones, con el agregado que `sequenceName` será el nombre de la tabla seguido del sufijo `_sequence` y `pkColumnValue` será igual al nombre de la tabla.
En el ejemplo se usa la parametrización por defecto.
Para utilizar otros parámetros se lo debe hacer de la siguiente manera:

```java
    @Id @Column(name = "id")
    @GenericGenerator(name = "provincias_generator", 
            strategy = "com.eiv.poc.generators.CustomGenerator",
            parameters = {
                    @Parameter(name = CustomGenerator.SEQUENCE_NAME, value = "provincias_sequence"),
                    @Parameter(name = CustomGenerator.PK_COLUMN_VALUE, value = "provincias")
            })
    @GeneratedValue(generator = "provincias_generator")
    private Long id;
```