-- For DAOXMLDatabase table called daoxml
-- auto-generated definition
create table daoxml
(
    ext_id         varchar(50) not null
        constraint daoxml_pk
            primary key,
    uuid           varchar(36) not null,
    description    varchar(1000),
    name           varchar(100),
    episode_number varchar(100),
    productionyear varchar(100),
    producedby     varchar(100),
    extendedcast   varchar(1000)
);

alter table daoxml
    owner to postgres;

create unique index daoxml_ext_id_uindex
    on daoxml (ext_id);

create unique index daoxml_uuid_uindex
    on daoxml (uuid);