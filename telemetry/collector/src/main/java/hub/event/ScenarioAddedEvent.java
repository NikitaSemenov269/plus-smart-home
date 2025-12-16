package hub.event;

import hub.HubEvent;
import hub.model.DeviceAction;
import hub.model.ScenarioCondition;
import hub.type.HubEventType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioAddedEvent extends HubEvent {

    @NotNull
    @Length(min = 3)
    private String name;
    @NotNull
    private List<ScenarioCondition> conditions;
    @NotNull
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
