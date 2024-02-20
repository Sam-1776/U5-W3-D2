package samuelesimeone.eserciziou5w3d2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import samuelesimeone.eserciziou5w3d2.dto.EmployeeDTO;
import samuelesimeone.eserciziou5w3d2.dto.EmployeeLoginDTO;
import samuelesimeone.eserciziou5w3d2.dto.LoginDTO;
import samuelesimeone.eserciziou5w3d2.entities.Employee;
import samuelesimeone.eserciziou5w3d2.exceptions.BadRequestException;
import samuelesimeone.eserciziou5w3d2.services.AuthService;
import samuelesimeone.eserciziou5w3d2.services.EmployeeService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/login")
    public LoginDTO login(@RequestBody EmployeeLoginDTO body){
        return new LoginDTO(authService.authEmployeeAndGenerateToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Employee save(@RequestBody @Validated EmployeeDTO employee, BindingResult validation){
        if (validation.hasErrors()){
            throw new BadRequestException(validation.getAllErrors());
        }
        return this.employeeService.save(employee);
    }

}
