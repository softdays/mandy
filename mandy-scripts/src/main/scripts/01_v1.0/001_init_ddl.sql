
    create table activity (
        id bigint not null auto_increment,
        label varchar(255) not null,
        position integer not null,
        primary key (id),
        unique (label, position)
    ) ENGINE=InnoDB;

    create table activity_resource (
        resource_id bigint not null,
        activity_id bigint not null,
        primary key (resource_id, activity_id)
    ) ENGINE=InnoDB;

    create table imputation (
        id bigint not null auto_increment,
        comment varchar(255),
        date date not null,
        quota float not null,
        activity_id bigint not null,
        resource_id bigint not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table resource (
        id bigint not null auto_increment,
        first_name varchar(255) not null,
        last_name varchar(255) not null,
        primary key (id)
    ) ENGINE=InnoDB;

    alter table activity_resource 
        add index FK__ACTRES__ACTIVITY_ID (activity_id), 
        add constraint FK__ACTRES__ACTIVITY_ID 
        foreign key (activity_id) 
        references activity (id);

    alter table activity_resource 
        add index FK__ACTRES__RESOURCE_ID (resource_id), 
        add constraint FK__ACTRES__RESOURCE_ID 
        foreign key (resource_id) 
        references resource (id);

    alter table imputation 
        add index FK__IMP__ACTIVITY_ID (activity_id), 
        add constraint FK__IMP__ACTIVITY_ID 
        foreign key (activity_id) 
        references activity (id);

    alter table imputation 
        add index FK__IMP__RESOURCE_ID (resource_id), 
        add constraint FK__IMP__RESOURCE_ID 
        foreign key (resource_id) 
        references resource (id);
