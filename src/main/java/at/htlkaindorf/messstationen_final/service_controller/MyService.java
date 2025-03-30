package at.htlkaindorf.messstationen_final.service_controller;

import at.htlkaindorf.messstationen_final.entities.Measure;
import at.htlkaindorf.messstationen_final.entities.MeasuringStation;
import at.htlkaindorf.messstationen_final.entities.MyUser;
import at.htlkaindorf.messstationen_final.entities.repos.MeasureRepository;
import at.htlkaindorf.messstationen_final.entities.repos.MeasuringStationRepository;
import at.htlkaindorf.messstationen_final.entities.repos.MyUserRepository;
import at.htlkaindorf.messstationen_final.jwt.JwtUtilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MyService {

    private final MyUserRepository myUserRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtilities jwtUtilities;
    private final MeasureRepository measureRepository;
    private final MeasuringStationRepository measuringStationRepository;

    public MyService(MyUserRepository myUserRepository, AuthenticationManager authenticationManager, JwtUtilities jwtUtilities, MeasureRepository measureRepository, MeasuringStationRepository measuringStationRepository) {
        this.myUserRepository = myUserRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtilities = jwtUtilities;
        this.measureRepository = measureRepository;
        this.measuringStationRepository = measuringStationRepository;
    }

    public String signin(String username, String password){
        MyUser myUser = myUserRepository.findMyUserByUsername(username);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        String token = jwtUtilities.generateToken(username);

        return token;
    }

    public Integer getNumberOfRecords() {
        Integer numRecords = measureRepository.findAll().size();

        return  numRecords;
    }

    public String getNameOfStations() {
        StringBuilder builder = new StringBuilder();
        List<MeasuringStation> stations = measuringStationRepository.findAll();

        stations.forEach(s -> {
            if(builder.length() > 0){
                builder.append(", ");
            }

            builder.append(s.getName());
        });

        return builder.toString();
    }

    public String getMaxTempOfStations() {
        //maximale Temperatur suchen
        // alle messungen die -3% vom max sind, und die +3% vom max sind
        //dann nach Stationen eingeben
        StringBuilder builder = new StringBuilder();

        List<MeasuringStation> stations = measuringStationRepository.findAll();

        Map<String, List<Measure>> measureMap = new HashMap<>();

        stations.forEach(station -> {
            List<Measure> measuresOfStation = measureRepository.findMeasuresByName(station.getName());
            Measure maxMeasure = measuresOfStation.get(0);

            for (int i = 0; i < measuresOfStation.size(); i++) {
                if(measuresOfStation.get(i).getTmax() > maxMeasure.getTmax()){
                    maxMeasure = measuresOfStation.get(i);
                }
            }

            double maxTemp = maxMeasure.getTmax();

            double percentOfMax = maxTemp * (0.03);
            double minusPercent = maxTemp - percentOfMax;
            double plusPercent = maxTemp + percentOfMax;

            measuresOfStation.forEach(m -> {
                if(m.getTmax() >= minusPercent && m.getTmax() <= plusPercent){
                    measureMap.putIfAbsent(station.getName(), new ArrayList<>());
                    measureMap.get(station.getName()).add(m);
                }
            });
        });

        measureMap.forEach((stationName, MeasureL) -> {
            builder.append(stationName).append(" (").append(MeasureL.size()).append(" )");
            builder.append("\n");

            MeasureL.forEach(m -> builder.append(m + "\n"));
            builder.append("\n");
        });

        return builder.toString();
    }
}
