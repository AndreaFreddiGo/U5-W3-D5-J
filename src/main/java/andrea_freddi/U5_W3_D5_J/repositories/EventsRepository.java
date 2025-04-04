package andrea_freddi.U5_W3_D5_J.repositories;

import andrea_freddi.U5_W3_D5_J.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventsRepository extends JpaRepository<Event, UUID> {
}
