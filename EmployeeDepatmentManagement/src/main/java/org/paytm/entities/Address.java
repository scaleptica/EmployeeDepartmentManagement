package org.paytm.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private BigInteger id;  //doubt    //should be in constructor
    @Column(name = "line1", columnDefinition = "NOT NULL")
    private String line1;
    @Column(name = "line2")
    private String line2;
    @Column(name = "line3")
    private String line3;
    @Column(name = "city", columnDefinition = "NOT NULL")
    private String city;
    @Column(name = "state", columnDefinition = "NOT NULL")
    private String state;
    @Column(name = "country", columnDefinition = "NOT NULL")
    private String country;
    @Column(name = "pin_code", columnDefinition = "NOT NULL")
    private Integer pinCode;
    @Column(name = "address_type", columnDefinition = "VARCHAR(6)")
    private String addressType;
    //address_type  varchar(6)  OFFICE/HOME/OTHER;  //@pattern, @matchers in dtoclass
}