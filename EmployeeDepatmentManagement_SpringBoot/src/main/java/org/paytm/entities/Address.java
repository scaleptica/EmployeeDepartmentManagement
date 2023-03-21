package org.paytm.entities;

import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "address")
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private BigInteger id;  //doubt    //should be in constructor
  @Column(name = "line1", nullable = false)
  private String line1;
  @Column(name = "line2")
  private String line2;
  @Column(name = "line3")
  private String line3;
  @Column(name = "city", nullable = false)
  private String city;
  @Column(name = "state", nullable = false)
  private String state;
  @Column(name = "country", nullable = false)
  private String country;
  @Column(name = "pin_code", nullable = false)
  private Integer pinCode;
  @Column(name = "address_type", columnDefinition = "VARCHAR(6)")
  private String addressType;
  //address_type  varchar(6)  OFFICE/HOME/OTHER;  //@pattern, @matchers in dtoclass
}