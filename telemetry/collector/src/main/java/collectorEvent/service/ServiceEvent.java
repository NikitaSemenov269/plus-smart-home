package collectorEvent.service;

import collectorEvent.gRPC.telemetry.event.HubEventProto;
import collectorEvent.gRPC.telemetry.event.SensorEventProto;
import collectorEvent.mapper.HubProtoMapToAvro;
import collectorEvent.mapper.SensorProtoMapToAvro;
import collectorEvent.sensor.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.*;
import collectorEvent.kafka.KafkaAvroProducer;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceEvent {
    private final KafkaAvroProducer kafkaProducer;
    private final HubProtoMapToAvro hubProtoMapToAvro;
    private final SensorProtoMapToAvro sensorProtoMapToAvro;

    public void processSensorEvent(SensorEventProto event) {
        SensorEventAvro avroEvent = sensorProtoMapToAvro.mapToAvro(event);
        kafkaProducer.sendSensorEvent(avroEvent);
    }

    public void processHubEvent(HubEventProto event) {
        HubEventAvro avroEvent = hubProtoMapToAvro.mapToAvro(event);
        kafkaProducer.sendHubEvent(avroEvent);
    }
}


