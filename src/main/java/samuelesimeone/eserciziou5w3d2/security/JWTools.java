package samuelesimeone.eserciziou5w3d2.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import samuelesimeone.eserciziou5w3d2.entities.Employee;
import samuelesimeone.eserciziou5w3d2.exceptions.UnauthorizedException;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTools {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(Employee employee){
        // USA LA LIBRERIA JWT PER LA CREAZIONE DEL TOKEN
        // IN QUESTO CASO ABBIAMO NEL BODY:
        // TEMPO DI EMISSIONE E SCADENZA,
        // IL SUBJECT (IN QUESTO CASO L'ID)
        // LA FIRMA FINALE PRENDE UN VALORE DALL'APPLICATIONPROPERTIES
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .subject(String.valueOf(employee.getId()))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public void verify(String token){
        // PERMETTE DI FARE IL CONTROLLO DI EVENTUALI SCADENZE DEL TOKEN O MODIFICHE(LO RENDEREBBE INVALIDO)
        try{
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parse(token);
        }catch (Exception ex){
            throw new UnauthorizedException("Problemi con il token. Rifare login");
        }
    }

    public String takeIdEmployeeFormToken(String token){
        // PERMETTE L'ESTRAPOLAZIONE DEL SUBJECT DAL TOKEN
        return  Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
