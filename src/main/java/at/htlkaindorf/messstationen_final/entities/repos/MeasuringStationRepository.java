package at.htlkaindorf.messstationen_final.entities.repos;

import at.htlkaindorf.messstationen_final.entities.MeasuringStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasuringStationRepository extends JpaRepository<MeasuringStation, Integer> {
    MeasuringStation findMeasuringStationByName(String name);
}
