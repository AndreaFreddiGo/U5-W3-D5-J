package andrea_freddi.U5_W3_D5_J.controllers;

import andrea_freddi.U5_W3_D5_J.entities.Booking;
import andrea_freddi.U5_W3_D5_J.entities.Event;
import andrea_freddi.U5_W3_D5_J.entities.User;
import andrea_freddi.U5_W3_D5_J.exception.BadRequestException;
import andrea_freddi.U5_W3_D5_J.payloads.BookingPayload;
import andrea_freddi.U5_W3_D5_J.services.BookingsService;
import andrea_freddi.U5_W3_D5_J.services.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// creo la classe BookingsController per gestire le richieste HTTP relative alle prenotazioni

/*

1. GET http://localhost:3001/bookings
2. POST http://localhost:3001/bookings (+ req.body) --> 201
3. GET http://localhost:3001/bookings/{bookingId}
4. PUT http://localhost:3001/bookings/{bookingId} (+ req.body)
5. DELETE http://localhost:3001/bookings/{bookingId} --> 204

*/

@RestController
@RequestMapping("/bookings") // definisco il path per le richieste HTTP
public class BookingsController {
    @Autowired
    private BookingsService bookingsService; // inietto il servizio per gestire le prenotazioni
    @Autowired
    private EventsService eventsService; // inietto il servizio per gestire gli eventi

    // creo un metodo per ottenere tutte le prenotazioni (può essere richiesto solo da un admin)
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')") // solo gli admin possono accedere a questo endpoint
    public Page<Booking> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "id") String sortBy) {
        // inserisco valori di default per far si che non ci siano errori se il client non ci invia uno dei query parameters
        return this.bookingsService.findAll(page, size, sortBy);
    }

    // creo un metodo per ottenere tutte le prenotazioni di un utente (può essere richiesto solo da un utente autenticato)
    @GetMapping("/me") // definisco il path per le richieste HTTP
    public List<Booking> findAllByUserId(@AuthenticationPrincipal User currentAuthenticatedUser) {
        // restituisco tutte le prenotazioni dell'utente autenticato
        return this.bookingsService.findAllByUserId(currentAuthenticatedUser.getId());
    }

    // creo un metodo per ottenere tutte le prenotazioni di un evento (può essere richiesto solo dal creatore dell'evento)
    @GetMapping("/{eventId}") // definisco il path per le richieste HTTP
    @PreAuthorize("hasAuthority('EVENTS_PLANNER')") // solo gli utenti con ruolo events_planner possono creare eventi
    public List<Booking> findAllByEventId(@AuthenticationPrincipal User currentAuthenticatedUser, @RequestParam UUID eventId) {
        // prima verifico se esiste l'evento e se è stato creato dall'utente autenticato
        // se non esiste l'evento o non è stato creato dall'utente autenticato, lancio un'eccezione
        Event found = this.eventsService.findById(eventId);
        if (found.getUser().getId() != currentAuthenticatedUser.getId()) {
            throw new BadRequestException("You are not the creator of this event!");
        }
        // restituisco tutte le prenotazioni dell'evento
        return this.bookingsService.findAllByEventId(eventId);
    }

    // creo un metodo per creare una nuova prenotazione (può essere richiesto solo da un utente autenticato)
    @PostMapping // definisco il path per le richieste HTTP
    @ResponseStatus(HttpStatus.CREATED)
    public Booking save(@AuthenticationPrincipal User currentAuthenticatedUser, @RequestBody BookingPayload booking) {
        // prima verifico se l'evento esiste
        Event found = this.eventsService.findById(booking.event().getId());
        // se l'evento esiste, creo la prenotazione
        return this.bookingsService.save(booking, currentAuthenticatedUser);
    }

    // creo un metodo per eliminare una prenotazione (può essere richiesto solo dal creatore della prenotazione)
    @DeleteMapping("/{bookingId}") // definisco il path per le richieste HTTP
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@AuthenticationPrincipal User currentAuthenticatedUser, @PathVariable UUID bookingId) {
        // prima verifico se la prenotazione esiste
        Booking found = this.bookingsService.findById(bookingId);
        // se la prenotazione esiste e l'utente autenticato è il creatore della prenotazione, elimino la prenotazione
        if (found.getUser().getId() == currentAuthenticatedUser.getId()) {
            this.bookingsService.findByIdAndDelete(bookingId);
        } else {
            throw new BadRequestException("You are not the creator of this booking!");
        }
    }
}
