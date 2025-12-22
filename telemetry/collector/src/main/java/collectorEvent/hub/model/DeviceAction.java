package collectorEvent.hub.model;

import collectorEvent.hub.type.ActionType;
import lombok.Getter;
import lombok.Setter;

/**
 * Представляет действие, которое должно быть выполнено устройством.
 */
@Getter
@Setter
public class DeviceAction {
    private String sensorId;
    private ActionType type;
    private Integer value;
}
