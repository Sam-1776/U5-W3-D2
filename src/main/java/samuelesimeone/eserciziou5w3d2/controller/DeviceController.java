package samuelesimeone.eserciziou5w3d2.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import samuelesimeone.eserciziou5w3d2.dto.DeviceDTO;
import samuelesimeone.eserciziou5w3d2.entities.Device;
import samuelesimeone.eserciziou5w3d2.exceptions.BadRequestException;
import samuelesimeone.eserciziou5w3d2.services.DeviceService;

import java.util.UUID;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    DeviceService deviceService;

    @GetMapping
    public Page<Device> getAll(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id") String order){
        return this.deviceService.getAll(page, size, order);
    }

    @GetMapping("/{id}")
    public Device getAutoreById(@PathVariable UUID id){
        return this.deviceService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Device save(@RequestBody @Validated DeviceDTO device, BindingResult validation){
        if (validation.hasErrors()){
            throw new BadRequestException(validation.getAllErrors());
        }
        return this.deviceService.save(device);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Device update(@PathVariable UUID id, @RequestBody DeviceDTO deviceUp){
        return this.deviceService.update(id, deviceUp);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id){
        this.deviceService.delete(id);
    }
}
