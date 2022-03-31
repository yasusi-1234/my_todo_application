create table if not exists ROLE(
    role_id bigint auto_increment primary key,
    role_name varchar(30) not null
);

create table if not exists app_user (
    app_user_id bigint not null auto_increment primary key,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    mail_address varchar(255) not null,
    password varchar(255) not null,
    role_id bigint not null,
    foreign key (role_id) references role(role_id)
);

create table if not exists task(
    task_id bigint not null auto_increment primary key,
    task_name varchar(100) not null,
    detail varchar(255) not null,
    start_date date not null,
    end_date date not null,
    importance varchar(20) not null,
    progress int not null,
    notice tinyint not null,
    app_user_id bigint not null,
    foreign key(app_user_id) references app_user(app_user_id)
);
