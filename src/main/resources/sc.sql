create table user_account (
    id uuid primary key,
    username varchar(50) not null check (
        char_length(username) >= 1
        and
        char_length(username) <= 50
        and
        array_length(regexp_matches(username, '^.*\S.*$')) >= 1
    ),
    password varchar(255) not null check (
        char_length(username) >= 8
        and
        char_length(username) <= 255
        and
        array_length(regexp_matches(password, '^.*\S.*$')) >= 1
    ),
    first_name varchar(100) not null check (
        char_length(username) >= 1
        and
        char_length(username) <= 100
        and
        array_length(regexp_matches(first_name, '^.*\S.*$')) >= 1
    ),
    last_name varchar(100) not null check (
        char_length(username) >= 1
        and
        char_length(username) <= 100
        and
        array_length(regexp_matches(last_name, '^.*\S.*$')) >= 1
    ),
    email varchar(255) not null check(
        char_length(username) >= 5
        and
        char_length(username) <= 255
        and
        array_length(regexp_matches(email, '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$')) = 1
    ),
    birth_day date not null check(birth_day < current_date),
    gender varchar(50) default 'UNSPECIFIED',
    enabled boolean default false,
    locked boolean default false,
    expired boolean default false,
    credentials_expired boolean default false,
    version bigserial default 0
);