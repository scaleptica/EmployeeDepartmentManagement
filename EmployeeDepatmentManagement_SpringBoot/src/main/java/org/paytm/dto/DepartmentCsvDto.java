package org.paytm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class DepartmentCsvDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @CsvBindByName(column = "id")
    private BigInteger id;

    @CsvBindByName(column = "name", required = true)
    private String name;

    @CsvBindByName(column = "remarks", required = true)
    private String remarks;
}
