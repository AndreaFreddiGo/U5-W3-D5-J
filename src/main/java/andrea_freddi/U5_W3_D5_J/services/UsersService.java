package andrea_freddi.U5_W3_D5_J.services;

import andrea_freddi.U5_W3_D5_J.entities.User;
import andrea_freddi.U5_W3_D5_J.exception.BadRequestException;
import andrea_freddi.U5_W3_D5_J.payloads.UserPayload;
import andrea_freddi.U5_W3_D5_J.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

// creo il Service per gestire tutte le operazioni CRUD sui dipendenti

@Service
public class UsersService {
    // inietto il repository per accedere al database
    @Autowired
    private UsersRepository usersRepository;

    // inietto PasswordEncoder per criptare le password
    @Autowired
    private PasswordEncoder bcrypt;

    // creo un metodo per salvare un nuovo utente
    public User save(UserPayload body) {
        // controllo se esiste già un utente con quell'email
        this.usersRepository.findByEmail(body.email()).ifPresent(
                // se esiste, lancio un'eccezione
                user -> {
                    throw new BadRequestException("Email " + body.email() + " already in use!");
                }
        );
        // se non esiste, creo un nuovo utente
        User newUser = new User(body.name(), body.surname(), body.email(),
                bcrypt.encode(body.password()));
        // salvo il nuovo utente
        return this.usersRepository.save(newUser);
    }

    // creo un metodo per trovare un utente per id
    public User findById(UUID id) {
        // se non trovo l'utente, lancio un'eccezione
        return this.usersRepository.findById(id).orElseThrow(
                () -> new BadRequestException("User with id " + id + " not found!")
        );
    }

    // creo un metodo per trovare un utente per email
    public User findByEmail(String email) {
        // se non trovo l'utente, lancio un'eccezione
        return this.usersRepository.findByEmail(email).orElseThrow(
                () -> new BadRequestException("User with email " + email + " not found!")
        );
    }

    // creo un metodo per trovare tutti gli utenti ed eventualmente impaginarli
    public Page<User> findAll(int page, int size, String sortBy) {
        if (size > 20) size = 20; // limito la dimensione massima a 20
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.usersRepository.findAll(pageable);
    }

    // creo un metodo per aggiornare un utente
    public User findByIdAndUpdate(UUID id, UserPayload body) {
        // cerco l'utente con il metodo findById che gestisce già l'eccezione
        User found = this.findById(id);
        if (!found.getEmail().equals(body.email())) {
            // se è previsto l'aggiornamento dell'email, controllo se esiste già un utente con quell'email
            this.usersRepository.findByEmail(body.email()).ifPresent(
                    user -> {
                        throw new BadRequestException("Email " + body.email() + " already in use!");
                    }
            );
        }
        // se trovo l'utente, aggiorno i campi
        found.setName(body.name());
        found.setSurname(body.surname());
        found.setEmail(body.email());
        found.setPassword(bcrypt.encode(body.password()));
        // salvo l'utente aggiornato
        return this.usersRepository.save(found);
    }

    // creo un metodo per eliminare un utente
    public void findByIdAndDelete(UUID id) {
        // cerco l'utente con il metodo findById che gestisce già l'eccezione
        User found = this.findById(id);
        // elimino l'utente
        this.usersRepository.delete(found);
    }
}
