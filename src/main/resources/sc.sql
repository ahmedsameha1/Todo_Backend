create table user_account (
    id uuid primary key,
    username varchar(50) check (
        char_length(username) >= 1
        and
        char_length(username) <= 50
        and
        array_length(regexp_matches(username, "^.*\S.*$")) >= 1
    ),

);