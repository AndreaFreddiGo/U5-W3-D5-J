package andrea_freddi.U5_W3_D5_J.services;

import andrea_freddi.U5_W3_D5_J.entities.User;
import andrea_freddi.U5_W3_D5_J.exception.UnauthorizedException;
import andrea_freddi.U5_W3_D5_J.payloads.LoginPayload;
import andrea_freddi.U5_W3_D5_J.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// creo un servizio per gestire l'autenticazione degli utenti

@Service
public class AuthService {
    // inietto UserService per cercare l'utente
    @Autowired
    private UsersService usersService;
    // inietto JWT per generare il token
    @Autowired
    private JWT jwt;
    // inietto PasswordEncoder per confrontare le password
    @Autowired
    private PasswordEncoder bcrypt;

    // creo un metodo per controllare le credenziali e generare il token
    public String checkCredentialsAndGenerateToken(LoginPayload body) {
        // cerco se nel database esiste un utente con l'email fornita
        User found = this.usersService.findByEmail(body.email());
        // se esiste, confronto la password fornita con quella salvata nel database
        // e se ok, genero il token
        if (bcrypt.matches(body.password(), found.getPassword())) {
            String token = jwt.createToken(found);
            return token;
        } else {
            // se invece la password non corrisponde, lancio un'eccezione
            throw new UnauthorizedException("Wrong password!");
        }
    }
}
