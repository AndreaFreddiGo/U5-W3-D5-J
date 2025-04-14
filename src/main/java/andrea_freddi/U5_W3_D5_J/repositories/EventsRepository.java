package andrea_freddi.U5_W3_D5_J.repositories;

import andrea_freddi.U5_W3_D5_J.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventsRepository extends JpaRepository<Event, UUID> {
    // creo un metodo per trovare un evento dal titolo
    Optional<Event> findByTitle(String title);

    // creo un metodo per trovare tutti gli eventi di un utente ed implemento la paginazione
    Page<Event> findAllByUserId(UUID id, Pageable pageable);

    // creo un metodo per trovare tutti gli eventi previsti ad una certa data
    Page<Event> findAllByDate(String date, Pageable pageable);
}
