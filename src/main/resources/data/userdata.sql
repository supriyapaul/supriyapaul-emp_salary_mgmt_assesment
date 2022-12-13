DROP TABLE IF EXISTS USER;
CREATE TABLE USER (
   id VARCHAR(50) VARCHAR(50),
   login VARCHAR(50) NOT NULL,
   name VARCHAR(20) NOT NULL,
   salary  NUMERIC(10,5) NOT NULL,
   startDate VARCHAR(10) NOT NULL,
   UNIQUE KEY login (login)
);