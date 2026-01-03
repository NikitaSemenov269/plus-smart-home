package collectorEvent.mapper;

import collectorEvent.gRPC.telemetry.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class HubProtoMapToAvro {

    public HubEventAvro mapToAvro(HubEventProto proto) {

        Object hubPayload = switch (proto.getPayloadCase()) {
            case DEVICE_ADDED -> {
                DeviceAddedEventProto deviceAddedEvent = proto.getDeviceAdded();
                yield DeviceAddedEventAvro.newBuilder()
                        .setId(deviceAddedEvent.getId())
                        .setType(DeviceTypeAvro.valueOf(deviceAddedEvent.getType().name()))
                        .build();
            }
            case DEVICE_REMOVED -> {
                DeviceRemovedEventProto deviceRemovedEvent = proto.getDeviceRemoved();
                yield DeviceRemovedEventAvro.newBuilder().setId(deviceRemovedEvent.getId()).build();
            }
            case SCENARIO_ADDED -> {
                ScenarioAddedEventProto scenarioAddedEvent = proto.getScenarioAdded();

                List<DeviceActionAvro> deviceActionAvros = new ArrayList<>();
                List<ScenarioConditionAvro> scenarioConditionAvros = new ArrayList<>();

                for (DeviceActionProto deviceAction : scenarioAddedEvent.getActionList()) {
                    DeviceActionAvro.Builder deviceActionAvro = DeviceActionAvro.newBuilder()
                            .setType(ActionTypeAvro.valueOf(deviceAction.getType().name()))
                            .setSensorId(deviceAction.getSensorId());

                    if (deviceAction.hasValue()) {
                        deviceActionAvro.setValue(deviceAction.getValue());
                    } else {
                        deviceActionAvro.setValue(null);
                    }

                    deviceActionAvros.add(deviceActionAvro.build());
                }

                for (ScenarioConditionProto scenarioCondition : scenarioAddedEvent.getConditionList()) {
                    ScenarioConditionAvro.Builder builder = ScenarioConditionAvro.newBuilder()
                            .setType(ConditionTypeAvro.valueOf(scenarioCondition.getType().name()))
                            .setOperation(ConditionOperationAvro.valueOf(scenarioCondition.getOperation().name()))
                            .setSensorId(scenarioCondition.getSensorId());

                    switch (scenarioCondition.getValueCase()) {
                        case BOOL_VALUE -> builder.setValue(scenarioCondition.getBoolValue());
                        case INT_VALUE -> builder.setValue(scenarioCondition.getIntValue());
                        case VALUE_NOT_SET -> builder.setValue(null);
                    }

                    scenarioConditionAvros.add(builder.build());
                }

                yield ScenarioAddedEventAvro.newBuilder()
                        .setActions(deviceActionAvros)
                        .setConditions(scenarioConditionAvros)
                        .setName(scenarioAddedEvent.getName())
                        .build();
            }
            case SCENARIO_REMOVED -> {
                ScenarioRemovedEventProto scenarioRemovedEvent = proto.getScenarioRemoved();
                yield ScenarioRemovedEventAvro.newBuilder()
                        .setName(scenarioRemovedEvent.getName())
                        .build();
            }
            case PAYLOAD_NOT_SET ->
                    throw new IllegalStateException("Payload not set in HubEventProto");
            default -> throw new IllegalStateException("Unexpected value: " + proto.getPayloadCase());
        };

        HubEventPayload hubEventPayload = HubEventPayload.newBuilder()
                .setPayload(hubPayload)
                .build();

        Instant instant = convertTimestampToInstant(proto.getTimestamp());

        return HubEventAvro.newBuilder()
                .setHubId(proto.getHubId())
                .setTimestamp(instant)
                .setPayload(hubEventPayload)
                .build();
    }

    private Instant convertTimestampToInstant(com.google.protobuf.Timestamp timestamp) {
        return Instant.ofEpochSecond(
                timestamp.getSeconds(),
                timestamp.getNanos()
        );
    }
}