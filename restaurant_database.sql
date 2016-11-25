-- noinspection SqlNoDataSourceInspectionForFile
DROP TABLE IF EXISTS city CASCADE;
CREATE TABLE city (
id_city SERIAL PRIMARY KEY,
name_city TEXT NOT NULL
);
DROP TABLE IF EXISTS user_restaurant CASCADE;
CREATE TABLE user_restaurant (
id_user SERIAL PRIMARY KEY,
username VARCHAR(100) NOT NULL UNIQUE,
email VARCHAR(100) NOT NULL UNIQUE,
password TEXT NOT NULL,
name TEXT NOT NULL,
role INTEGER NOT NULL,
token VARCHAR(100) UNIQUE,
phone TEXT,
balance INTEGER NOT NULL,
identification VARCHAR(100) UNIQUE
);
DROP TABLE IF EXISTS restaurant CASCADE;
CREATE TABLE restaurant (
id_restaurant SERIAL PRIMARY KEY,
name_restaurant VARCHAR(100) NOT NULL UNIQUE,
description TEXT,
email VARCHAR(100) NOT NULL UNIQUE,
admin INTEGER NOT NULL REFERENCES user_restaurant
);
DROP TABLE IF EXISTS franchise CASCADE;
CREATE TABLE franchise (
id_franchise SERIAL PRIMARY KEY,
name_franchise VARCHAR(100),
restaurant INTEGER NOT NULL REFERENCES restaurant,
address VARCHAR(100) NOT NULL UNIQUE,
city INTEGER NOT NULL REFERENCES city,
phone TEXT NOT NULL,
latitude DOUBLE PRECISION NOT NULL,
longitude DOUBLE PRECISION NOT NULL,
open_time_week TIME NOT NULL,
close_time_week TIME NOT NULL,
open_time_weekend TIME NOT NULL,
close_time_weekend TIME NOT NULL
);
DROP TABLE IF EXISTS photo CASCADE;
CREATE TABLE photo (
id_photo SERIAL PRIMARY KEY,
url_photo TEXT NOT NULL,
franchise INTEGER NOT NULL REFERENCES franchise
);
DROP TABLE IF EXISTS food_type CASCADE;
CREATE TABLE food_type (
id_food_type SERIAL PRIMARY KEY,
type TEXT NOT NULL
);
DROP TABLE IF EXISTS food_type_restaurant CASCADE;
CREATE TABLE food_type_restaurant (
id_food_type_restaurant SERIAL PRIMARY KEY,
food_type INTEGER NOT NULL REFERENCES food_type,
restaurant INTEGER NOT NULL REFERENCES restaurant
);
DROP TABLE IF EXISTS table_restaurant CASCADE;
CREATE TABLE table_restaurant (
id_table_restaurant SERIAL PRIMARY KEY,
franchise INTEGER NOT NULL REFERENCES franchise,
capacity INTEGER NOT NULL,
available BOOLEAN NOT NULL
);
DROP TABLE IF EXISTS reservation CASCADE;
CREATE TABLE reservation (
id_reservation SERIAL PRIMARY KEY,
user_restaurant
INTEGER NOT NULL REFERENCES user_restaurant,
table_restaurant INTEGER NOT NULL REFERENCES table_restaurant,
date_init TIMESTAMP NOT NULL,
date_end TIMESTAMP NOT NULL,
amount_people INTEGER NOT NULL,
state INTEGER NOT NULL
);
DROP TABLE IF EXISTS dish_type CASCADE;
CREATE TABLE dish_type (
id_dish_type SERIAL PRIMARY KEY,
type TEXT NOT NULL
);
DROP TABLE IF EXISTS dish CASCADE;
CREATE TABLE dish (
id_dish SERIAL PRIMARY KEY,
name_dish TEXT NOT NULL,
description TEXT,
price DOUBLE PRECISION NOT NULL,
restaurant INTEGER NOT NULL REFERENCES restaurant,
type INTEGER NOT NULL REFERENCES dish_type
);
DROP TABLE IF EXISTS delivery CASCADE;
CREATE TABLE delivery (
id_delivery SERIAL PRIMARY KEY,
address TEXT NOT NULL,
phone TEXT NOT NULL,
delivery_user INTEGER NOT NULL REFERENCES user_restaurant,
delivery_status BOOLEAN NOT NULL,
score INTEGER,
comment TEXT
);
DROP TABLE IF EXISTS order_restaurant CASCADE;
CREATE TABLE order_restaurant (
id_delivery_dish SERIAL PRIMARY KEY,
dish INTEGER NOT NULL REFERENCES dish,
amount INTEGER NOT NULL,
type INTEGER NOT NULL,
delivery INTEGER REFERENCES delivery,
reservation INTEGER REFERENCES reservation,
price DOUBLE PRECISION NOT NULL
);
DROP TABLE IF EXISTS payment CASCADE;
CREATE TABLE payment (
id_payment SERIAL PRIMARY KEY,
date TIMESTAMP NOT NULL,
user_payment INTEGER NOT NULL REFERENCES user_restaurant,
type INTEGER NOT NULL,
delivery INTEGER REFERENCES delivery,
reservation INTEGER REFERENCES reservation
);

-- role user

-- 0 : cliente
-- 1 : administrador

-- state reservation

-- 0 : reservada
-- 1 : cancelada
-- 2 : incumplida
-- 3 : completada

-- type order

-- 0 : delivery
-- 1 : reservation

-- type payment

-- 0 : no pagada
-- 1 : pagada


