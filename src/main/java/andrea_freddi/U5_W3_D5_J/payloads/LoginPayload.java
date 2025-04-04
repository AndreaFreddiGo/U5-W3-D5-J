package andrea_freddi.U5_W3_D5_J.payloads;

// creo una classe record per il payload di login
// questa classe serve per ricevere i dati di login dal client (email e password)

public record LoginPayload(
        String email,
        String password
) {
}