package samuelesimeone.eserciziou5w3d2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import samuelesimeone.eserciziou5w3d2.dao.EmployeeDAO;
import samuelesimeone.eserciziou5w3d2.dto.EmployeeDTO;
import samuelesimeone.eserciziou5w3d2.dto.EmployeeLoginDTO;
import samuelesimeone.eserciziou5w3d2.entities.Employee;
import samuelesimeone.eserciziou5w3d2.exceptions.BadRequestException;
import samuelesimeone.eserciziou5w3d2.exceptions.UnauthorizedException;
import samuelesimeone.eserciziou5w3d2.security.JWTools;

@Service
public class AuthService {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    JWTools jwTools;

    @Autowired
    EmployeeDAO employeeDAO;

    @Autowired
    PasswordEncoder passwordEncoder;

    public String authEmployeeAndGenerateToken(EmployeeLoginDTO body) throws UnauthorizedException{
        // CONTROLLA L'ESISTENZA DELL'UTENTE E LA CORRETTEZZA DELLE CREDENZIALI
        // PASSATI I CONTROLLI GENERA IL TOKE E LO RITORNA COME STRINGA
        Employee user = employeeService.findByEmail(body.email());
        if (passwordEncoder.matches(body.password(), user.getPassword())){
            return jwTools.generateToken(user);
        }else {
            throw new UnauthorizedException("Credenziali errate; Riprovare!");
        }
    }

    public Employee save(EmployeeDTO employee){
        employeeDAO.findByEmail(employee.email()).ifPresent(element -> {
            throw new BadRequestException("Email inserita già in uso riprovare");
        });
        String avatar = "https://ui-avatars.com/api/?name=" + employee.name() + "+" + employee.surname();
        Employee newEmployee = new Employee(employee.username(), employee.name(), employee.surname(), employee.email(), passwordEncoder.encode(employee.password()), avatar);
        return employeeDAO.save(newEmployee);
    }
}
