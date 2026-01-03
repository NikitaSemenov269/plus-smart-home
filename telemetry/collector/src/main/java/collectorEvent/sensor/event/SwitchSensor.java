package collectorEvent.sensor.event;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import collectorEvent.sensor.type.SensorEventType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class SwitchSensor extends Sensor {

    @NotNull
    private boolean state; // текущее состояние переключателя (включено/выключено).

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
