package org.paytm.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.paytm.dto.DepartmentDto;
import org.paytm.mapper.DepartmentMapper;
import org.paytm.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class TopicKafkaConsumer {


   @Autowired
   private ObjectMapper objectMapper;

   @Autowired
   private DepartmentService departmentService;

   @Autowired
   private DepartmentMapper departmentMapper;

   /**
    * It listens to the kafka topic, and when a orderIdInfo is received, it calls the
    * TopicService.processMessage() function
    *
    * @param message The OrderIdInfo object string that is received from the Kafka topic.
    */
   @KafkaListener(
           id = "${kafka.consumer.topic}",
           topics = "${kafka.consumer.topic}",
           groupId = "${kafka.consumer.consumer_group}",
           containerFactory = "concurrentKafkaListenerContainerFactory")
   public void message(String message) {
       try {
           DepartmentDto object = this.objectMapper.readValue(message, DepartmentDto.class);

           if (object == null ) {
               log.warn(
                       "TopicKafkaConsumer::message : invalid request  {}",
                       message);
               return;
           }

           // call your code of creating department
           object.setCreatedAt(LocalDateTime.now());
           object.setUpdatedAt(LocalDateTime.now());
           this.departmentService.addDepartment(this.departmentMapper.fromDtoToEntity(object));


       } catch (Exception ex) {
           log.error(
                   "TopicKafkaConsumer::message  request {} \n\r exception", message, ex);
           throw new RuntimeException(ex);
       }
   }
}