(as root)
su - postgres
psql -c "create user test_username password 'test_password';"
psql -c "create database test_database owner test_username"
(logout from postgres and root)

drop DATABASE winestory;
CREATE DATABASE winestory;

\connect winestory

5432 is the default port


From here://

'/Applications/Postgres.app/Contents/Versions/9.4/bin'/psql -p5432

CREATE USER ownwinestory WITH PASSWORD 'Abcd1234';

drop DATABASE winestory;
CREATE DATABASE winestory;

GRANT ALL PRIVILEGES ON DATABASE winestory to ownwinestory;


'/Applications/Postgres.app/Contents/Versions/9.4/bin'/psql -p5432 -d winestory -U ownwinestory


CREATE SCHEMA winestory;

DROP SEQUENCE user_id_seq;
DROP TABLE winestory.user;

CREATE SEQUENCE user_id_seq;
CREATE TABLE IF NOT EXISTS winestory.user (
  id serial PRIMARY KEY,
  email varchar(50) UNIQUE NOT NULL,
  full_name varchar(100),
  password_salt_hash varchar(200)
);

drop TABLE winestory.session;
CREATE TABLE IF NOT EXISTS winestory.session (
  id char(40) PRIMARY KEY NOT NULL,
  user_id INT NOT NULL,
  time_created timestamp NOT NULL default CURRENT_TIMESTAMP,
  time_updated timestamp NOT NULL default CURRENT_TIMESTAMP
);


DROP SEQUENCE wine_id_seq;
DROP TABLE winestory.wine;

CREATE SEQUENCE wine_id_seq;
CREATE TABLE IF NOT EXISTS winestory.wine (
  id serial PRIMARY KEY,
  name varchar(100) NOT NULL,
  image_path varchar(50),
  tasting_note varchar(1000),
  year varchar(4),
  colour varchar(200),
  nose varchar(200),
  palate varchar(200),
  grapes varchar(200),
  volume int,
  available_stock int,
  price decimal
);

ALTER TABLE winestory.user ADD COLUMN address varchar(200);
ALTER TABLE winestory.user ADD COLUMN postal_code varchar(10);
ALTER TABLE winestory.user ADD COLUMN mobile varchar(10);




DROP SEQUENCE order_id_seq;
DROP TABLE winestory.order;

CREATE SEQUENCE order_id_seq;
CREATE TABLE IF NOT EXISTS winestory.order (
  id serial PRIMARY KEY,
  email varchar(50),
  full_name varchar(100),
  address varchar(200),
  postal_code varchar(10),
  mobile varchar(10),
  sub_total decimal,
  shipping_cost decimal,
  tax decimal,
  total_cost decimal,
  createddt timestamp,
  user_id int,
  other_instructions varchar(1000)
);

DROP SEQUENCE orderItem_id_seq;
DROP TABLE winestory.orderItem;

CREATE SEQUENCE orderItem_id_seq;
CREATE TABLE IF NOT EXISTS winestory.orderItem (
  id serial PRIMARY KEY,
  name varchar(100),
  unit_price decimal,
  quantity int,
  order_id int,
  createddt timestamp,
  user_id int,
  wine_id int
);

