package at.htlkaindorf.messstationen_final.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Measure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private float tmin;
    private float tmax;
    private double precipitation;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "measuringStation_id")
    private MeasuringStation measuringStation;


    @Override
    public String toString() {
        return String.format("Data { %s, %s, %.1f, %.1f, %.1f }",
                measuringStation != null ? measuringStation.getName() : "null",
                date,
                tmin,
                tmax,
                precipitation);
    }
}
