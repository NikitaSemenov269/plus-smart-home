package sensorEvent.abstractClass;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sensor.event.*;
import sensorEvent.*;
import sensorEventClass.*;

import java.time.Instant;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = DefaultSensorEvent.class
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = LightSensorEvent.class, name = "LIGHT_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = ClimateSensorEvent.class, name = "CLIMATE_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = MotionSensorEvent.class, name = "MOTION_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = SwitchSensorEvent.class, name = "SWITCH_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = TemperatureSensorEvent.class, name = "TEMPERATURE_SENSOR_EVENT")
})

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class SensorEvent {

    @NotBlank(message = "Поле id не может быть пустым")
    private String id;

    @NotBlank(message = "Поле hubId не может быть пустым")
    private String hubId;

    private Instant timestamp = Instant.now();

    private SensorEventType type;

    @NotNull(message = "Тип события не может быть = null")
    public SensorEventType getType() {
        return this.type;
    }
}
