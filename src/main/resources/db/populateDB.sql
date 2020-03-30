DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

DO $$
  DECLARE
    users_id INTEGER := (select id from users where name = 'User');
  BEGIN
    INSERT INTO meals (datetime, description, calories, user_id) VALUES
    (make_timestamp(2020, 1, 30, 10, 0, 0), 'Завтрак', 500, users_id),
    (make_timestamp(2020, 1, 30, 20, 0, 0), 'Ужин', 500, users_id),
    (make_timestamp(2020, 1, 31, 0, 0, 0), 'Еда на граничное значение', 100, users_id),
    (make_timestamp(2020, 1, 31, 10, 0, 0), 'Завтрак', 1000, users_id),
    (make_timestamp(2020, 1, 31, 13, 0, 0), 'Обед', 500, users_id),
    (make_timestamp(2020, 1, 31, 20, 0, 0), 'Ужин', 410, users_id);
  END $$;

DO $$
  DECLARE
    admin_id INTEGER := (select id from users where name = 'Admin');
  BEGIN
    INSERT INTO meals (datetime, description, calories, user_id) VALUES
    (make_timestamp(2015, 6, 1, 14, 0, 0), 'Админ ланч', 510, admin_id),
    (make_timestamp(2015, 6, 1, 21, 0, 0), 'Админ ужин', 1500, admin_id);
  END $$;