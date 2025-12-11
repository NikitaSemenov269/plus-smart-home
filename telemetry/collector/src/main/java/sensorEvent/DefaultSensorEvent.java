package sensorEvent;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultSensorEvent {
    @NotBlank(message = "Поле id не может быть пустым")
    private String id;

    @NotBlank(message = "Поле hubId не может быть пустым")
    private String hubId;

    private Instant timestamp = Instant.now();

    private String type;
}
