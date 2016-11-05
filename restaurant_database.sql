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
restaurant INTEGER NOT NULL REFERENCES restaurant
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
restaurant INTEGER NOT NULL REFERENCES restaurant,
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