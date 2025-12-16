package controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/events/")
@RequiredArgsConstructor
public class Controller {
/*
    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        log.info("222");
        ResponseEntity.ok().build();
    }

    @PostMapping("/hubs")
    public ResponseEntity<?> collectHubEvent() {
        log.info("111");
        return ResponseEntity.ok("");
    }*/
}
