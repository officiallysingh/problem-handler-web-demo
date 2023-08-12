CREATE TABLE IF NOT EXISTS employees
(
    id bigint NOT NULL,
    dob date NOT NULL,
    name character varying(50) NOT NULL,
    CONSTRAINT employees_pkey PRIMARY KEY (id),
    CONSTRAINT unqemployeesname UNIQUE (name)
);

CREATE SEQUENCE IF NOT EXISTS employees_seq
    INCREMENT 50
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;