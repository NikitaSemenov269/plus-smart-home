package collectorEvent.sensor.event;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import collectorEvent.sensor.type.SensorEventType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class MotionSensorEvent extends SensorEvent {

    @NotNull
    private Integer linkQuality; // качество сигнала связи.
    @NotNull
    private boolean motion; // обнаружено ли движение.
    @NotNull
    private Integer voltage; // уровень напряжения.

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}

