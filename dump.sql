set schema PUBLIC;

INSERT INTO client (address, company_name, industry)
VALUES ('123 Main St', 'Company A', 'Tech'),
       ('456 Elm St', 'Company B', 'Finance'),
       ('789 Pine St', 'Company C', 'Retail');

INSERT INTO users (username, password)
VALUES ('admin', '$2a$12$f7NCJMDwzsMWtES72uukqu/Pxv.IX7PLpaZd32KUqRuHxYhMHI/su'),
       ('user', '$2a$12$f7NCJMDwzsMWtES72uukqu/Pxv.IX7PLpaZd32KUqRuHxYhMHI/su'),
       ('admin_user', '$2a$12$f7NCJMDwzsMWtES72uukqu/Pxv.IX7PLpaZd32KUqRuHxYhMHI/su');

INSERT INTO client_user (client_id, user_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 1),
       (2, 2),
       (2, 3),
       (3, 1),
       (3, 2),
       (3, 3);

INSERT INTO contact (client_id, first_name, last_name, email, phone_number)
VALUES (1, 'John', 'Johnson', 'john.doe@example.com', '555-1111'),
       (2, 'Jane', 'Smith', 'jane.smith@example.com', '555-2222'),
       (3, 'Alice', 'Johnson', 'alice.johnson@example.com', '555-3333');

INSERT INTO task (client_id, contact_id, description, status, due_date)
VALUES (1, 1, 'Task 1 Description', 'TODO', '2025-02-01'),
       (2, 2, 'Task 2 Description', 'IN_PROGRESS', '2025-02-15'),
       (3, 3, 'Task 3 Description', 'DONE', '2025-03-01');

COMMIT;
