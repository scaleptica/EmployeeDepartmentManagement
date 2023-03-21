package org.paytm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressDto {

  @JsonProperty(value = "id")
  private BigInteger id;

  @JsonProperty(value = "line1")
  private String line1;

  @JsonProperty(value = "line2")
  private String line2;

  @JsonProperty(value = "line3")
  private String line3;

  @JsonProperty(value = "city")
  private String city;

  @JsonProperty(value = "state")
  private String state;

  @JsonProperty(value = "country")
  private String country;

  @JsonProperty(value = "pin_code")
  private Integer pinCode;

  @JsonProperty(value = "address_type")
  private String addressType;
}
