create table user_account (
    id uuid primary key,
    username varchar(50) not null check (
        char_length(username) >= 1
        and
        char_length(username) <= 50
        and
        username ~ '^.*\S.*$'
    ),
    password varchar(255) not null check (
        char_length(username) >= 8
        and
        char_length(username) <= 255
        and
        password ~ '^.*\S.*$'
    ),
    first_name varchar(100) not null check (
        char_length(username) >= 1
        and
        char_length(username) <= 100
        and
        first_name ~ '^.*\S.*$'
    ),
    last_name varchar(100) not null check (
        char_length(username) >= 1
        and
        char_length(username) <= 100
        and
        last_name ~ '^.*\S.*$'
    ),
    email varchar(255) not null check(
        char_length(username) >= 5
        and
        char_length(username) <= 255
        and
        email ~* '^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$'
    ),
    birth_day date not null check(birth_day < current_date),
    gender varchar(50) default 'UNSPECIFIED',
    enabled boolean default false,
    locked boolean default false,
    expired boolean default false,
    credentials_expired boolean default false,
    version bigint default 0
);