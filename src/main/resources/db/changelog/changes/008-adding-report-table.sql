--Создание таблицы reports
create table reports
(
    id bigserial primary key,
    date timestamp,
    diet text,
    behaviour text,
    overall_health text,
    adoption_id bigint references adoptions(id)
);
create table adoptions_reports
(
    id bigserial primary key,
    adoption_id bigint not null,
    reports_id bigint not null
);
--Добавление столбцов для adoptions
alter table adoptions
    add column reports_count integer,
    add column reports_required integer,
    add column status varchar(16);

--Добавление столбца для volunteers
alter table volunteers
    add column action varchar(32);


