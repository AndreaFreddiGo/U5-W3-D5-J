package andrea_freddi.U5_W3_D5_J.repositories;

import andrea_freddi.U5_W3_D5_J.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingsRepository extends JpaRepository<Booking, UUID> {
    // creo un metodo per trovare tutte le prenotazioni ad un evento
    List<Booking> findAllByEventId(UUID id);

    // creo un metodo per trovare tutte le prenotazioni di un utente
    List<Booking> findAllByUserId(UUID id);
}
