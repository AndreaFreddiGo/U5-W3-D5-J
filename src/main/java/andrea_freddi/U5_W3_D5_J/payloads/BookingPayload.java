package andrea_freddi.U5_W3_D5_J.payloads;

import andrea_freddi.U5_W3_D5_J.entities.Event;
import andrea_freddi.U5_W3_D5_J.entities.User;

// creo la classe BookingPayload per effettuare una nuova prenotazione
// avrò bisogno di un utente e di un evento (id di entrambi)

public record BookingPayload(
        User user,
        Event event
) {
}
