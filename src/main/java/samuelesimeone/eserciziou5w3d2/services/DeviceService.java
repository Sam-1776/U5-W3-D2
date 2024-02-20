package samuelesimeone.eserciziou5w3d2.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import samuelesimeone.eserciziou5w3d2.dao.DeviceDAO;
import samuelesimeone.eserciziou5w3d2.dto.DeviceDTO;
import samuelesimeone.eserciziou5w3d2.entities.Device;
import samuelesimeone.eserciziou5w3d2.entities.State;
import samuelesimeone.eserciziou5w3d2.exceptions.BadRequestException;
import samuelesimeone.eserciziou5w3d2.exceptions.NotFoundException;

import java.util.UUID;

@Service
public class DeviceService {

    @Autowired
    DeviceDAO deviceDAO;

    public Page<Device> getAll(int pageN, int pageS, String orderBy){
        Pageable pageable = PageRequest.of(pageN, pageS, Sort.by(orderBy));
        return deviceDAO.findAll(pageable);
    }

    public Device save(DeviceDTO device){
        if (device.state().toLowerCase().equals(State.DISPONIBILE.toString().toLowerCase())){
            Device newDevice = new Device(State.DISPONIBILE, device.numberSeries(), device.type());
            return deviceDAO.save(newDevice);
        } else if (device.state().toLowerCase().equals(State.ASSEGNATO.toString().toLowerCase())) {
            Device newDevice = new Device(State.ASSEGNATO, device.numberSeries(), device.type());
            return deviceDAO.save(newDevice);
        }else if (device.state().toLowerCase().equals(State.DISMESSO.toString().toLowerCase())){
            Device newDevice = new Device(State.DISMESSO, device.numberSeries(), device.type());
            return deviceDAO.save(newDevice);
        }else if (device.state().toLowerCase().equals(State.MANUTENZIONE.toString().toLowerCase())){
            Device newDevice = new Device(State.MANUTENZIONE, device.numberSeries(), device.type());
            return deviceDAO.save(newDevice);
        }
        throw new BadRequestException("Lo stato del dispositivo non è stato inserito correttamente, riprovare");
    }

    public Device findById(UUID id){
        return deviceDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Device update(UUID id, DeviceDTO deviceUp){
        Device found = this.findById(id);
        switch (deviceUp.state().toLowerCase()){
            case "disponibile":
                found.setState(State.DISPONIBILE);
                break;
            case "assegnato":
                found.setState(State.ASSEGNATO);
                break;
            case "dismesso":
                found.setState(State.DISMESSO);
                break;
            case "manutenzione":
                found.setState(State.MANUTENZIONE);
                break;
            default:
                throw new BadRequestException("Lo stato del dispositivo non è stato inserito correttamente, riprovare");
        }
        found.setNumberSeries(deviceUp.numberSeries());
        found.setType(deviceUp.type());
        found.setEmployee(found.getEmployee());
        return deviceDAO.save(found);
    }

    public void delete(UUID id){
        Device found = this.findById(id);
        deviceDAO.delete(found);
    }

}
