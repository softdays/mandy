
    create table mandy.activity (
        id bigint not null auto_increment,
		short_label varchar(10) not null,
        long_label varchar(50) not null,
		type char(1) not null default 'P',
        position integer not null,       
        primary key (id),
        unique (long_label, position, short_label)
    ) ENGINE=InnoDB;

    create table mandy.activity_team (        
        activity_id bigint not null,
		team_id bigint not null,
        primary key (team_id, activity_id)
    ) ENGINE=InnoDB;

    create table mandy.imputation (
        id bigint not null auto_increment,  
		activity_id bigint not null,
        resource_id bigint not null,		
        date date not null,
        quota float not null,        
		comment varchar(255),
        primary key (id),
        unique (activity_id, resource_id, date)
    ) ENGINE=InnoDB;

    create table mandy.resource (
        id bigint not null auto_increment,
		uid varchar(25) not null,
        first_name varchar(50) not null,
        last_name varchar(50) not null,
        role varchar(25) not null default 'ROLE_USER',        
        primary key (id)
    ) ENGINE=InnoDB;

    create table mandy.team (
        id bigint not null auto_increment,
        code varchar(10) not null,
        label varchar(50) not null,
        primary key (id),
        unique (code, label)
    ) ENGINE=InnoDB;

    create table mandy.team_resource (        
        team_id bigint not null,
		resource_id bigint not null,
        primary key (resource_id, team_id)
    ) ENGINE=InnoDB;

    alter table mandy.activity_team 
        add index FK__ACTIVITY_TEAM__ACTIVITY (activity_id), 
        add constraint FK__ACTIVITY_TEAM__ACTIVITY 
        foreign key (activity_id) 
        references mandy.activity (id);

    alter table mandy.activity_team 
        add index FK__ACTIVITY_TEAM__TEAM (team_id), 
        add constraint FK__ACTIVITY_TEAM__TEAM 
        foreign key (team_id) 
        references mandy.team (id);

    alter table mandy.imputation 
        add index FK__IMPUTATION__ACTIVITY (activity_id), 
        add constraint FK__IMPUTATION__ACTIVITY 
        foreign key (activity_id) 
        references mandy.activity (id);

    alter table mandy.imputation 
        add index FK__IMPUTATION__RESOURCE (resource_id), 
        add constraint FK__IMPUTATION__RESOURCE 
        foreign key (resource_id) 
        references mandy.resource (id);

    alter table mandy.team_resource 
        add index FK__TEAM_RESOURCE__TEAM (team_id), 
        add constraint FK__TEAM_RESOURCE__TEAM 
        foreign key (team_id) 
        references mandy.team (id);

    alter table mandy.team_resource 
        add index FK__TEAM_RESOURCE__RESOURCE (resource_id), 
        add constraint FK__TEAM_RESOURCE__RESOURCE 
        foreign key (resource_id) 
        references mandy.resource (id);
