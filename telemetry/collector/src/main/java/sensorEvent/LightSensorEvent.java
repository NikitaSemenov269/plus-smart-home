package sensorEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sensorEvent.abstractClass.SensorEvent;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LightSensorEvent extends SensorEvent {
    private int linkQuality;
    private int luminosity;
}
