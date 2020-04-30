create table user_account (
    id uuid primary key default uuid_generate_v4(),
    username varchar(50) unique not null check (
        char_length(username) >= 1
        and
        char_length(username) <= 50
        and
        username ~ '^\S+$'
    ),
    password varchar(255) not null check (
        char_length(password) >= 8
        and
        char_length(password) <= 255
        and
        password ~ '^.*\S.*$'
    ),
    first_name varchar(100) not null check (
        char_length(first_name) >= 1
        and
        char_length(first_name) <= 100
        and
        first_name ~ '^.*\S.*$'
    ),
    last_name varchar(100) not null check (
        char_length(last_name) >= 1
        and
        char_length(last_name) <= 100
        and
        last_name ~ '^.*\S.*$'
    ),
    email varchar(255) not null check(
        char_length(email) >= 5
        and
        char_length(email) <= 255
        and
        email ~* '^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$'
    ),
    birth_day date not null check(birth_day < current_date),
    gender varchar(50) default 'UNSPECIFIED',
    enabled boolean default false,
    locked boolean default false,
    expired boolean default false,
    credentials_expired boolean default false,
    version bigint default 0,
    creation_time timestamp not null,
    update_time timestamp not null
);

create table todo (
    id uuid primary key default uuid_generate_v4(),
    description text not null check (
        char_length(description) >= 1
        and
        char_length(description) <= 200000000
        and
        description ~ '^.*\S.*$'
    ),
    target_date date not null check(target_date > current_date),
    is_done boolean default false,
    user_account_id uuid not null references user_account(id),
    version bigint default 0,
    creation_time timestamp not null,
    update_time timestamp not null
);

create table email_verification_token(
    id uuid primary key default uuid_generate_v4(),
    token varchar(100) not null check (token ~ '^.*\S.*$'),
    expires_at timestamp not null check(expires_at > current_timestamp),
    user_account_id uuid not null unique references user_account(id),
    creation_time timestamp not null,
    update_time timestamp not null
);