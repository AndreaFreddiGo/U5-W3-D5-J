package andrea_freddi.U5_W3_D5_J.controllers;

import andrea_freddi.U5_W3_D5_J.entities.User;
import andrea_freddi.U5_W3_D5_J.exception.BadRequestException;
import andrea_freddi.U5_W3_D5_J.payloads.LoginDTO;
import andrea_freddi.U5_W3_D5_J.payloads.LoginPayload;
import andrea_freddi.U5_W3_D5_J.payloads.UserPayload;
import andrea_freddi.U5_W3_D5_J.services.AuthService;
import andrea_freddi.U5_W3_D5_J.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

// creo la classe AuthController per gestire le richieste di autenticazione e registrazione

@RestController
@RequestMapping("/auth") // definisco il path per le richieste di autenticazione
public class AuthController {
    @Autowired
    private AuthService authService; // inietto il servizio di autenticazione

    @Autowired
    private UsersService usersService; // inietto il servizio di gestione utenti

    // creo un metodo per gestire il login degli utenti
    @PostMapping("/login")
    public LoginDTO login(@RequestBody LoginPayload body) {
        // il metodo riceve un oggetto LoginPayload con email e password e restituisce un oggetto LoginDTO con il token
        return new LoginDTO(this.authService.checkCredentialsAndGenerateToken(body));
    }

    // creo un metodo per gestire la registrazione degli utenti
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED) // restituisco lo status 201 Created
    public User save(@RequestBody @Validated UserPayload body, BindingResult validationResult) {
        // il metodo riceve un oggetto UserPayload con i dati dell'utente e restituisce l'oggetto User salvato nel database
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }
        return this.usersService.save(body);
    }

}


