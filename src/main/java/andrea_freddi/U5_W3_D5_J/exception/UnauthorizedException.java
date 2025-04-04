package andrea_freddi.U5_W3_D5_J.exception;

// creo una nuova eccezione per gestire gli errori di autenticazione 401

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
