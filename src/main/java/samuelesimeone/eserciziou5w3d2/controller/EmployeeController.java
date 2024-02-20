package samuelesimeone.eserciziou5w3d2.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/profile")
    public Employee getProfile(@AuthenticationPrincipal Employee currentUser){
        return currentUser;
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Employee update(@PathVariable UUID id, @RequestBody EmployeeDTO employeeUp){
        return this.employeeService.update(id, employeeUp);
    }

    @PutMapping("/profile")
    public Employee updateMyself(@AuthenticationPrincipal Employee currentUser, @RequestBody EmployeeDTO employeeUp){
        return update(currentUser.getId(), employeeUp);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(@PathVariable UUID id){
        this.employeeService.delete(id);
    }

    @DeleteMapping("/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMySelf(@AuthenticationPrincipal Employee currentUSer){
        this.employeeService.delete(currentUSer.getId());
    }

    @PostMapping("/{id}/device/{deviceId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Employee assignDevice(@PathVariable UUID id, @PathVariable UUID deviceId) throws Exception {
        return this.employeeService.assignDevice(id, deviceId);
    }
}
