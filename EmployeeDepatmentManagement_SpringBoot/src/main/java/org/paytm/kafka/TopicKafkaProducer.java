package org.paytm.kafka;

import lombok.extern.slf4j.Slf4j;
import org.paytm.dto.DepartmentCsvDto;
import org.paytm.dto.DepartmentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class TopicKafkaProducer {

   @Autowired
   @Qualifier("dealKafkaTopicKafkaTemplate")
   private KafkaTemplate<String, DepartmentCsvDto> kafkaTemplate;

   @Value("${kafka.consumer.topic}")
   private String kafkaTopic;

   public void departmentCsvDtoRead(DepartmentCsvDto departmentCsvDto) throws Exception {
       log.info("KafkaProducer:departmentCsvDto  => {}", departmentCsvDto);
       if (Objects.isNull(departmentCsvDto)) {
           throw new IllegalArgumentException("Can not publish null departmentCsvDto");
       }
       this.kafkaTemplate.send(this.kafkaTopic, departmentCsvDto);
   }
}