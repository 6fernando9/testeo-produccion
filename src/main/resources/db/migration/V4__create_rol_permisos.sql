 create table roles_permisos (
        permiso_id bigint not null,
        rol_id bigint not null,
        primary key (rol_id, permiso_id),
        foreign key (permiso_id) references permisos(id),
        foreign key (rol_id) references roles(id)
    );