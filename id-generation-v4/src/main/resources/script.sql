
CREATE TABLE provincias (
    id bigint NOT NULL,
    nombre varchar(100) NOT NULL
);

ALTER TABLE provincias
ADD CONSTRAINT PK_provincias PRIMARY KEY (id);

------------------------

CREATE TABLE localidades (
    provincia_id bigint NOT NULL,
    id bigint NOT NULL,
    nombre varchar(100) NOT NULL
);

ALTER TABLE localidades
ADD CONSTRAINT PK_localidades PRIMARY KEY (provincia_id, id);

------------------------

ALTER TABLE localidades
ADD CONSTRAINT FK_localidades_provincias FOREIGN KEY (provincia_id)
REFERENCES provincias (id);

------------------------

CREATE TABLE sequence_table (
    query_id varchar(100) PRIMARY KEY,
    ult_valor bigint
);