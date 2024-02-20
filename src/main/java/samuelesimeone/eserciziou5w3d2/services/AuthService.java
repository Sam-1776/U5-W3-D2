package samuelesimeone.eserciziou5w3d2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import samuelesimeone.eserciziou5w3d2.dto.EmployeeLoginDTO;
import samuelesimeone.eserciziou5w3d2.entities.Employee;
import samuelesimeone.eserciziou5w3d2.exceptions.UnauthorizedException;
import samuelesimeone.eserciziou5w3d2.security.JWTools;

@Service
public class AuthService {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    JWTools jwTools;

    public String authEmployeeAndGenerateToken(EmployeeLoginDTO body) throws UnauthorizedException{
        // CONTROLLA L'ESISTENZA DELL'UTENTE E LA CORRETTEZZA DELLE CREDENZIALI
        // PASSATI I CONTROLLI GENERA IL TOKE E LO RITORNA COME STRINGA
        Employee user = employeeService.findByEmail(body.email());
        if (user.getPassword().equals(body.password())){
            return jwTools.generateToken(user);
        }else {
            throw new UnauthorizedException("Credenziali errate; Riprovare!");
        }
    }
}
