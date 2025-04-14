package andrea_freddi.U5_W3_D5_J.services;

import andrea_freddi.U5_W3_D5_J.entities.Event;
import andrea_freddi.U5_W3_D5_J.entities.User;
import andrea_freddi.U5_W3_D5_J.exception.BadRequestException;
import andrea_freddi.U5_W3_D5_J.payloads.EventPayload;
import andrea_freddi.U5_W3_D5_J.repositories.EventsRepository;
import andrea_freddi.U5_W3_D5_J.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

// creo il Service per gestire tutte le operazioni CRUD sugli eventi

@Service
public class EventsService {

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private UsersRepository usersRepository;

    // creo un metodo per salvare un nuovo evento
    public Event save(EventPayload body, User user) {
        // controllo se l'evento esiste già
        if (this.eventsRepository.findByTitle(body.title()).isPresent()) {
            // se esiste, lancio un'eccezione
            throw new BadRequestException("Event already exists!");
        }
        // se non esiste, creo un nuovo evento e lo salvo nel database
        Event newEvent = new Event(body.title(), body.description(), body.date(), body.location(), body.capacity(), user);
        return this.eventsRepository.save(newEvent);
    }

    // creo un metodo per trovare tutti gli eventi ed in caso impaginarli
    public Page<Event> findAll(int page, int size, String sortBy) {
        if (size > 20) size = 20; // limito la dimensione massima a 20
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.eventsRepository.findAll(pageable);
    }

    // creo un metodo per trovare tutti gli eventi di un utente
    public Page<Event> findAllByUserId(UUID userId, int page, int size, String sortBy) {
        // controllo se l'utente esiste
        if (this.usersRepository.findById(userId).isEmpty()) {
            // se non esiste, lancio un'eccezione
            throw new BadRequestException("User not found!");
        }
        if (size > 20) size = 20; // limito la dimensione massima a 20
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.eventsRepository.findAllByUserId(userId, pageable);
    }

    // creo un metodo per trovare tutti gli eventi previsti in un certo giorno
    public Page<Event> findAllByDate(String date, int page, int size, String sortBy) {
        if (size > 20) size = 20; // limito la dimensione massima a 20
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.eventsRepository.findAllByDate(date, pageable);
    }

    // creo un metodo per trovare un evento per id
    public Event findById(UUID id) {
        // cerco l'evento per id
        return this.eventsRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Event not found!"));
    }

    // creo un metodo per trovare un evento per titolo
    public Event findByTitle(String title) {
        // cerco l'evento per titolo
        return this.eventsRepository.findByTitle(title).orElseThrow(
                () -> new BadRequestException("Event not found!"));
    }

    // creo un metodo per aggiornare un evento
    public Event update(EventPayload body, UUID id) {
        // cerco l'evento con il metodo findById che gestisce l'eccezione
        Event found = this.findById(id);
        // se lo trovo, aggiorno i campi
        found.setTitle(body.title());
        found.setDescription(body.description());
        found.setDate(body.date());
        found.setLocation(body.location());
        found.setCapacity(body.capacity());
        // salvo le modifiche nel database
        return this.eventsRepository.save(found);
    }

    // creo un metodo per eliminare un evento
    public void deleteById(UUID id) {
        // cerco l'evento con il metodo findById che gestisce l'eccezione
        Event found = this.findById(id);
        // se lo trovo, lo elimino
        this.eventsRepository.delete(found);
    }
}
