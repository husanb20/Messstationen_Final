package at.htlkaindorf.messstationen_final.db;

import at.htlkaindorf.messstationen_final.dto.MeasureDto;
import at.htlkaindorf.messstationen_final.entities.Measure;
import at.htlkaindorf.messstationen_final.entities.MeasuringStation;
import at.htlkaindorf.messstationen_final.entities.MyUser;
import at.htlkaindorf.messstationen_final.entities.repos.MeasureRepository;
import at.htlkaindorf.messstationen_final.entities.repos.MeasuringStationRepository;
import at.htlkaindorf.messstationen_final.entities.repos.MyUserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class DBInit implements ApplicationRunner {
    @Value("classpath:student.json")
    private Resource studentData;

    @Value("classpath:allData.json")
    private Resource allData;

    private final ObjectMapper om;
    private final MeasuringStationRepository measuringStationRepository;
    private final MyUserRepository myUserRepository;
    private final MeasureRepository measureRepository;
    private final PasswordEncoder passwordEncoder;

    public DBInit(ObjectMapper om, MeasuringStationRepository measuringStationRepository, MyUserRepository myUserRepository, MeasureRepository measureRepository, PasswordEncoder passwordEncoder) {
        this.om = om;
        this.measuringStationRepository = measuringStationRepository;
        this.myUserRepository = myUserRepository;
        this.measureRepository = measureRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<MeasuringStation> measuringStationsList =
                om.readerForListOf(MeasuringStation.class)
                        .readValue(allData.getFile());

        Set<MeasuringStation> measuringStationsSet = new HashSet<>();

        measuringStationsList.forEach(ms -> {
            measuringStationsSet.add(ms);
        });

        measuringStationRepository.saveAll(measuringStationsSet);

        List<MeasureDto> measureDtoList = om.readerForListOf(MeasureDto.class)
                .readValue(allData.getFile());

        List<Measure> measuresList = new ArrayList<>();

        measureDtoList.forEach(mDto -> {
            Measure m = new Measure();
            m.setName(mDto.getName());
            m.setDate(mDto.getDate());
            m.setTmin(mDto.getTmin());
            m.setTmax(mDto.getTmax());
            m.setPrecipitation(mDto.getPrecipitation());

            String stationName = mDto.getName();
            MeasuringStation msStation = measuringStationRepository.findMeasuringStationByName(stationName);
            m.setMeasuringStation(msStation);

            measuresList.add(m);
        });

        measureRepository.saveAll(measuresList);

        /*List<MyUser> myUserList = om.readerForListOf(MyUser.class)
                .readValue(studentData.getFile());*/
        JsonNode rootNode = om.readTree(studentData.getFile());
        List<MyUser> myUserList = om.convertValue(rootNode.get("USERS"), new TypeReference<List<MyUser>>() {});

        //!!
        myUserList.forEach(u -> {
            String pwString = String.valueOf(u.getPassword());
            u.setPassword(passwordEncoder.encode(pwString));
        });

        myUserRepository.saveAll(myUserList);
    }
}