INSERT INTO city VALUES(1,'Medellin');
INSERT INTO city VALUES(2,'Bogota');
INSERT INTO city VALUES(3,'Cali');
INSERT INTO city VALUES(4,'Cartagena');
INSERT INTO user_restaurant VALUES (10,'joluditru','jlditru@gmail.com','joluditru1234','Jorge Diaz',1,NULL,'3103323818',0,'1017228117');
INSERT INTO user_restaurant VALUES (11,'estebancatanoe','esteban.catanoe','esteban1234','Esteban Cataño',1,NULL,'5556532',0,'54297853');
INSERT INTO user_restaurant VALUES (12,'adrianx30','adrianx30@gmail.com','adrianx30','Adrian Jimenez',1,NULL,'123456789',0,'11111111');
INSERT INTO user_restaurant VALUES (13,'joanma','joan@gmail.com','joan1234','joan manuel',0,NULL,'3108502694',0,'1035429901');
INSERT INTO user_restaurant VALUES (14,'pepe','pepe@gmail.com','pepe123','pepe pepe',0,NULL,'3208521469',0,'1234567895');
INSERT INTO user_restaurant VALUES (15,'fulanito_master','fulanito@gmail.com','fulanito1234','Cosme Fulanito',0,NULL,'3214556625',0,'99852143');
INSERT INTO user_restaurant VALUES (16,'peranito16','peranito@gmail.com','peranito1234','peranito perez',0,NULL,'3003003030',0,'159753456');
INSERT INTO restaurant VALUES (10,'Doña Rosa','Restaurante Doña Rosa el mejor de la ciudad','admin@donarosa.com',10);
INSERT INTO restaurant VALUES (11,'Restaurante Rafael','La mejor comida peruana','admin@rafael.com',10);
INSERT INTO restaurant VALUES (12,'DF','Restaurante Mexicano','admin@df.com',11);
INSERT INTO restaurant VALUES (13,'Subway','Toma tu sandwich','admin@subway.com',12);
INSERT INTO restaurant VALUES (14,'McDonalds','La mejor carne de perro','admin@macdonald.com',12);
INSERT INTO franchise VALUES (10,'Doña Rosa Laureles',10,'Cra. 74 #39d-1 a 39d-27',1,'4514545',6.2465769,-75.59389277777777, '08:00:00','20:00:00','08:00:00','22:00:00');
INSERT INTO franchise VALUES (11,'Subway Puerta del Norte',13,'Diagonal 55 No. 34 - 67. Local 10-69',1,'4537178',6.339921, -75.593893, '10:00:00','21:30:00','12:00:00','20:30:00');
INSERT INTO franchise VALUES (12,'McDonalds Poblado',14,'Cra. 43b #9-2 a 9-76',1,'4514545',6.210293 ,-75.571175, '10:00:00','23:00:00','10:00:00','23:00:00');
INSERT INTO franchise VALUES (13,'Restaurante Rafael EMAUS',11,'Calle 70 4 65',2,'4554138',6.210293 ,-75.571175, '10:00:00','23:00:00','10:00:00','23:00:00');
INSERT INTO franchise VALUES (14,'Subway Ruta N',13,'Calle 67 No. 52-20',1,2129605,6.217,-75.567, '10:00:00','21:30:00','12:00:00','20:30:00');
INSERT INTO food_type VALUES(10, 'Colombiana');
INSERT INTO food_type VALUES(11, 'Mexicana');
INSERT INTO food_type VALUES(12, 'Italiana');
INSERT INTO food_type_restaurant VALUES(10, 11, 12);INSERT INTO food_type_restaurant VALUES(11, 12, 13);
INSERT INTO food_type_restaurant VALUES(12, 10, 10);
INSERT INTO table_restaurant VALUES(10,10,4,true);
INSERT INTO table_restaurant VALUES(11,10,6,false);
INSERT INTO table_restaurant VALUES(12,10,8,true);
INSERT INTO table_restaurant VALUES(13,11,4,true);
INSERT INTO table_restaurant VALUES(14,11,4,false);
INSERT INTO table_restaurant VALUES(15,13,4,false);
INSERT INTO reservation VALUES(10, 13, 10, '2016-12-01 11:00:00', '2016-12-01 13:00:00', 4, 0);
INSERT INTO reservation VALUES(11, 14, 10, '2016-12-01 13:30:00', '2016-12-01 15:00:00', 4, 0);
INSERT INTO reservation VALUES(12, 15, 11, '2016-12-01 13:00:00', '2016-12-01 15:00:00', 4, 0);
INSERT INTO reservation VALUES(13, 16, 12, '2016-12-01 14:00:00', '2016-12-01 16:00:00', 4, 0);
INSERT INTO reservation VALUES(14, 13, 13, '2016-12-15 15:00:00', '2016-12-15 17:00:00', 4, 0);
INSERT INTO reservation VALUES(15, 14, 14, '2016-12-24 16:00:00', '2016-12-24 18:00:00', 4, 0);
INSERT INTO reservation VALUES(16, 15, 14, '2016-12-30 14:00:00', '2016-12-30 16:00:00', 4, 0);
INSERT INTO dish_type VALUES (10, 'Sopa');
INSERT INTO dish_type VALUES (11, 'Bebida');
INSERT INTO dish_type VALUES (12, 'Plato Principal');
INSERT INTO dish VALUES (10, '​ Caldo de pollo​ ', '​ Contiene ricos trozos de pollo​ ', 5000, 10, 10);
INSERT INTO dish VALUES (11, '​ Rollo de verduras​ ', '​ Disfruta verduras frescas envueltas en una tortilla de harina​ ', 12000, 10, 12);
INSERT INTO order_restaurant VALUES(1, 10, 1, 1, null, 10, 12000);INSERT INTO order_restaurant VALUES(2, 11, 1, 1, null, 10, 20000);
INSERT INTO order_restaurant VALUES(3, 10, 1, 1, null, 11, 14000);
INSERT INTO order_restaurant VALUES(4, 11, 1, 1, null, 11, 21000);