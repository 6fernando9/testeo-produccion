-- V2__add_unique_constraint_to_correo.sql
ALTER TABLE usuarios
ADD CONSTRAINT uk_correo UNIQUE (correo);

ALTER TABLE roles
ADD CONSTRAINT uk_roles_nombre UNIQUE (nombre);

