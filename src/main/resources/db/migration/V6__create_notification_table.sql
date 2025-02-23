  create table notificaciones (
        fecha datetime(6),
        id bigint not null auto_increment,
        usuario_id bigint,
        notificacion enum ('CODIGO_REGISTRO','CORREO_INFORMATIVO','PAGO_EXITOSO'),
        foreign key (usuario_id) references usuarios(id),
        primary key (id)

    );