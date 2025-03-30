package at.htlkaindorf.messstationen_final.entities.repos;

import at.htlkaindorf.messstationen_final.entities.Measure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeasureRepository extends JpaRepository<Measure, Integer> {
    List<Measure> findMeasuresByName(String name);
}
