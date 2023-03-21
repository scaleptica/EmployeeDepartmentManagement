package org.paytm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EmployeeCsvDto implements Serializable{

    private static final long serialVersionUID = 2L;

    @CsvBindByName(column = "id")
    private BigInteger id;

    @CsvBindByName(column = "firstName", required = true)
    private String firstName;

    @CsvBindByName(column = "middleName", required = true)
    private String middleName;

    @CsvBindByName(column = "lastName", required = true)
    private String lastName;

    @CsvBindByName(column = "dob", required = true)
    private LocalDate dob;

    @CsvBindByName(column = "doj", required = true)
    private LocalDate doj;

    @CsvBindByName(column = "remarks", required = true)
    private String remarks;

    @CsvBindByName(column = "email", required = true)
    private String email;

    @CsvBindByName(column = "contactNumber", required = true)
    private String contactNumber;

    @CsvBindByName(column = "createdAt", required = true)
    private LocalDateTime createdAt;

    @CsvBindByName(column = "updatedAt", required = true)
    private LocalDateTime updatedAt;

    @CsvBindByName(column = "addressDto", required = true)
    private AddressDto addressDto;

    @CsvBindByName(column = "departmentId", required = true)
    private BigInteger departmentId;
}
