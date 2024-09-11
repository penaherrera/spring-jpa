package sv.edu.udb.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.repository.domain.Reservation;
import sv.edu.udb.repository.domain.Room;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    @Transactional
    void shouldMakeReservation_When_RoomIsAvailable() {

        final Long roomId = 3L;
        final String clientName = "John Doe";
        final LocalDate startDate = LocalDate.of(2024, 9, 10);

        // Verificar que la habitación está disponible
        Room room = roomRepository.findById(roomId);
        assertNotNull(room);
        assertEquals("Disponible", room.getStatus());

        // Hacer la reserva
        reservationRepository.makeReservation(roomId, clientName, startDate);

        // Verificar la reserva
        Reservation reservation = reservationRepository.findAll().stream()
                .filter(res -> res.getRoom().getId().equals(roomId))
                .findFirst()
                .orElse(null);

        assertNotNull(reservation);
        assertEquals(clientName, reservation.getClient());
        assertEquals(startDate, reservation.getStartDate());

        // Verificar que la habitación esté marcada como "Reservada"
        Room reservedRoom = roomRepository.findById(roomId);
        assertEquals("Reservada", reservedRoom.getStatus());
    }

    @Test
    @Transactional
    void shouldCancelReservation_When_ReservationExists() {
        // Preparar datos
        final Long reservationId = 1L; // Suponiendo que esta reserva existe

        // Verificar que la reserva existe
        Reservation reservation = reservationRepository.findById(reservationId);
        assertNotNull(reservation);

        // Obtener la habitación asociada
        Room room = reservation.getRoom();
        assertNotNull(room);
        assertEquals("Reservada", room.getStatus());

        // Cancelar la reserva
        reservationRepository.cancelReservation(reservationId);

        // Verificar que la reserva ya no existe
        Reservation cancelledReservation = reservationRepository.findById(reservationId);
        assertNull(cancelledReservation);

        // Verificar que la habitación esté marcada como "Disponible"
        Room updatedRoom = roomRepository.findById(room.getId());
        assertNotNull(updatedRoom);
        assertEquals("Disponible", updatedRoom.getStatus());
    }
}
