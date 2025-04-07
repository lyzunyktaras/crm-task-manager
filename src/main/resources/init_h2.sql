set schema PUBLIC;

create table client
(
    id BIGINT AUTO_INCREMENT primary key,
    address VARCHAR(255),
    company_name VARCHAR(255),
    industry VARCHAR(255)
);

create table contact
(
    client_id BIGINT,
    id BIGINT AUTO_INCREMENT primary key,
    email VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone_number VARCHAR(255),
    constraint fkt0lxtgfimywi23cewqbmgdu62 foreign key (client_id) references client(id)
);

create table notification
(
    id BIGINT AUTO_INCREMENT primary key,
    sent_at TIMESTAMP,
    type VARCHAR(255),
    constraint notification_type_check check (type in ('TASK_STATUS_CHANGED', 'TASK_DUE_DATE'))
);

create table task
(
    due_date DATE,
    client_id BIGINT,
    contact_id BIGINT,
    id BIGINT AUTO_INCREMENT primary key,
    description VARCHAR(255),
    status VARCHAR(255),
    constraint task_status_check check (status in ('TODO', 'IN_PROGRESS', 'DONE')),
    constraint fkphvo4rwjcbuf358bw8p7omyn5 foreign key (client_id) references client(id),
    constraint fkh5fnalwrtlcfo81jswsjccers foreign key (contact_id) references contact(id)
);

create table users
(
    id BIGINT AUTO_INCREMENT primary key,
    password VARCHAR(255),
    username VARCHAR(255)
);

create table client_user
(
    client_id BIGINT not null,
    user_id BIGINT not null,
    constraint fkcatxs6o2rpy2y6skeqj1t4vl3 foreign key (client_id) references client(id),
    constraint fkfe38lm3ym3r2i4sg5kxj2wgne foreign key (user_id) references users(id)
);

create table comment
(
    id BIGINT AUTO_INCREMENT primary key,
    sent_at TIMESTAMP,
    task_id BIGINT,
    user_id BIGINT,
    content VARCHAR(255),
    constraint fkfknte4fhjhet3l1802m1yqa50 foreign key (task_id) references task(id),
    constraint fkqm52p1v3o13hy268he0wcngr5 foreign key (user_id) references users(id)
);

create table task_comment
(
    comments_id BIGINT not null unique,
    task_id BIGINT not null,
    constraint fk7sybm6byg0d319yp5b0xkvn9b foreign key (comments_id) references comment(id),
    constraint fk57giy29i5nak139pefvyvhj9h foreign key (task_id) references task(id)
);

create table user_notification
(
    dismissed BOOLEAN not null,
    viewed BOOLEAN not null,
    id BIGINT AUTO_INCREMENT primary key,
    notification_id BIGINT,
    user_id BIGINT,
    params JSON,
    constraint fki5naecliicmigrk01qx5me5sp foreign key (notification_id) references notification(id),
    constraint fkc2d7aih8weit50jlu4q57cvs foreign key (user_id) references users(id)
);

set schema PUBLIC;

insert into client (address, company_name, industry)
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
