package at.htlkaindorf.messstationen_final.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class MeasuringStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "measuringStation", cascade = CascadeType.ALL)
    private List<Measure> measures = new ArrayList<>();

    @Override
    public String toString() {
        return "MeasuringStation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}'; // Keine measures hier einfügen!
    }
}
