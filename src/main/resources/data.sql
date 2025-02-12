-- Удаление существующих таблиц (если они есть)
DROP TABLE IF EXISTS booking;
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS hotel;
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS our_user;

-- Создание таблицы our_user, если она еще не существует
CREATE TABLE IF NOT EXISTS our_user (
id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email_address VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Создание таблицы authorities, если она еще не существует
CREATE TABLE IF NOT EXISTS authorities (
 id BIGSERIAL PRIMARY KEY,
    authority VARCHAR(20) NOT NULL,
    user_id BIGINT REFERENCES our_user(id) ON DELETE CASCADE
);

-- Создание таблицы hotel, если она еще не существует
CREATE TABLE IF NOT EXISTS hotel (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    heading_advertisements VARCHAR(255),
    city VARCHAR(255),
    address VARCHAR(255),
    distance BIGINT,
    ratings BIGINT,
    number_ratings BIGINT
);

-- Создание таблицы room, если она еще не существует
CREATE TABLE IF NOT EXISTS room (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    number BIGINT,
    price BIGINT,
    maximum_people BIGINT,
    unavailable_begin DATE,
    unavailable_end DATE,
    hotel_id BIGINT REFERENCES hotel(id) ON DELETE SET NULL
);

-- Создание таблицы booking, если она еще не существует
CREATE TABLE IF NOT EXISTS booking (
      id BIGSERIAL PRIMARY KEY,
       date_check_in DATE NOT NULL,
       date_check_out DATE NOT NULL,
       user_id BIGINT REFERENCES our_user(id) ON DELETE SET NULL,
       room_id BIGINT REFERENCES room(id) ON DELETE SET NULL
);

-- Вставка данных в таблицу our_user
INSERT INTO our_user (name, email_address, password) VALUES ('John Doe', 'john.doe@example.com', '$2a$12$Qo.O2dD4YKeoisV5UnA1D.I8Vbje8Zb/qiYtD0X/XsLEma41fyx/i');--  password123 passwordEncoder().encode
INSERT INTO our_user (name, email_address, password) VALUES ('Jane Smith', 'jane.smith@example.com', '$2a$12$7QqNc203TWoFAdZA7RiyY.btBXpW8JHyekSeJFqLLCDG.xGUKGdZK');-- password456 passwordEncoder().encode

-- Вставка ролей для каждого пользователя
INSERT INTO authorities (authority, user_id) VALUES ('ROLE_USER', 1);
INSERT INTO authorities (authority, user_id) VALUES ('ROLE_ADMIN', 2);

-- Вставка отелей
INSERT INTO hotel (title, heading_advertisements, city, address, distance, ratings, number_ratings)
VALUES ('Grand Hotel', 'Luxury at its finest!', 'Moscow', 'Tverskaya Street, 12', 500, 4.8, 200);
INSERT INTO hotel (title, heading_advertisements, city, address, distance, ratings, number_ratings)
VALUES ('City View Inn', 'Comfortable stay with a view!', 'Saint Petersburg', 'Nevsky Prospect, 45', 300, 4.5, 150);

-- Вставка номеров
INSERT INTO room (name, description, number, price, maximum_people, unavailable_begin, unavailable_end, hotel_id)
VALUES ('Deluxe Suite', 'Spacious suite with city views.', 101, 25000.00, 4, '2023-06-01', '2023-07-15', 1);
INSERT INTO room (name, description, number, price, maximum_people, unavailable_begin, unavailable_end, hotel_id)
VALUES ('Standard Double', 'Comfortable double room.', 102, 12000.00, 2, '2023-08-20', '2023-09-05', 1);
INSERT INTO room (name, description, number, price, maximum_people, unavailable_begin, unavailable_end, hotel_id)
VALUES ('Superior King', 'Elegant king-sized bed room.', 201, 18000.00, 2, '2023-11-25', '2023-12-31', 2);
INSERT INTO room (name, description, number, price, maximum_people, unavailable_begin, unavailable_end, hotel_id)
VALUES ('Family Room', 'Room for up to four guests.', 202, 22000.00, 4, '2023-03-14', '2023-04-30', 2);
INSERT INTO room (name, description, number, price, maximum_people, unavailable_begin, unavailable_end, hotel_id)
VALUES ('Budget Single', 'Cozy single room for solo travelers.', 203, 8000.00, 1, '2023-02-18', '2023-03-28', 2);

-- Вставка бронирований
INSERT INTO booking (date_check_in, date_check_out, user_id, room_id)
VALUES ('2023-05-10', '2023-05-17', 1, 1);
INSERT INTO booking (date_check_in, date_check_out, user_id, room_id)
VALUES ('2023-07-21', '2023-07-24', 2, 3);
INSERT INTO booking (date_check_in, date_check_out, user_id, room_id)
VALUES ('2023-09-29', '2023-10-03', 1, 4);