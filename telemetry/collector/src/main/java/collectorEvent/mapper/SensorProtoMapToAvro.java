package collectorEvent.mapper;

import collectorEvent.gRPC.telemetry.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;

@Slf4j
@Component
public class SensorProtoMapToAvro {

    public SensorEventAvro mapToAvro(SensorEventProto proto) {
        Object sensorPayload = switch (proto.getPayloadCase()) {
            case TEMPERATURE_SENSOR_EVENT -> {
                TemperatureSensorProto tempEvent = proto.getTemperatureSensorEvent();
                yield TemperatureSensorAvro.newBuilder()
                        .setTemperatureF(tempEvent.getTemperatureF())
                        .setTemperatureC(tempEvent.getTemperatureC())
                        .build();
            }
            case LIGHT_SENSOR_EVENT -> {
                LightSensorProto light = proto.getLightSensorEvent();
                yield LightSensorAvro.newBuilder()
                        .setLinkQuality(light.getLinkQuality())
                        .setLuminosity(light.getLuminosity())
                        .build();
            }
            case CLIMATE_SENSOR_EVENT -> {
                ClimateSensorProto climate = proto.getClimateSensorEvent();
                yield ClimateSensorAvro.newBuilder()
                        .setHumidity(climate.getHumidity())
                        .setCo2Level(climate.getCo2Level())
                        .setTemperatureC(climate.getTemperatureC())
                        .build();
            }
            case MOTION_SENSOR_EVENT -> {
                MotionSensorProto motion = proto.getMotionSensorEvent();
                yield MotionSensorAvro.newBuilder()
                        .setLinkQuality(motion.getLinkQuality())
                        .setMotion(motion.getMotion())
                        .setVoltage(motion.getVoltage())
                        .build();
            }
            case SWITCH_SENSOR_EVENT -> {
                SwitchSensorProto switchSensor = proto.getSwitchSensorEvent();
                yield SwitchSensorAvro.newBuilder()
                        .setState(switchSensor.getState())
                        .build();
            }
            default -> throw new IllegalStateException("Unexpected value: " + proto.getPayloadCase());
        };

        SensorEventPayload eventPayload = SensorEventPayload.newBuilder()
                .setPayload(sensorPayload)
                .build();

        Instant instant = convertTimestampToInstant(proto.getTimestamp());

        return SensorEventAvro.newBuilder()
                .setId(proto.getId())
                .setHubId(proto.getHubId())
                .setTimestamp(instant)
                .setPayload(eventPayload)
                .build();
    }

    private Instant convertTimestampToInstant(com.google.protobuf.Timestamp timestamp) {
        return Instant.ofEpochSecond(
                timestamp.getSeconds(),
                timestamp.getNanos()
        );
    }
}
