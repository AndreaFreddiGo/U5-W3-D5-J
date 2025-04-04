package andrea_freddi.U5_W3_D5_J.exception;

// creo una classe di eccezione personalizzata per gestire gli errori 400

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
