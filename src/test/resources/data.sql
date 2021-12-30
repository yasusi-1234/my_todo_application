-- TRUNCATE TABLE task;
-- TRUNCATE TABLE app_user;

INSERT INTO role (role_name) VALUES('ROLE_GENERAL');

INSERT INTO app_user (last_name, first_name, mail_address, app_user_id, role_id, password) VALUES('青山', '元気', '6TM8ytI8xvJU@xxx.xx.xx', 1, 1, 'password');
INSERT INTO app_user (last_name, first_name, mail_address, app_user_id, role_id, password) VALUES('石田', '真由子', 'L3g9Pmpu4MUyY@xxx.xx.xx', 2, 1, 'password');
INSERT INTO app_user (last_name, first_name, mail_address, app_user_id, role_id, password) VALUES('永田', '喜代', 'MV2i0Y9acmk6cnS@xxx.xx.xx', 3, 1, 'password');
INSERT INTO app_user (last_name, first_name, mail_address, app_user_id, role_id, password) VALUES('古谷', '兼子', 'Rj0t6D3n51SLT@xxx.xx.xx', 4, 1, 'password');
INSERT INTO app_user (last_name, first_name, mail_address, app_user_id, role_id, password) VALUES('加藤', '左京', 'WgY0lw5yPaF97m1@xxx.xx.xx', 5, 1, 'password');

