package sensor.event;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import sensor.type.SensorEventType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class SwitchSensorEvent extends SensorEvent {

    @NotNull
    private boolean state; // текущее состояние переключателя (включено/выключено).

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
