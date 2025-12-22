package collectorEvent.sensor.event;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import collectorEvent.sensor.type.SensorEventType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class TemperatureSensorEvent extends SensorEvent {
    @NotNull
    private Integer temperatureC; // температура в градусах Цельсия.
    @NotNull
    private Integer temperatureF; // температура в градусах Фаренгейта.

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
