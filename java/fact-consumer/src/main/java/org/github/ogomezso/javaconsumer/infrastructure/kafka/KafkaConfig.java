package org.github.ogomezso.javaconsumer.infrastructure.kafka;

import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.github.ogomezso.javaconsumer.config.AppConfig;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KafkaConfig {

  public static final String DESERIALIZATION_STRING_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
  public static final String DESERIALIZATION_LONG_DESERIALIZER = "org.apache.kafka.common.serialization.LongDeserializer";

  static KafkaConsumer<String, String> createChuckKafkaConsumer(AppConfig appConfig) {

    Properties props = new Properties();
    try (Reader reader = Files.newBufferedReader(Paths.get("connection.properties"))) {
      props.load(reader);
    } catch (Exception e) {
      System.err.printf("Exception reading properties file %s%n", e.getMessage());
      e.printStackTrace(System.err);
      System.exit(1);
    }
    //props.put(BOOTSTRAP_SERVERS_CONFIG, appConfig.getBootstrapServers());
    props.put(CLIENT_ID_CONFIG, appConfig.getChuckClientId());
    props.put(GROUP_ID_CONFIG, appConfig.getChuckGroupId());
    props.put(AUTO_OFFSET_RESET_CONFIG, appConfig.getAutoOffsetReset());
    props.put(KEY_DESERIALIZER_CLASS_CONFIG, DESERIALIZATION_STRING_DESERIALIZER);
    props.put(VALUE_DESERIALIZER_CLASS_CONFIG, DESERIALIZATION_STRING_DESERIALIZER);

    final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(List.of(appConfig.getChuckTopic()));

    return consumer;
  }
}
