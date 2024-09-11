--
-- Tablas de la base de datos: `spring-hotel`
--

-- ----------------------------------------------------------------
--  TABLE ROOM
-- ----------------------------------------------------------------
CREATE TABLE ROOM (
    ID BIGINT NOT NULL AUTO_INCREMENT,
    NUMBER VARCHAR(255) NOT NULL,
    TYPE VARCHAR(255) NOT NULL CHECK (TYPE IN ('Individual', 'Doble', 'Suite')),
    STATUS VARCHAR(255) NOT NULL CHECK (STATUS IN ('Disponible', 'Reservada')),
    PRIMARY KEY (ID)
);

-- ----------------------------------------------------------------
--  TABLE RESERVATION
-- ----------------------------------------------------------------
CREATE TABLE RESERVATION (
    ID BIGINT NOT NULL AUTO_INCREMENT,
    ROOM_ID BIGINT NOT NULL,
    CLIENT VARCHAR(255) NOT NULL,
    START_DATE DATE NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT FK_RESERVATION_ROOM_ID
    FOREIGN KEY (ROOM_ID)
    REFERENCES ROOM(ID)
);