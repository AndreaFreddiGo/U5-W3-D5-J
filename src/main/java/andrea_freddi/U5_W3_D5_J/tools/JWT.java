package andrea_freddi.U5_W3_D5_J.tools;

// creo la classe JWT per gestire la creazione e la validazione dei token JWT

import andrea_freddi.U5_W3_D5_J.entities.User;
import andrea_freddi.U5_W3_D5_J.exception.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWT {

    // importo secret da application.properties
    @Value("${jwt.secret}")
    private String secret;

    // creo il metodo per generare il token
    public String createToken(User user) {
        // creo il token con i dati dell'utente
        return String.valueOf(Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis())) // data di emissione del token
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // data di scadenza del token
                .subject(String.valueOf(user.getId())) // subject del token
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())) // firmo il token con la secret
                .compact()); // compilo il token
    }

    // creo il metodo per verificare il token
    public void verifyToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes())) // verifico il token con la secret
                    .build().parse(token);
        } catch (Exception e) {
            // se il token non è valido lancio un'eccezione
            throw new UnauthorizedException("Not valid token! Please login again!");
        }
    }

    // creo il metodo per ottenere l'id dal token
    public String getIdFromToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes())) // verifico il token con la secret
                .build().parseSignedClaims(token) // parsifico il token
                .getPayload().getSubject(); // prendo l'id dal subject del payload
    }
}
