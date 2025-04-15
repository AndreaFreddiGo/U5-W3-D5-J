package andrea_freddi.U5_W3_D5_J.controllers;

import andrea_freddi.U5_W3_D5_J.entities.Event;
import andrea_freddi.U5_W3_D5_J.entities.User;
import andrea_freddi.U5_W3_D5_J.exception.BadRequestException;
import andrea_freddi.U5_W3_D5_J.payloads.EventPayload;
import andrea_freddi.U5_W3_D5_J.services.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// creo la classe EventsController per gestire le richieste HTTP relative agli eventi

/*

1. GET http://localhost:3001/events
2. POST http://localhost:3001/events (+ req.body) --> 201
3. GET http://localhost:3001/events/{eventId}
4. PUT http://localhost:3001/events/{eventId} (+ req.body)
5. DELETE http://localhost:3001/events/{eventId} --> 204

*/

@RestController
@RequestMapping("/events") // definisco il path per la classe EventsController
public class EventsController {
    @Autowired
    private EventsService eventsService; // inietto il servizio EventsService per gestire gli eventi

    // creo un metodo per ottenere tutti gli eventi e faccio in modo che la richiesta possa essere fatta da tutti gli utenti
    @GetMapping
    public Page<Event> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id") String sortBy) {
        // inserisco valori di default per far si che non ci siano errori se il client non ci invia uno dei query parameters
        return this.eventsService.findAll(page, size, sortBy);
    }

    // creo un metodo per ottenere un evento in base all'id
    @GetMapping("/{eventId}")
    public Event findById(@RequestParam UUID eventId) {
        return this.eventsService.findById(eventId);
    }

    // creo un metodo per ottenere tutti gli eventi creati da un utente autenticato e con ruolo events_planner
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('EVENTS_PLANNER')")
    // solo gli utenti con ruolo events_planner possono vedere i propri eventi
    public Page<Event> findAllByUserId(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "id") String sortBy,
                                       @AuthenticationPrincipal User currentAuthenticatedUser) {
        // inserisco valori di default per far si che non ci siano errori se il client non ci invia uno dei query parameters
        return this.eventsService.findAllByUserId(currentAuthenticatedUser.getId(), page, size, sortBy);
    }

    // creo un metodo per creare un evento (l'evento potrà essere creato solo da un utente autenticato
    // e con ruolo events_planner)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('EVENTS_PLANNER')") // solo gli utenti con ruolo events_planner possono creare eventi
    public Event save(@RequestBody @Validated EventPayload body, @AuthenticationPrincipal User currentAuthenticatedUser, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Invalid request body!");
        }
        return this.eventsService.save(body, currentAuthenticatedUser);
    }

    // creo un metodo per modificare un evento in base all'id (l'evento potrà essere modificato solo
    // dall'utente autenticato e con ruolo events_planner che ha creato l'evento)
    @PutMapping("/{eventId}")
    @PreAuthorize("hasAuthority('EVENTS_PLANNER')")
    // solo gli utenti con ruolo events_planner possono modificare eventi
    public Event findByIdAndUpdate(@RequestBody @Validated EventPayload body, @PathVariable UUID eventId,
                                   @AuthenticationPrincipal User currentAuthenticatedUser, BindingResult validationResult) {
        // per prima cosa verifico se l'evento esiste e se l'utente autenticato è il creatore dell'evento
        Event found = this.eventsService.findById(eventId);
        if (found.getUser().getId() != currentAuthenticatedUser.getId()) {
            throw new BadRequestException("You are not the creator of this event!");
        }
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Invalid request body!");
        }
        return this.eventsService.findByIdAndUpdate(body, eventId);
    }

    // creo un metodo per eliminare un evento in base all'id (l'evento potrà essere eliminato solo
    // dall'utente autenticato e con ruolo events_planner che ha creato l'evento)
    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasAuthority('EVENTS_PLANNER')") // solo gli utenti con ruolo events_planner possono eliminare eventi
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID eventId, @AuthenticationPrincipal User currentAuthenticatedUser) {
        // per prima cosa verifico se l'evento esiste e se l'utente autenticato è il creatore dell'evento
        Event found = this.eventsService.findById(eventId);
        if (found.getUser().getId() != currentAuthenticatedUser.getId()) {
            throw new BadRequestException("You are not the creator of this event!");
        }
        this.eventsService.findByIdAndDelete(eventId);
    }
}
