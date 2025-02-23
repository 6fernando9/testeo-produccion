 create table bitacora_usuarios (
        tipo_sesion varchar(1),
        fecha datetime(6),
        id bigint not null auto_increment,
        usuario_id bigint,
        ip varchar(255) not null,
        username varchar(255) not null,
        primary key (id)
    );