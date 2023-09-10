--создание таблицы волонтеров

create table volunteers
(
    id bigserial primary key,
    chat_id bigint not null,
    first_name varchar(20) not null,
    is_free boolean default true,
    visitor_id bigint
);
--создание связи волонтеров с посетителями
alter table volunteers
    add constraint visitor_to_volunteer_fk foreign key(visitor_id) references visitors(id);
--добавление столбца с волонтером
alter table visitors
    add column volunteer_id bigint;
--создание связи посетителей с волонтерами
alter table visitors
    add constraint volunteer_to_visitor_fk foreign key(volunteer_id) references volunteers(id);