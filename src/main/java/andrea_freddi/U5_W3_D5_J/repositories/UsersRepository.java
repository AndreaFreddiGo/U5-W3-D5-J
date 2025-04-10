package andrea_freddi.U5_W3_D5_J.repositories;

import andrea_freddi.U5_W3_D5_J.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<User, UUID> {
    // creo un metodo per trovare un utente per email (se esiste)
    Optional<User> findByEmail(String email);
}
