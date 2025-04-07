create table client
(
    id           bigserial
        primary key,
    address      varchar(255),
    company_name varchar(255),
    industry     varchar(255)
);

alter table client
    owner to postgres;

create table contact
(
    client_id    bigint
        constraint fkt0lxtgfimywi23cewqbmgdu62
            references client,
    id           bigserial
        primary key,
    email        varchar(255),
    first_name   varchar(255),
    last_name    varchar(255),
    phone_number varchar(255)
);

alter table contact
    owner to postgres;

create table notification
(
    id      bigserial
        primary key,
    sent_at timestamp(6),
    type    varchar(255)
        constraint notification_type_check
            check ((type)::text = ANY
                   ((ARRAY ['TASK_STATUS_CHANGED'::character varying, 'TASK_DUE_DATE'::character varying])::text[]))
);

alter table notification
    owner to postgres;

create table task
(
    due_date    date,
    client_id   bigint
        constraint fkphvo4rwjcbuf358bw8p7omyn5
            references client,
    contact_id  bigint
        constraint fkh5fnalwrtlcfo81jswsjccers
            references contact,
    id          bigserial
        primary key,
    description varchar(255),
    status      varchar(255)
        constraint task_status_check
            check ((status)::text = ANY
                   ((ARRAY ['TODO'::character varying, 'IN_PROGRESS'::character varying, 'DONE'::character varying])::text[]))
);

alter table task
    owner to postgres;

create table users
(
    id       bigserial
        primary key,
    password varchar(255),
    username varchar(255)
);

alter table users
    owner to postgres;

create table client_user
(
    client_id bigint not null
        constraint fkcatxs6o2rpy2y6skeqj1t4vl3
            references client,
    user_id   bigint not null
        constraint fkfe38lm3ym3r2i4sg5kxj2wgne
            references users
);

alter table client_user
    owner to postgres;

create table comment
(
    id      bigserial
        primary key,
    sent_at timestamp(6),
    task_id bigint
        constraint fkfknte4fhjhet3l1802m1yqa50
            references task,
    user_id bigint
        constraint fkqm52p1v3o13hy268he0wcngr5
            references users,
    content varchar(255)
);

alter table comment
    owner to postgres;

create table task_comments
(
    comments_id bigint not null
        unique
        constraint fk7sybm6byg0d319yp5b0xkvn9b
            references comment,
    task_id     bigint not null
        constraint fk57giy29i5nak139pefvyvhj9h
            references task
);

alter table task_comments
    owner to postgres;

create table user_notification
(
    dismissed       boolean not null,
    viewed          boolean not null,
    id              bigserial
        primary key,
    notification_id bigint
        constraint fki5naecliicmigrk01qx5me5sp
            references notification,
    user_id         bigint
        constraint fkc2d7aih8weit50jlu4q57cvs
            references users,
    params          jsonb
);

alter table user_notification
    owner to postgres;

INSERT into client (address, company_name, industry)
values ('123 Main St', 'Company A', 'Tech'),
       ('456 Elm St', 'Company B', 'Finance'),
       ('789 Pine St', 'Company C', 'Retail');

insert into users (username, password)
values ('admin', '$2a$12$f7NCJMDwzsMWtES72uukqu/Pxv.IX7PLpaZd32KUqRuHxYhMHI/su'),
       ('user', '$2a$12$f7NCJMDwzsMWtES72uukqu/Pxv.IX7PLpaZd32KUqRuHxYhMHI/su'),
       ('admin_user', '$2a$12$f7NCJMDwzsMWtES72uukqu/Pxv.IX7PLpaZd32KUqRuHxYhMHI/su');

insert into client_user (client_id, user_id)
values (1, 1),
       (1, 2),
       (1, 3),
       (2, 1),
       (2, 2),
       (2, 3),
       (3, 1),
       (3, 2),
       (3, 3);

insert into contact (client_id, first_name, last_name, email, phone_number)
values (1, 'John', 'Johnson', 'john.doe@example.com', '555-1111'),
       (2, 'Jane', 'Smith', 'jane.smith@example.com', '555-2222'),
       (3, 'Alice', 'Johnson', 'alice.johnson@example.com', '555-3333');

insert into task (client_id, contact_id, description, status, due_date)
values (1, 1, 'Task 1 Description', 'TODO', '2025-02-01'),
       (2, 2, 'Task 2 Description', 'IN_PROGRESS', '2025-02-15'),
       (3, 3, 'Task 3 Description', 'DONE', '2025-03-01');

commit;
