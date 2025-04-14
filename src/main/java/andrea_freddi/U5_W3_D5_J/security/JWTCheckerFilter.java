package andrea_freddi.U5_W3_D5_J.security;

import andrea_freddi.U5_W3_D5_J.entities.User;
import andrea_freddi.U5_W3_D5_J.exception.UnauthorizedException;
import andrea_freddi.U5_W3_D5_J.services.UsersService;
import andrea_freddi.U5_W3_D5_J.tools.JWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

// Questa classe è un filtro che viene eseguito ad ogni richiesta HTTP

@Component
public class JWTCheckerFilter extends OncePerRequestFilter {

    // Inietto il JWT per verificare il token
    @Autowired
    private JWT jwt;

    // Inietto il servizio UsersService per cercare l'utente
    @Autowired
    private UsersService usersService;

    // Questo metodo viene eseguito ad ogni richiesta HTTP e controlla se il token è presente e valido
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Inserire token nell'Authorization Header nel formato corretto!");
        String accessToken = authHeader.substring(7);

        jwt.verifyToken(accessToken);

        // se il token è valido, prendo l'id dell'utente dal token
        String userId = jwt.getIdFromToken(accessToken);
        User currentUser = this.usersService.findById(UUID.fromString(userId));

        // trovato l'utente, lo metto nel SecurityContext
        Authentication authentication = new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication); // Aggiorniamo il SecurityContext associandogli l'utente autenticato

        filterChain.doFilter(request, response);
    }

    // Questo metodo serve per escludere alcune richieste dal filtro, in questo caso escludiamo le richieste che iniziano con "/auth/"
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
