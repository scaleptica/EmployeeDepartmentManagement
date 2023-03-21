package org.paytm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeDto {

  @JsonProperty(value = "id")
  private BigInteger id;

  //@NotNull
  @Pattern(regexp = "[a-zA-Z]*")  //check
  @JsonProperty(value = "first_name")
  private String firstName;

  @Pattern(regexp = "[a-zA-Z]*")
  @JsonProperty(value = "middle_name")
  private String middleName;

  //@NotNull
  @Pattern(regexp = "[a-zA-Z]*")
  @JsonProperty(value = "last_name")
  private String lastName;

  //@NotNull
  @JsonProperty(value = "dob")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate dob;   //Date works for sure, not sure about LocalDateTime

  //@NotNull
  @JsonProperty(value = "doj")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate doj;

  @JsonProperty(value = "remarks")
  private String remarks;

  //@NotNull
  //@Email  //email dependency added
  @JsonProperty(value = "email")
  private String email;

  //@NotNull
  @Pattern(regexp = "^[0-9]{10,11}$")
  //can contain any digit. The length can be from 10 to 11 characters (inclusive)
  @JsonProperty(value = "contact_number")
  private String contactNumber;

  @JsonProperty(value = "created_at")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime createdAt;

  @JsonProperty(value = "updated_at")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime updatedAt;

  @JsonProperty(value = "address_dto")
  private AddressDto addressDto;

  @JsonProperty(value = "department_id")
  private BigInteger departmentId;
}
