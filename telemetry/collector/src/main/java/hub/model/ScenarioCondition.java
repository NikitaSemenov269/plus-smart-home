package hub.model;

import hub.type.ConditionOperation;
import hub.type.ConditionType;
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
