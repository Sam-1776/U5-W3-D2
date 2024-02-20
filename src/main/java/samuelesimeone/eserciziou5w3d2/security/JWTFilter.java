package samuelesimeone.eserciziou5w3d2.security;

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
import samuelesimeone.eserciziou5w3d2.entities.Employee;
import samuelesimeone.eserciziou5w3d2.exceptions.UnauthorizedException;
import samuelesimeone.eserciziou5w3d2.services.EmployeeService;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    JWTools jwTools;

    @Autowired
    EmployeeService employeeService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // PRENDAIMO DALLA RICHIESTA IL SUO HEADER E CONTROLLIAMO LA PRESENZA SIA DEL TOKEN SIA DEL PREFISSO BEARER
        // IN CASO DI ASSENZA DI HEADER O DEL PREFISSO PARTE UN ERRORE DI TIPO 401
        String Header = request.getHeader("Authorization");
        if (Header == null || !Header.startsWith("Bearer")) {
            throw new UnauthorizedException("Inserire il token o controllo presenza prefisso");
        }
        // CONTROLLATA LA PRESENZA DEL TOKEN CON PREFISSO SALVIAMO LO SALVIAMO IN UNA VARIABILE
        // RIMUOVENDO IL PREFISSO
        String token = Header.substring(7);
        jwTools.verify(token);
        // PERMETTE DI ESTRAPOLARE L'ID DELL'UTENTE DAL TOKEN IN MODO DA POTERLO CERCARE SETTARLO COME AUTORIZZATO
        // EVITANDO COSI ERRORI DI TIPO 403
        String id = jwTools.takeIdEmployeeFormToken(token);
        Employee user = employeeService.findById(UUID.fromString(id));
        // CON IL TERZO PARAMETRO PASSIAMO IL/I RUOLO/I DELL'UTENTE
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null,user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        // MANDA AVANTI LA CATENA DEI FILTRI
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        // PERMETTE DI FARE RICHIESTE DA TUTTI I PATH IN CUI E' PRESENTE AUTH
        // IN QUESTO CASO TUTTE LE RICHIESTE PRESENTE IN AuthController (Login e Register)
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
