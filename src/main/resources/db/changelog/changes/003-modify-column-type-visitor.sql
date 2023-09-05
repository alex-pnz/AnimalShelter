-- change type of column chat_id
alter table visitors
    alter column chat_id type bigint using chat_id::bigint;
