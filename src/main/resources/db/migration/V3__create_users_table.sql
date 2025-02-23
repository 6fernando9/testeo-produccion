 create table usuarios (
        enabled bit not null,
        id bigint not null auto_increment,
        rol_id bigint,
        username varchar(20) not null,
        correo varchar(255) not null,
        password varchar(255),
        primary key (id),
        foreign key (rol_id) references roles(id)
    );
