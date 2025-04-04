package andrea_freddi.U5_W3_D5_J.payloads;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

// creo la classe EventPayload per la creazione di un nuovo evento
// la classe contiene i campi title, description, date, location e capacity
// la classe contiene le annotazioni per la validazione dei campi

public record EventPayload(
        @NotEmpty(message = "Title cannot be empty")
        @Size(max = 20, message = "Title cannot exceed 20 characters")
        String title,
        @Size(max = 100, message = "Description cannot exceed 100 characters")
        String description,
        @NotEmpty(message = "Date cannot be empty")
        @FutureOrPresent(message = "Date must be in the future or present")
        LocalDate date,
        @NotEmpty(message = "Location cannot be empty")
        @Size(max = 20, message = "Location cannot exceed 20 characters")
        String location,
        @NotEmpty(message = "Capacity cannot be empty")
        @Min(value = 1, message = "Capacity must be greater than 0")
        Integer capacity
) {
}
