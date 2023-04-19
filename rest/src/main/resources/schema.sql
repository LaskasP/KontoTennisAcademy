drop table if exists application_user_refresh_tokens^;
drop table if exists users_roles^;
drop table if exists reservations_time_intervals^;
drop table if exists time_intervals^;
drop table if exists reservations^;
drop table if exists application_user_roles^;
drop table if exists application_users^;
drop table if exists hibernate_sequence^;
drop table if exists courts^;


create table application_user_roles
(
    id            bigint not null,
    app_user_role varchar(20),
    primary key (id)
)^;

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
)^;

create index application_user_index on application_users (username)^;

create table users_roles
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id)
)^;

create table application_user_refresh_tokens
(
    id                  bigint       not null,
    expiry_date         datetime(6)  not null,
    token               varchar(255) not null,
    application_user_id bigint       not null,
    primary key (id)
)^;

create table courts
(
    id         bigint       not null,
    court_type varchar(255) not null,
    primary key (id)
)^;

create table reservations
(
    id               bigint not null,
    user_id          bigint not null,
    court_id         bigint not null,
    reservation_date date   not null,
    reservation_start_time          time not null,
    reservation_end_time            time not null,
    primary key (id),
    foreign key (user_id) references application_users (id),
    foreign key (court_id) references courts (id)
)^;

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
)^;


CREATE TRIGGER reservation_time_insert_overlap BEFORE INSERT
    ON reservations FOR EACH ROW
BEGIN
    DECLARE rowcount bigint;
    SELECT COUNT(*) INTO rowcount FROM reservations
    WHERE court_id = NEW.court_id AND reservation_date = NEW.reservation_date
      AND (NEW.reservation_start_time < reservation_end_time AND NEW.reservation_end_time > reservation_start_time);
    IF rowcount > 0 THEN
        signal sqlstate '23000' set message_text = 'Reservation time intervals overlap other time intervals';
END IF;
END^;


-- CREATE TRIGGER prices_update_overlap
--     BEFORE UPDATE
--     ON prices FOR EACH ROW
-- BEGIN
--     DECLARE rowcount INT;
--
--     SELECT COUNT(*) INTO rowcount FROM prices
--     WHERE gas_station_id = NEW.gas_station_id AND id != OLD.id
--       AND (NEW.ts_start <= ts_end AND NEW.ts_end >= ts_start);
--
--     IF rowcount > 0 THEN
--         signal sqlstate '45000' set message_text = 'Interval Overlaps Existing Intervals';
-- END IF;
--
-- END;