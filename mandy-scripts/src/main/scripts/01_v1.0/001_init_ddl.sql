
    create table mandy.activity (
        id bigint not null auto_increment,
        label varchar(255) not null,
        position integer not null,
        primary key (id),
        unique (label, position)
    ) ENGINE=InnoDB;

    create table mandy.activity_resource (
        resource_id bigint not null,
        activity_id bigint not null,
        primary key (resource_id, activity_id)
    ) ENGINE=InnoDB;

    create table mandy.imputation (
        id bigint not null auto_increment,
        comment varchar(255),
        date date not null,
        quota float not null,
        activity_id bigint not null,
        resource_id bigint not null,
        primary key (id),
        unique (activity_id, resource_id, date)
    ) ENGINE=InnoDB;

    create table mandy.resource (
        id bigint not null auto_increment,
        first_name varchar(50) not null,
        last_name varchar(50) not null,
        role varchar(25) not null default 'ROLE_USER',
        uid varchar(25) not null,
        primary key (id)
    ) ENGINE=InnoDB;

    alter table mandy.activity_resource 
        add index FK__ACTRES__ACTIVITY_ID (activity_id), 
        add constraint FK__ACTRES__ACTIVITY_ID 
        foreign key (activity_id) 
        references mandy.activity (id);

    alter table mandy.activity_resource 
        add index FK__ACTRES__RESOURCE_ID (resource_id), 
        add constraint FK__ACTRES__RESOURCE_ID 
        foreign key (resource_id) 
        references mandy.resource (id);

    alter table mandy.imputation 
        add index FK__IMP__ACTIVITY_ID (activity_id), 
        add constraint FK__IMP__ACTIVITY_ID 
        foreign key (activity_id) 
        references mandy.activity (id);

    alter table mandy.imputation 
        add index FK__IMP__RESOURCE_ID (resource_id), 
        add constraint FK__IMP__RESOURCE_ID 
        foreign key (resource_id) 
        references mandy.resource (id);
