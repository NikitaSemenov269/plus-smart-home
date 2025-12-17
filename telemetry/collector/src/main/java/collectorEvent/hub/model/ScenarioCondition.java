package collectorEvent.hub.model;

import collectorEvent.hub.type.ConditionOperation;
import collectorEvent.hub.type.ConditionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScenarioCondition {

    private String sensorId;
    private ConditionType type;
    private ConditionOperation operation;
    private Integer value;
}
