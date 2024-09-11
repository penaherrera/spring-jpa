package sv.edu.udb.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.repository.domain.Reservation;
import sv.edu.udb.repository.domain.Room;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ReservationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // Crear Reserva
    @Transactional
    public void makeReservation(Long roomId, String client, LocalDate startDate) {

        // Buscar la habitación por ID
        Room room = entityManager.find(Room.class, roomId);

        if (room == null) {
            throw new IllegalArgumentException("La habitación con el ID proporcionado no existe.");
        }

        // Verificar que no haya reservas existentes para la misma habitación en la fecha proporcionada
        final String QUERY = "select count(res) from Reservation res where res.room.id = :roomId and res.startDate = :startDate";
        TypedQuery<Long> query = entityManager.createQuery(QUERY, Long.class);
        query.setParameter("roomId", roomId);
        query.setParameter("startDate", startDate);

        Long reservationCount = query.getSingleResult();

        if (reservationCount == 0) {
            // Cambiar el estado de la habitación a "Reservada"
            room.setStatus("Reservada");
            entityManager.merge(room);

            // Crear la nueva reserva
            Reservation reservation = Reservation.builder()
                    .room(room)
                    .client(client)
                    .startDate(startDate)
                    .build();
            entityManager.persist(reservation);
        } else {
            throw new IllegalArgumentException("La habitación ya está reservada para la fecha seleccionada.");
        }
    }


    // Cancelar una reserva
    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = entityManager.find(Reservation.class, reservationId);

        if (reservation != null) {
            Room room = reservation.getRoom();
            room.setStatus("Disponible");
            entityManager.merge(room);
            entityManager.remove(reservation);
        } else {
            throw new IllegalArgumentException("La reserva no existe.");
        }
    }

    // Obtener todas las reservas
    public List<Reservation> findAll() {
        final String QUERY = "select r from Reservation r";
        return entityManager.createQuery(QUERY, Reservation.class).getResultList();
    }

    // Obtener una reserva por ID
    public Reservation findById(Long reservationId) {
        return entityManager.find(Reservation.class, reservationId);
    }
}
