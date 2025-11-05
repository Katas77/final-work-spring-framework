-- Очистка (опционально, для повторного запуска)
TRUNCATE TABLE booking, room, hotel, authorities, our_user RESTART IDENTITY CASCADE;

-- Вставка данных в таблицу our_user
INSERT INTO our_user (name, email_address, password)
VALUES
    ('John Doe', 'john.doe@example.com', '$2a$12$Qo.O2dD4YKeoisV5UnA1D.I8Vbje8Zb/qiYtD0X/XsLEma41fyx/i'),  -- password123
    ('Jane Smith', 'jane.smith@example.com', '$2a$12$7QqNc203TWoFAdZA7RiyY.btBXpW8JHyekSeJFqLLCDG.xGUKGdZK'); -- password456

-- Вставка ролей для каждого пользователя
INSERT INTO authorities (authority, user_id)
VALUES
    ('ROLE_USER', 1),
    ('ROLE_ADMIN', 2);

-- Вставка  отелей
INSERT INTO hotel (title, heading_advertisements, city, address, distance, ratings, number_ratings)
VALUES
    ('Grand Hotel', 'Luxury at its finest!', 'Moscow', 'Tverskaya Street, 12', 500, 4.8, 200),
    ('City View Inn', 'Comfortable stay with a view!', 'Saint Petersburg', 'Nevsky Prospect, 45', 300, 4.5, 150),
    ('Alpine Retreat', 'Mountain serenity and comfort', 'Sochi', 'Kurortny Prospekt, 78', 1200, 4.7, 180),
    ('Golden Bay Resort', 'Beachfront paradise', 'Sochi', 'Primorskaya Street, 33', 50, 4.9, 250),
    ('Historic Palace Hotel', 'Step into history with modern luxury', 'Kazan', 'Bauman Street, 22', 800, 4.6, 170);

-- Вставка  комнат
INSERT INTO room (name, description, number, price, maximum_people, unavailable_begin, unavailable_end, hotel_id)
VALUES
    ('Deluxe Suite', 'Spacious suite with city views.', 101, 25000.00, 4, '2026-06-01', '2026-07-15', 1),
    ('Standard Double', 'Comfortable double room.', 102, 12000.00, 2, '2026-08-20', '2026-09-05', 1),
    ('Superior King', 'Elegant king-sized bed room.', 201, 18000.00, 2, '2026-11-25', '2026-12-31', 2),
    ('Alpine Family Cabin', 'Cozy wooden cabin for 4 with mountain view.', 10, 20000.00, 4, '2026-01-10', '2026-02-20', 3),
    ('Oceanfront Villa', 'Private villa steps from the beach.', 1, 45000.00, 6, '2026-07-01', '2026-08-31', 4);



-- Вставка бронирований
INSERT INTO booking (date_check_in, date_check_out, user_id, room_id)
VALUES
    ('2023-05-10', '2023-05-17', 1, 1),  -- John → Deluxe Suite (Grand Hotel)
    ('2023-07-21', '2023-07-24', 2, 3),  -- Jane → Superior King (City View Inn)
    ('2023-09-29', '2023-10-03', 1, 4),  -- John → Alpine Family Cabin
    ('2023-08-05', '2023-08-12', 2, 5),  -- Jane → Oceanfront Villa (Golden Bay)
    ('2024-03-15', '2024-03-20', 1, 2);  -- John → Standard Double