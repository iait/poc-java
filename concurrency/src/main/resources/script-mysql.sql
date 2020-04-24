
CREATE TABLE provincias (
    id bigint NOT NULL,
    nombre varchar(100) NOT NULL
);

ALTER TABLE provincias
ADD CONSTRAINT PK_provincias PRIMARY KEY (id);

------------------------

CREATE TABLE localidades (
    id bigint NOT NULL,
    nombre varchar(100) NOT NULL,
    provincia_id bigint NOT NULL
);

ALTER TABLE localidades
ADD CONSTRAINT PK_localidades PRIMARY KEY (id);

------------------------

ALTER TABLE localidades
ADD CONSTRAINT FK_localidades_provincias FOREIGN KEY(provincia_id)
REFERENCES provincias (id);

------------------------

CREATE TABLE id_gen (
    gen_key varchar(100) PRIMARY KEY,
    gen_value bigint
);
