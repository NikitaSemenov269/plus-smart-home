import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.VoidSerializer;
import service.kafka.SensorEventSerializer;

import java.util.Properties;

public class KafkaAvroProducer {
    public static void main(String[] args) {
        Properties config = new Properties();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, VoidSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SensorEventSerializer.class);

        String topic = "telemetry.sensors.v1";
    }
}
