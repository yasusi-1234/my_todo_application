create table if not exists role(
    role_id serial primary key,
    role_name varchar(100) not null
);

create table if not exists app_user(
    app_user_id serial primary key,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    mail_address varchar(255) not null,
    password varchar(255) not null,
    role_id bigint,
    foreign key (role_id) references role(role_id)
);

create table if not exists task(
    task_id serial primary key,
    task_name varchar(100) not null,
    start_datetime timestamp not null,
    end_datetime timestamp not null,
    detail varchar(255) not null,
    importance varchar(50) not null,
    progress int not null,
    notice tinyint not null,
    app_user_id bigint,
    foreign key (app_user_id) references app_user(app_user_id)
);
