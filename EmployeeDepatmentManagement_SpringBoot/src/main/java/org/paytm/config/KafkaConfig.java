package org.paytm.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.paytm.dto.DepartmentCsvDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaConfig {

    @Value("${kafka.endpoint}")
    private String dealKafkaEndpoint;

    @Value("${kafka.consumer.consumer_group}")
    private String consumerGroupName;

    @Value("${kafka.max_poll_records}")
    private Integer maxPollRecords;

    @Value("${kafka.concurrency}")
    private Integer concurrency;

    @Value("${kafka.time_interval}")
    private Integer timeInterval;

    @Value("${kafka.max_attempts}")
    private Integer maxAttempts;

    @Value("${kafka.offset_reset}")
    private String offset_reset;

    @Value("${kafka.producer.acks}")
    private String acks;

    @Value("${kafka.producer.request_timeout_ms}")
    private String request_timeout_ms;

    private Map<String, Object> consumerConfigs() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.dealKafkaEndpoint);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, this.consumerGroupName);
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, this.offset_reset);
        configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, this.maxPollRecords);
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return configProps;
    }

    /**
     * This function creates a consumer factory that will be used to create a consumer that will be
     * used to consume messages from the success topic
     *
     * @return A ConsumerFactory<String, String>
     */
    private ConsumerFactory<String, String> consumerFactory() {

        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new StringDeserializer());
    }

    /**
     * This function is used to configure the producer factory
     *
     * @return A map of configuration properties for the producer factory.
     */
    @Bean
    public Map<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.dealKafkaEndpoint);
        configProps.put(ProducerConfig.ACKS_CONFIG, this.acks);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, this.request_timeout_ms);

        return configProps;
    }


    /**
     * This function creates a KafkaTemplate for the dealKafkaTopic
     *
     * @return A KafkaTemplate for the DepartmentCsvDto
     */
    @Bean
    @Qualifier("dealKafkaTopicKafkaTemplate")
    public KafkaTemplate<String, DepartmentCsvDto> dealKafkaTopicKafkaTemplate() {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerFactory()));
    }

    /**
     * The kafka default error handler is used to handle errors that occur during the processing of a
     * record
     *
     * @return A DefaultErrorHandler object
     */
    @Bean
    public DefaultErrorHandler defaultErrorhandler() {
        return new DefaultErrorHandler(
                (rec, ex) ->
                        log.warn(
                                "KafkaConfig::defaultErrorhandler Recovered {} "
                                        + "\n\rDefaultErrorHandler Exception: ",
                                rec,
                                ex),
                new FixedBackOff(this.timeInterval, this.maxAttempts));
    }

    /**
     * This function creates a Kafka listener container factory that is used to create Kafka listener
     * containers
     *
     * @return A ConcurrentKafkaListenerContainerFactory
     */
    @Bean
    @Qualifier("concurrentKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String>
    concurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String>
                concurrentKafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        concurrentKafkaListenerContainerFactory.setConsumerFactory(this.consumerFactory());
        concurrentKafkaListenerContainerFactory.setConcurrency(this.concurrency);
        concurrentKafkaListenerContainerFactory.setBatchListener(true);
        concurrentKafkaListenerContainerFactory.setCommonErrorHandler(defaultErrorhandler());

        return concurrentKafkaListenerContainerFactory;
    }
}