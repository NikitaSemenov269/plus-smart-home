package collectorEvent.sensor.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import collectorEvent.sensor.type.SensorEventType;

import java.time.Instant;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = SensorEventType.class
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = LightSensor.class, name = "LIGHT_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = ClimateSensor.class, name = "CLIMATE_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = MotionSensor.class, name = "MOTION_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = SwitchSensor.class, name = "SWITCH_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = TemperatureSensor.class, name = "TEMPERATURE_SENSOR_EVENT")
})

@Getter
@Setter
@ToString(callSuper = true)
public abstract class Sensor {

    @NotBlank(message = "Поле id не может быть пустым")
    private String id;

    @NotBlank(message = "Поле hubId не может быть пустым")
    private String hubId;

    private Instant timestamp = Instant.now();

    public abstract SensorEventType getType();
}
