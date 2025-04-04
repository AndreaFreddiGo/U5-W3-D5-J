package andrea_freddi.U5_W3_D5_J.exception;

// creo l'eccezione NotFoundException per gestire gli errori 404

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(UUID id) {
        super(id + " not found!");
    }
}