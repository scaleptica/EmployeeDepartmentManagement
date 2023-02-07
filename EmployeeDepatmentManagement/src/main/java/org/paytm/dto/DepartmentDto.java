package org.paytm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.bridge.IMessage;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepartmentDto {

    @JsonProperty(value = "id")
    private BigInteger id;

    //@NotNull
    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "remarks")
    private String remarks;

    @JsonProperty(value = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
    private LocalDateTime createdAt;    //jsonFormat might not work with LocalDateTime, refer to-
                                        // https://stackoverflow.com/questions/29956175/json-java-8-localdatetime-format-in-spring-boot

    @JsonProperty(value = "update_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
    private LocalDateTime updatedAt;

    //do I need to do something for employees
}
