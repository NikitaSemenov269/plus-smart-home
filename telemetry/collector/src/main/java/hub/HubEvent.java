package hub;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import hub.event.DeviceAddedEvent;
import hub.event.DeviceRemovedEvent;
import hub.event.ScenarioAddedEvent;
import hub.event.ScenarioRemovedEvent;
import hub.type.HubEventType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = HubEvent.class
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceAddedEvent.class, name = "DEVICE_ADDED"),
        @JsonSubTypes.Type(value = DeviceRemovedEvent.class, name = "DEVICE_REMOVED"),
        @JsonSubTypes.Type(value = ScenarioAddedEvent.class, name = "SCENARIO_ADDED"),
        @JsonSubTypes.Type(value = ScenarioRemovedEvent.class, name = "SCENARIO_REMOVED")
})

@Getter
@Setter
@ToString(callSuper = true)
public abstract class HubEvent {

    @NotBlank(message = "Поле hubId не может быть пустым")
    private String hubId;

    private Instant timestamp = Instant.now();

    public abstract HubEventType getType();
}
