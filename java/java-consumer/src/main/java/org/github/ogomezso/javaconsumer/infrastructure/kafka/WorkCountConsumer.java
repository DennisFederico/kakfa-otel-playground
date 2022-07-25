package org.github.ogomezso.javaconsumer.infrastructure.kafka;

import java.time.Duration;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.github.ogomezso.javaconsumer.config.AppConfig;

public class WorkCountConsumer {

  private final KafkaConsumer<String, Long> workCountConsumer;

  public WorkCountConsumer(AppConfig appConfig) {
    this.workCountConsumer = KafkaConfig.createWordCountConsumer(appConfig);
  }

  public void pollMessage() {
    while (true) {
      final ConsumerRecords<String, Long> consumerRecords = workCountConsumer.poll(Duration.ofMillis(500));

      consumerRecords.forEach(record -> {
        System.out.printf("Consumer Record:(%d, %s, %d, %d)\n",
            record.key(), record.value(),
            record.partition(), record.offset());
      });
    }
  }

}
