-- change type of column animal_type in table animals
alter table animals
    alter column animal_type type varchar(16) using animal_type::varchar;

-- change type of column shelter_type in table animals
alter table shelters
alter column shelter_type type varchar(16) using shelter_type::varchar;