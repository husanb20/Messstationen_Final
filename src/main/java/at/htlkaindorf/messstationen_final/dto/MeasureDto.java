package at.htlkaindorf.messstationen_final.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MeasureDto {
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private float tmin;
    private float tmax;
    private double precipitation;
}
