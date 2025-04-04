package andrea_freddi.U5_W3_D5_J.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

// creo la classe UserPayload per la registrazione di un nuovo utente
// la classe UserPayload contiene i campi name, surname, email e password
// ho aggiunto le annotazioni per la validazione dei campi

public record UserPayload(
        @NotEmpty(message = "Name cannot be empty")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        String name,
        @NotEmpty(message = "Surname cannot be empty")
        @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
        String surname,
        @NotEmpty(message = "Email cannot be empty")
        @Email(message = "Email must be valid")
        String email,
        @NotEmpty(message = "Password cannot be empty")
        @Size(min = 4, message = "Password must be at least 4 characters")
        String password
) {
}
