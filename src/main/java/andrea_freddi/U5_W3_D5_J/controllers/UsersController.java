package andrea_freddi.U5_W3_D5_J.controllers;

import andrea_freddi.U5_W3_D5_J.entities.User;
import andrea_freddi.U5_W3_D5_J.exception.BadRequestException;
import andrea_freddi.U5_W3_D5_J.payloads.UserPayload;
import andrea_freddi.U5_W3_D5_J.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// creo la classe UsersController per gestire le richieste HTTP relative agli utenti

/*

1. GET http://localhost:3001/users
2. POST http://localhost:3001/users (+ req.body) --> 201 // questo endpoint viene gestito in AuthController per creare un nuovo utente
3. GET http://localhost:3001/users/{userId}
4. PUT http://localhost:3001/users/{userId} (+ req.body)
5. DELETE http://localhost:3001/users/{userId} --> 204

*/

@RestController
@RequestMapping("/users") // definisco il path per le richieste HTTP
public class UsersController {
    @Autowired
    private UsersService usersService; // inietto il servizio UsersService per gestire le richieste HTTP

    // creo un metodo per gestire la richiesta GET per ottenere tutti gli utenti
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')") // solo gli utenti con ruolo ADMIN possono accedere a questo endpoint
    public Page<User> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "id") String sortBy) {
        // inserisco valori di default per far si che non ci siano errori se il client non ci invia uno dei query parameters
        return this.usersService.findAll(page, size, sortBy);
    }

    // creo un metodo per gestire la richiesta GET per ottenere un utente in base all'id
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User findById(@PathVariable UUID userId) {
        return this.usersService.findById(userId);
    }

    // creo un metodo per gestire la richiesta PUT per aggiornare un utente in base all'id
    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User findByIdAndUpdate(@PathVariable UUID userId, @RequestBody @Validated UserPayload body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Invalid request body!");
        }
        return this.usersService.findByIdAndUpdate(userId, body);
    }

    // creo un metodo per gestire la richiesta DELETE per eliminare un utente in base all'id
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID userId) {
        this.usersService.findByIdAndDelete(userId);
    }

    // creo i metodi per gestire le operazioni sul proprio profilo autenticato

    @GetMapping("/me")
    public User getProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
        return currentAuthenticatedUser;
    }

    @PutMapping("/me")
    public User updateProfile(@AuthenticationPrincipal User currentAuthenticatedUser, @RequestBody @Validated UserPayload body) {
        return this.usersService.findByIdAndUpdate(currentAuthenticatedUser.getId(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
        this.usersService.findByIdAndDelete(currentAuthenticatedUser.getId());
    }
}