INSERT INTO task (task_id, notice,  app_user_id, progress, task_name, detail, importance, start_datetime, end_datetime) VALUES(1, 1, 3, 58, 'Yjyycdz5vKA', 'meMU861oMVYQhsmSj', 'NORMAL', '2021-12-29 00:00:00', '2022-01-29 00:00:00');
INSERT INTO task (task_id, notice,  app_user_id, progress, task_name, detail, importance, start_datetime, end_datetime) VALUES(2, 0, 3, 4, 'omTWtJq', 'ldAtNGJ8AJFM0H', 'NORMAL', '2021-12-29 00:00:00', '2022-01-29 00:00:00');
INSERT INTO task (task_id, notice,  app_user_id, progress, task_name, detail, importance, start_datetime, end_datetime) VALUES(3, 1, 2, 95, 'cRz7CPzO4DcWF9R1Yi', 'CZH5wba0Xb1obT3I', 'NORMAL', '2021-12-29 00:00:00', '2022-01-29 00:00:00');
INSERT INTO task (task_id, notice,  app_user_id, progress, task_name, detail, importance, start_datetime, end_datetime) VALUES(4, 0, 4, 0, 'llfJxjqp', 'Z5LlJstgbM22', 'NORMAL', '2021-12-29 00:00:00', '2022-01-29 00:00:00');
INSERT INTO task (task_id, notice,  app_user_id, progress, task_name, detail, importance, start_datetime, end_datetime) VALUES(5, 0, 5, 48, 'LLweBTirb6ms', 'YjyOiR', 'NORMAL', '2021-12-29 00:00:00', '2022-01-29 00:00:00');
INSERT INTO task (task_id, notice,  app_user_id, progress, task_name, detail, importance, start_datetime, end_datetime) VALUES(6, 0, 2, 74, '0OLNL2C0gsl5hMNN', 'W1zOYSqQ', 'NORMAL', '2021-12-29 00:00:00', '2022-01-29 00:00:00');
INSERT INTO task (task_id, notice,  app_user_id, progress, task_name, detail, importance, start_datetime, end_datetime) VALUES(7, 1, 2, 15, 'cowhgvKF3cv2', 'FjjtmRalt', 'NORMAL', '2021-12-29 00:00:00', '2022-01-29 00:00:00');
INSERT INTO task (task_id, notice,  app_user_id, progress, task_name, detail, importance, start_datetime, end_datetime) VALUES(8, 0, 5, 2, 'RNx095v4aCtuO', '6ae1W2', 'NORMAL', '2021-12-29 00:00:00', '2022-01-29 00:00:00');
INSERT INTO task (task_id, notice,  app_user_id, progress, task_name, detail, importance, start_datetime, end_datetime) VALUES(9, 0, 2, 23, 'RTytr4ZvYVK', 'm64AgZZLjDcNSk', 'NORMAL', '2021-12-29 00:00:00', '2022-01-29 00:00:00');
INSERT INTO task (task_id, notice,  app_user_id, progress, task_name, detail, importance, start_datetime, end_datetime) VALUES(10, 0, 1, 13, 'i8DyYU', '8iCMa0biXdNwiYFzv', 'NORMAL', '2021-12-29 00:00:00', '2022-01-29 00:00:00');
INSERT INTO task (task_id, notice,  app_user_id, progress, task_name, detail, importance, start_datetime, end_datetime) VALUES(11, 0, 1, 26, 'tVKvwNO6EowP4ptMUX', 'XNqdekwLq4gEN0tw', 'NORMAL', '2021-12-29 00:00:00', '2022-01-29 00:00:00');
INSERT INTO task (task_id, notice,  app_user_id, progress, task_name, detail, importance, start_datetime, end_datetime) VALUES(12, 1, 5, 87, 'RVief', 'bZZruUw', 'NORMAL', '2021-12-29 00:00:00', '2022-01-29 00:00:00');
INSERT INTO task (task_id, notice,  app_user_id, progress, task_name, detail, importance, start_datetime, end_datetime) VALUES(13, 0, 3, 25, 'Be5rRB41uC', 'F2qneQiRli', 'NORMAL', '2021-12-29 00:00:00', '2022-01-29 00:00:00');
INSERT INTO task (task_id, notice,  app_user_id, progress, task_name, detail, importance, start_datetime, end_datetime) VALUES(14, 0, 4, 23, 'MFxQkHFNuzyki', 'pNXwPGG50', 'NORMAL', '2021-12-29 00:00:00', '2022-01-29 00:00:00');
INSERT INTO task (task_id, notice,  app_user_id, progress, task_name, detail, importance, start_datetime, end_datetime) VALUES(15, 0, 1, 84, '8IUUL2H', 'INUhwXx36', 'NORMAL', '2021-12-29 00:00:00', '2022-01-29 00:00:00');
INSERT INTO task (task_id, notice,  app_user_id, progress, task_name, detail, importance, start_datetime, end_datetime) VALUES(16, 0, 2, 17, '6T2GEY4EJLhpcr', 'n1up67SZRCxtZ', 'NORMAL', '2021-12-29 00:00:00', '2022-01-29 00:00:00');
INSERT INTO task (task_id, notice,  app_user_id, progress, task_name, detail, importance, start_datetime, end_datetime) VALUES(17, 0, 4, 22, 'vVEJYjEGnZxatbOSd891', 'KOdljridgIBuEnF', 'NORMAL', '2021-12-29 00:00:00', '2022-01-29 00:00:00');
INSERT INTO task (task_id, notice,  app_user_id, progress, task_name, detail, importance, start_datetime, end_datetime) VALUES(18, 0, 5, 49, 'wb7qK4MaE4gET9oqnL', 'cWFdL9RBDSmB', 'NORMAL', '2021-12-29 00:00:00', '2022-01-29 00:00:00');
INSERT INTO task (task_id, notice,  app_user_id, progress, task_name, detail, importance, start_datetime, end_datetime) VALUES(19, 1, 1, 7, 'XJMJUINZ2c', 'iGTHk52x0ic0', 'NORMAL', '2021-12-29 00:00:00', '2022-01-29 00:00:00');
INSERT INTO task (task_id, notice,  app_user_id, progress, task_name, detail, importance, start_datetime, end_datetime) VALUES(20, 0, 1, 0, 'RdrPJSDVqc4AupU9Z2', 'yASbipW7D', 'NORMAL', '2021-12-29 00:00:00', '2022-01-29 00:00:00');
