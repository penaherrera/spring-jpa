package sv.edu.udb.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.repository.domain.Room;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Test
    void shouldReturnAvailableRooms_When_FindAll() {
        int expectedAvailableRooms = 2;
        List<Room> availableRooms = roomRepository.findAvailableRooms();
        assertNotNull(availableRooms);
        assertEquals(expectedAvailableRooms, availableRooms.size());
    }

    @Test
    @Transactional
    void shouldCloseRoom_When_RoomIsClosed() {
        final Long roomId = 1L;
        Room room = roomRepository.findById(roomId);
        assertNotNull(room);
        roomRepository.delete(room);
        Room deletedRoom = roomRepository.findById(roomId);
        assertNull(deletedRoom);
    }

    @Test
    @Transactional
    void shouldUpdateRoomType_When_RoomIsUpdated() {
        final Long roomId = 2L;
        Room room = roomRepository.findById(roomId);
        assertNotNull(room);

        String newType = "Suite";
        room.setType(newType);
        roomRepository.save(room);

        Room updatedRoom = roomRepository.findById(roomId);
        assertNotNull(updatedRoom);
        assertEquals(newType, updatedRoom.getType());
    }

    @Test
    @Transactional
    void shouldChangeRoomStatus_When_RoomIsReservedOrReleased() {
        final Long roomId = 2L;
        Room room = roomRepository.findById(roomId);
        assertNotNull(room);

        // Cambiar estado a Reservada
        room.setStatus("Reservada");
        roomRepository.save(room);

        Room reservedRoom = roomRepository.findById(roomId);
        assertEquals("Reservada", reservedRoom.getStatus());

        // Cambiar estado a Disponible
        room.setStatus("Disponible");
        roomRepository.save(room);

        Room availableRoom = roomRepository.findById(roomId);
        assertEquals("Disponible", availableRoom.getStatus());
    }

    @Test
    @Transactional
    void shouldPersistRoom_When_RoomIsSaved() {
        final Long roomId = 4L;
        final String roomNumber = "104";
        final String roomType = "Doble";
        final String roomStatus = "Disponible";

        Room newRoom = Room.builder()
                .id(roomId)
                .number(roomNumber)
                .type(roomType)
                .status(roomStatus)
                .build();

        roomRepository.save(newRoom);

        Room savedRoom = roomRepository.findById(roomId);
        assertNotNull(savedRoom);
        assertEquals(roomId, savedRoom.getId());
        assertEquals(roomNumber, savedRoom.getNumber());
        assertEquals(roomType, savedRoom.getType());
        assertEquals(roomStatus, savedRoom.getStatus());

        // Eliminar la habitacion para mantener la consistencia en los datos
        roomRepository.delete(newRoom);
    }

    @Test
    void shouldReturnAvailableRooms_When_SearchedByDate() {
        final String date = "2024-09-02"; // Fecha de consulta
        List<Room> availableRooms = roomRepository.findAvailableRoomsByDate(LocalDate.parse(date));
        assertNotNull(availableRooms);
        assertTrue(availableRooms.size() > 0, "Debe haber habitaciones disponibles en la fecha seleccionada.");
    }
}
