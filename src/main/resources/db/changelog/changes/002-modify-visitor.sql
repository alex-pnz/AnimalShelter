-- change first_name to chat_id and last_name to name
alter table visitors
    rename column first_name to chat_id;

alter table visitors
    rename column last_name to visitor_name;

