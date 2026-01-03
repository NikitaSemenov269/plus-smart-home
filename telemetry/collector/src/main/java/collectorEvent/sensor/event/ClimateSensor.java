package collectorEvent.sensor.event;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import collectorEvent.sensor.type.SensorEventType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ClimateSensor extends Sensor {

    @NotNull
    private Integer temperatureC; // Цельсия
    @NotNull
    private Integer humidity;
    @NotNull
    private Integer co2Level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
