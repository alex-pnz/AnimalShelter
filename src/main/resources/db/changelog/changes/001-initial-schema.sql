-- Перед запуском приложения с миграцией необходимо создать БД
-- в pgAdmin нажать кнопку SQL Tool с таким значком [>_]
-- ввести команду create database animal_shelter owner student;

create table animals
(
    id bigserial primary key,
    animal_type int,
    name varchar(20) not null,
    age int,
    admission_date timestamp,
    leave_date timestamp
);

create table shelters
(
    id bigserial primary key,
    shelter_type int,
    shelter_name varchar(30),
    city_address varchar(50),
    street_address varchar(50),
    house_number int,
    phone_number varchar(20),
    email varchar(30),
    opening_time time,
    closing_time time
);

create table animals_per_shelter
(
    id bigserial primary key,
    shelter_id bigint not null,
    animal_id bigint not null
);

create table visitors
(
    id bigserial primary key,
    first_name varchar(20) not null,
    last_name varchar(20) not null,
    phone_number varchar(20),
    email varchar(30)
);

create table visitors_per_shelter
(
    id bigserial primary key,
    shelter_id bigint not null,
    visitor_id bigint not null,
    visit_date date
);

create table adoptions
(
    id bigserial primary key,
    visitor_id bigint not null,
    animal_id bigint not null,
    adoption_date date
);

-- связываем животных с приютами
alter table animals_per_shelter
    add constraint animal_to_shelter_fk foreign key(animal_id) references animals(id);

alter table animals_per_shelter
    add constraint shelter_to_animal_fk foreign key(shelter_id) references shelters(id);

-- связываем посетителей с приютами
alter table visitors_per_shelter
    add constraint visitor_to_shelter_fk foreign key(visitor_id) references visitors(id);

alter table visitors_per_shelter
    add constraint shelter_to_visitor_fk foreign key(shelter_id) references shelters(id);

-- связываем посетителей с животными
alter table adoptions
    add constraint visitor_to_adoption_fk foreign key(visitor_id) references visitors(id);

alter table adoptions
    add constraint animal_to_adoption_fk foreign key(animal_id) references animals(id);
