package com.rhotiz.container.demo.schemaregistry;

import com.rhotiz.avro.MyMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConditionalOnProperty(value = "schema-registry.interaction.config.enabled", havingValue = "true")
public class KafkaWithSchemaRegistryProducerConfig {

    private String schemaRegistryUrl = "http://localhost:8081";
    private String bootstrapServers = "localhost:9092";

    @Bean
    public ProducerFactory<String, MyMessage> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer.class); // Avro serializer
        configProps.put("schema.registry.url", schemaRegistryUrl); // Schema Registry URL
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, MyMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
