-- Insertando datos en Room
INSERT INTO ROOM (
    NUMBER,
    TYPE,
    STATUS
)
VALUES (
           '101',
           'Individual',
           'Disponible'
       ),
       (
           '102',
           'Doble',
           'Reservada'
       ),
       (
           '103',
           'Suite',
           'Disponible'
       );

-- Insertando datos en Reservation
INSERT INTO RESERVATION (
    ROOM_ID,
    CLIENT,
    START_DATE
)
VALUES (
           2,
           'David Heinemeier Hansson',
           '2024-09-01'
       );
