package com.prgrms.rg.config;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public class KafkaFacade {

  private static final String TOPIC;

  static {
    var profile = System.getProperty("spring.profiles.active");
    boolean isProductionMode = profile == null || !profile.equals("prod");
    TOPIC = isProductionMode ? "test" : "RG_SERVER";
  }

  private final KafkaProducer<String, String> producer;
  private final Callback transmissionCallback = ((metadata, exception) -> {
    if (exception != null) {
      log.warn(exception.getMessage(), exception);
    }
  });

  private KafkaFacade() {
    var yaml = new YamlMapFactoryBean();
    yaml.setResources(new ClassPathResource("application-security.yml"));
    var kafkaMetadata = (Map<String, ?>) (yaml.getObject().get("kafka"));

    var config = new Properties();
    config.put("bootstrap.servers", kafkaMetadata.get("broker-ip"));
    config.put("key.serializer", StringSerializer.class.getName());
    config.put("value.serializer", StringSerializer.class.getName());
    producer = new KafkaProducer<>(config);

  }

  public static KafkaFacade getInstance() {
    return Singleton.SINGLETON_INSTANCE;
  }

  public Future<RecordMetadata> send(String message) {
    return producer.send(new ProducerRecord<>(TOPIC, message), transmissionCallback);

  }

  public boolean isServiceWorking() {
    try {
      producer.send(new ProducerRecord<>("bootstrap", "bootstrap")).get();
    } catch (InterruptedException | ExecutionException exception) {
      log.warn(exception.getMessage(), exception);
      return false;
    }
    return true;
  }

  private static class Singleton {

    private static final KafkaFacade SINGLETON_INSTANCE = new KafkaFacade();
  }
}
