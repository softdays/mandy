    alter table mandy.activity 
        add column category char(1) not null default 'P' after `long_label`;

    alter table mandy.activity 
        add column parent_id bigint after `type`;

    alter table mandy.activity 
        add index FK__ACTIVITY__PARENT_ID (parent_id), 
        add constraint FK__ACTIVITY__PARENT_ID 
        foreign key (parent_id) 
        references mandy.activity (id);