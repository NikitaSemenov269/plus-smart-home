package controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sensorEvent.abstractClass.SensorEvent;

@Slf4j
@RestController
@RequestMapping("/events/")
@RequiredArgsConstructor
public class Controller {

    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        log.info("");
        ResponseEntity.ok().build();
    }

    @PostMapping("/hubs")
    public ResponseEntity<?> collectHubEvent() {
        log.info("");
        return ResponseEntity.ok("");
    }
}
