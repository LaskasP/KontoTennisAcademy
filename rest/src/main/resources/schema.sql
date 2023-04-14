drop table if exists application_user_refresh_tokens;
drop table if exists users_roles;
drop table if exists reservations_time_intervals;
drop table if exists time_intervals;
drop table if exists reservations;
drop table if exists application_user_roles;
drop table if exists application_users;
drop table if exists hibernate_sequence;
drop table if exists courts;


create table application_user_roles
(
    id            bigint not null,
    app_user_role varchar(20),
    primary key (id)
);

create table application_users
(
    id                      bigint not null,
    email                   varchar(60),
    first_name              varchar(255),
    is_active               bit    not null,
    is_not_locked           bit    not null,
    join_date               datetime(6),
    last_login_date         datetime(6),
    last_login_date_display datetime(6),
    last_name               varchar(255),
    password                varchar(200),
    profile_image_url       varchar(255),
    user_id                 varchar(255),
    username                varchar(25),
    primary key (id)
);

create index application_user_index on application_users (username);

create table users_roles
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id)
);

create table application_user_refresh_tokens
(
    id                  bigint       not null,
    expiry_date         datetime(6)  not null,
    token               varchar(255) not null,
    application_user_id bigint       not null,
    primary key (id)
);

create table courts
(
    id         bigint       not null,
    court_type varchar(255) not null,
    primary key (id)
);

create table reservations
(
    id               bigint not null,
    user_id          bigint not null,
    court_id         bigint not null,
    reservation_date date   not null,
    primary key (id),
    foreign key (user_id) references application_users (id),
    foreign key (court_id) references courts (id)
);

-- create table unavailability
-- (
--     id                  bigint       not null,
--     day                 date         not null,
--     is_work_day         bit          not null,
--     start_time          time,
--     end_time            time,
--     primary key (day),
--     foreign key (user_id) references application_users(id),
--     foreign key (court_id) references courts(id)
-- )

create table time_intervals
(
    id         bigint not null,
    time_value time   not null,
    primary key (id)
);

CREATE TABLE reservations_time_intervals
(
    reservation_id   bigint,
    time_interval_id bigint,
    PRIMARY KEY (reservation_id, time_interval_id),
    FOREIGN KEY (reservation_id) REFERENCES reservations (id) ON DELETE CASCADE,
    FOREIGN KEY (time_interval_id) REFERENCES time_intervals (id) ON DELETE CASCADE
);