create table mandy.preferences (
        resource_id bigint not null,
        quota float not null default 0.25,
        primary key (resource_id)
    ) ENGINE=InnoDB;

alter table mandy.preferences 
        add constraint FK__PREFERENCES__RESOURCE 
        foreign key (resource_id) 
        references mandy.resource (id);