package samuelesimeone.eserciziou5w3d2.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import samuelesimeone.eserciziou5w3d2.dto.EmployeeDTO;
import samuelesimeone.eserciziou5w3d2.entities.Employee;
import samuelesimeone.eserciziou5w3d2.services.EmployeeService;

import java.util.UUID;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;
    @GetMapping
    public Page<Employee> getAll(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "id") String order){
        return this.employeeService.getAll(page, size, order);
    }

    @GetMapping("/{id}")
    public Employee getAutoreById(@PathVariable UUID id){
        return this.employeeService.findById(id);
    }


    @PutMapping("/{id}")
    public Employee update(@PathVariable UUID id, @RequestBody EmployeeDTO employeeUp){
        return this.employeeService.update(id, employeeUp);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id){
        this.employeeService.delete(id);
    }

    @PostMapping("/{id}/device/{deviceId}")
    public Employee assignDevice(@PathVariable UUID id, @PathVariable UUID deviceId) throws Exception {
        return this.employeeService.assignDevice(id, deviceId);
    }
}
