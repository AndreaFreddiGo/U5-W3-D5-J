package andrea_freddi.U5_W3_D5_J.services;

import andrea_freddi.U5_W3_D5_J.entities.Booking;
import andrea_freddi.U5_W3_D5_J.entities.Event;
import andrea_freddi.U5_W3_D5_J.entities.User;
import andrea_freddi.U5_W3_D5_J.exception.BadRequestException;
import andrea_freddi.U5_W3_D5_J.exception.NotFoundException;
import andrea_freddi.U5_W3_D5_J.payloads.BookingPayload;
import andrea_freddi.U5_W3_D5_J.repositories.BookingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

// creo il Service per gestire tutte le operazioni CRUD sulle prenotazioni

@Service
public class BookingsService {
    // inietto il repository per le prenotazioni
    @Autowired
    private BookingsRepository bookingsRepository;

    // inietto il service per gli utenti
    @Autowired
    private UsersService usersService;

    // inietto il service per gli eventi
    @Autowired
    private EventsService eventsService;

    // creo un metodo per salvare una nuova prenotazione
    public Booking save(BookingPayload body) {
        Integer totalBookings = this.bookingsRepository.findAllByEventId(body.event().getId()).size();
        // prima verifico se l'utente e l'evento esistono
        User foundUser = this.usersService.findById(body.user().getId());
        Event foundEvent = this.eventsService.findById(body.event().getId());
        // controllo anche se l'utente ha già prenotato l'evento
        if (this.bookingsRepository.findAllByEventId(body.event().getId()).stream()
                .anyMatch(booking -> booking.getUser().getId().equals(body.user().getId()))) {
            throw new BadRequestException("User already booked this event");
            // ed infine controllo se ci sono ancora posti disponibili per l'evento
        } else if (totalBookings == foundEvent.getCapacity()) {
            throw new BadRequestException("Event is fully booked");
        } else {
            // se è tutto ok, creo una nuova prenotazione
            Booking newBooking = new Booking();
            newBooking.setUser(body.user());
            newBooking.setEvent(body.event());
            // salvo la prenotazione nel database
            return this.bookingsRepository.save(newBooking);
        }
    }

    // creo un metodo per trovare una prenotazione per id
    public Booking findById(UUID bookingId) {
        // Se la prenotazione non esiste, viene lanciata NotFoundException
        return this.bookingsRepository.findById(bookingId).orElseThrow(
                () -> new NotFoundException(bookingId)
        );
    }

    // creo un metodo per trovare tutte le prenotazioni di un utente
    public List<Booking> findAllByUserId(UUID userId) {
        // Se l'utente non esiste, viene lanciata NotFoundException
        User foundUser = this.usersService.findById(userId);
        return this.bookingsRepository.findAllByUserId(foundUser.getId());
    }

    // creo un metodo per trovare tutte le prenotazioni ad un evento
    public List<Booking> findAllByEventId(UUID eventId) {
        // Se l'evento non esiste, viene lanciata NotFoundException
        Event foundEvent = this.eventsService.findById(eventId);
        return this.bookingsRepository.findAllByEventId(foundEvent.getId());
    }

    // creo un metodo per trovare tutte le prenotazioni
    public Page<Booking> findAll(String date, int page, int size, String sortBy) {
        if (size > 20) size = 20; // limito la dimensione massima a 20
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.bookingsRepository.findAll(pageable);
    }

    // creo un metodo per cancellare una prenotazione
    public void findByIdAndDelete(UUID bookingId) {
        // Se la prenotazione non esiste, viene lanciata NotFoundException
        Booking foundBooking = this.findById(bookingId);
        this.bookingsRepository.delete(foundBooking);
    }

    // creo un metodo per aggiornare una prenotazione
    public Booking update(UUID bookingId, BookingPayload body) {
        // Se la prenotazione non esiste, viene lanciata NotFoundException
        Booking foundBooking = this.findById(bookingId);
        // prima verifico se l'utente e l'evento esistono
        User foundUser = this.usersService.findById(body.user().getId());
        Event foundEvent = this.eventsService.findById(body.event().getId());
        // controllo anche se l'utente ha già prenotato l'evento
        if (this.bookingsRepository.findAllByEventId(body.event().getId()).stream()
                .anyMatch(booking -> booking.getUser().getId().equals(body.user().getId()))) {
            throw new BadRequestException("User already booked this event");
            // ed infine controllo se ci sono ancora posti disponibili per l'evento
        } else if (this.bookingsRepository.findAllByEventId(body.event().getId()).size() == foundEvent.getCapacity()) {
            throw new BadRequestException("Event is fully booked");
        } else {
            // se è tutto ok, aggiorno la prenotazione
            foundBooking.setUser(body.user());
            foundBooking.setEvent(body.event());
            // salvo la prenotazione nel database
            return this.bookingsRepository.save(foundBooking);
        }
    }
}
