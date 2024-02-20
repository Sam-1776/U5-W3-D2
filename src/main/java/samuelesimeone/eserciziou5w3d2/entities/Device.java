package samuelesimeone.eserciziou5w3d2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Device {
    @Id
    @GeneratedValue
    private UUID id;
    @Enumerated(EnumType.STRING)
    private State state;
    private String numberSeries;
    @ManyToOne
    @JsonIgnore
    private Employee employee;
    private String type;

    public Device(State state, String numberSeries, String type) {
        this.state = state;
        this.numberSeries = numberSeries;
        this.type = type;
    }

}
