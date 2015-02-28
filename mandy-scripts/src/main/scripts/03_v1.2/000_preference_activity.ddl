
    create table mandy.preference_activity (
        preference_id bigint not null,
        activity_id bigint not null,
        activity_order integer not null,
        primary key (preference_id, activity_order)
    ) ENGINE=InnoDB;

    alter table mandy.preference_activity 
        add index FK__PREFERENCE_ACTIVITY__ACTIVITY (activity_id), 
        add constraint FK__PREFERENCE_ACTIVITY__ACTIVITY 
        foreign key (activity_id) 
        references mandy.activity (id);

    alter table mandy.preference_activity 
        add index FK__PREFERENCE_ACTIVITY__PREFERENCE (preference_id), 
        add constraint FK__PREFERENCE_ACTIVITY__PREFERENCE 
        foreign key (preference_id) 
        references mandy.preferences (resource_id);

