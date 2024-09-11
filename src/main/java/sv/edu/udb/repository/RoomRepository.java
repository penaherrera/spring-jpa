package sv.edu.udb.repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import jakarta.persistence.TypedQuery;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.repository.domain.Room;
import java.util.List;
import java.time.LocalDate;

@Repository
public class RoomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    //Todas las habitaciones
    public List<Room> findAll() {
        final String QUERY = "select r from Room r";
        return entityManager
                .createQuery(QUERY, Room.class)
                .getResultList();
    }

    //Habitaciones Disponibles
    public List<Room> findAvailableRooms() {
        final String QUERY = "select r from Room r where r.status = 'Disponible'";
        TypedQuery<Room> query = entityManager.createQuery(QUERY, Room.class);
        return query.getResultList();
    }

    //Habitaciones Disponibles por fecha
    public List<Room> findAvailableRoomsByDate(LocalDate date) {
        final String QUERY = "select r from Room r where r.status = 'Disponible' and r.id not in "
                            + "(select res.room.id from Reservation res where res.startDate = :date)";
        TypedQuery<Room> query = entityManager.createQuery(QUERY, Room.class);
        query.setParameter("date", date);
        return query.getResultList();
    }

    //Encontrar Habitacion
    public Room findById(final Long id) {
        return entityManager.find(Room.class, id);
    }

    //Crear Habitacion
    @Transactional
    public void save(final Room room) {
        entityManager.persist(room);
    }

    //Eliminar Habitacion
    public void delete(final Room room) {
        entityManager.remove(room);
    }

    //Editar Habitacion
    @Transactional
    public void updateRoomType(final Long id, final String newType) {
        Room room = entityManager.find(Room.class, id);
        if (room != null) {
            room.setType(newType);
            entityManager.merge(room);
        }
    }
}
