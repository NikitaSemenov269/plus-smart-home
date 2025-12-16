package service;

import hub.HubEvent;
import hub.event.DeviceAddedEvent;
import hub.event.DeviceRemovedEvent;
import hub.event.ScenarioAddedEvent;
import hub.event.ScenarioRemovedEvent;
import hub.model.DeviceAction;
import hub.model.ScenarioCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.*;
import sensor.event.*;
import service.kafka.HubEventSerializer;
import service.kafka.SensorEventSerializer;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceEvent {
    private final SensorEventSerializer serializerEvent;
    private final HubEventSerializer serializerHub;


    public void serializeEvent(SensorEvent event) {
        final String topic = "telemetry.sensors.v1";

        Object sensorPayload = switch (event.getType()) {
            case TEMPERATURE_SENSOR_EVENT -> {
                TemperatureSensorEvent tempEvent = (TemperatureSensorEvent) event;
                yield TemperatureSensorAvro.newBuilder()
                        .setTemperatureF(tempEvent.getTemperatureF())
                        .setTemperatureC(tempEvent.getTemperatureC())
                        .build();
            }
            case LIGHT_SENSOR_EVENT -> {
                LightSensorEvent light = (LightSensorEvent) event;
                yield LightSensorAvro.newBuilder()
                        .setLinkQuality(light.getLinkQuality())
                        .setLuminosity(light.getLuminosity())
                        .build();
            }
            case CLIMATE_SENSOR_EVENT -> {
                ClimateSensorEvent climate = (ClimateSensorEvent) event;
                yield ClimateSensorAvro.newBuilder()
                        .setHumidity(climate.getHumidity())
                        .setCo2Level(climate.getCo2Level())
                        .setTemperatureC(climate.getTemperatureC())
                        .build();
            }
            case MOTION_SENSOR_EVENT -> {
                MotionSensorEvent motion = (MotionSensorEvent) event;
                yield MotionSensorAvro.newBuilder()
                        .setLinkQuality(motion.getLinkQuality())
                        .setMotion(motion.isMotion())
                        .setVoltage(motion.getVoltage())
                        .build();
            }
            case SWITCH_SENSOR_EVENT -> {
                SwitchSensorEvent switchSensor = (SwitchSensorEvent) event;
                yield SwitchSensorAvro.newBuilder()
                        .setState(switchSensor.isState())
                        .build();
            }
        };

        SensorEventPayload eventPayload = SensorEventPayload.newBuilder()
                .setPayload(sensorPayload)
                .build();

        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(eventPayload)
                .build();

        serializerEvent.serialize(topic, sensorEventAvro);
    }

    public void serializeHub(HubEvent event) {
        final String topic = "telemetry.hubs.v1";

        Object hubPayload = switch (event.getType()) {
            case DEVICE_ADDED -> {
                DeviceAddedEvent deviceAddedEvent = (DeviceAddedEvent) event;
                yield DeviceAddedEventAvro.newBuilder()
                        .setId(deviceAddedEvent.getId())
                        .setType(DeviceTypeAvro.valueOf(deviceAddedEvent.getDeviceType().name()))
                        .build();
            }
            case DEVICE_REMOVED -> {
                DeviceRemovedEvent deviceRemovedEvent = (DeviceRemovedEvent) event;
                yield DeviceRemovedEventAvro.newBuilder().setId(deviceRemovedEvent.getId()).build();
            }
            case SCENARIO_ADDED -> {
                ScenarioAddedEvent scenarioAddedEvent = (ScenarioAddedEvent) event;

                List<DeviceActionAvro> deviceActionAvros = new ArrayList<>();
                List<ScenarioConditionAvro> scenarioConditionAvros = new ArrayList<>();

                for (DeviceAction deviceAction : scenarioAddedEvent.getActions()) {
                    DeviceActionAvro deviceActionAvro = DeviceActionAvro.newBuilder()
                            .setType(ActionTypeAvro.valueOf(deviceAction.getType().name()))
                            .setSensorId(deviceAction.getSensorId())
                            .setValue(deviceAction.getValue())
                            .build();
                    deviceActionAvros.add(deviceActionAvro);
                }

                for (ScenarioCondition scenarioCondition : scenarioAddedEvent.getConditions()) {
                    ScenarioConditionAvro scenarioConditionAvro = ScenarioConditionAvro.newBuilder()
                            .setType(ConditionTypeAvro.valueOf(scenarioCondition.getType().name()))
                            .setOperation(ConditionOperationAvro.valueOf(scenarioCondition.getOperation().name()))
                            .setSensorId(scenarioCondition.getSensorId())
                            .setValue(scenarioCondition.getValue())
                            .build();
                    scenarioConditionAvros.add(scenarioConditionAvro);
                }

                yield ScenarioAddedEventAvro.newBuilder()
                        .setActions(deviceActionAvros)
                        .setConditions(scenarioConditionAvros)
                        .setName(scenarioAddedEvent.getName())
                        .build();
            }
            case SCENARIO_REMOVED -> {
                ScenarioRemovedEvent scenarioRemovedEvent = (ScenarioRemovedEvent) event;
                yield ScenarioRemovedEventAvro.newBuilder().setName(scenarioRemovedEvent.getName()).build();
            }
        };

        HubEventPayload hubEventPayload = HubEventPayload.newBuilder()
                .setPayload(hubPayload)
                .build();

        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(hubEventPayload)
                .build();

        serializerHub.serialize(topic, hubEventAvro);
    }
}


