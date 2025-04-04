package andrea_freddi.U5_W3_D5_J.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

// creo la classe Booking e gestisco Getter e Setter e costruttore vuoto con lombok
// invece il costruttore lo gestisco io così come il toString

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @Setter(AccessLevel.NONE) // non voglio che venga settato dall'esterno
    @GeneratedValue
    @Column(name = "booking_id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    public Booking(UUID id, User user, Event event) {
        this.id = id;
        this.user = user;
        this.event = event;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", user=" + user +
                ", event=" + event +
                '}';
    }
}
