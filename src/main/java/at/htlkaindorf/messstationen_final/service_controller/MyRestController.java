package at.htlkaindorf.messstationen_final.service_controller;

import at.htlkaindorf.messstationen_final.entities.MyUser;
import at.htlkaindorf.messstationen_final.jwt.JwtAuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class MyRestController {

    private MyService myService;

    public MyRestController(MyService myService) {
        this.myService = myService;
    }

    @PostMapping("public/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody MyUser myUser) {
        String jwt = myService.signin(myUser.getUsername(), myUser.getPassword());

        JwtAuthenticationResponse jwtResponse = new JwtAuthenticationResponse();
        jwtResponse.setToken(jwt);

        return ResponseEntity.ok(jwtResponse);
    }

    @GetMapping("user/getNumberOfRecords")
    public ResponseEntity<Integer> getTotalNumOfRecords() {
        Integer numOfRecords = myService.getNumberOfRecords();

        return ResponseEntity.ok(numOfRecords);
    }

    @GetMapping("admin/getNameOfMeasuringStations")
    public ResponseEntity<String> getNameOfStations() {
        String nameOfStations = myService.getNameOfStations();

        return ResponseEntity.ok(nameOfStations);
    }

    @GetMapping("admin/getMaxTemp")
    public ResponseEntity<String> getMaxTemps() {
        String max = myService.getMaxTempOfStations();

        return ResponseEntity.ok(max);
    }
}
