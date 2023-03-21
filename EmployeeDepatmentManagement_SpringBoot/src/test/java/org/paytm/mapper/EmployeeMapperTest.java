package org.paytm.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.TypeMap;
import org.paytm.dto.DepartmentDto;
import org.paytm.dto.EmployeeDto;
import org.paytm.entities.Department;
import org.paytm.entities.Employee;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class EmployeeMapperTest {

    @Mock
    private TypeMap<EmployeeDto, Employee> typeMapDTO;

    @Mock
    private TypeMap<Employee, EmployeeDto> typeMapDTO2;

    @InjectMocks
    private EmployeeMapper employeeMapper;

    @BeforeEach
    void setup(){


    }

    @Test
    public void fromDtoToEntityTest_success() {

        Employee REC1 = new Employee();
        LocalDateTime created_at1 = LocalDateTime.parse("2017-06-04T10:20");
        LocalDateTime updated_at1 = LocalDateTime.parse("2018-06-04T09:20");
        LocalDate dob = LocalDate.parse("2000-08-04");
        LocalDate doj = LocalDate.parse("2017-06-04");
        REC1.setId(BigInteger.valueOf(1));
        REC1.setFirstName("Rajesh");
        REC1.setLastName("Gupta");
        REC1.setDob(dob);
        REC1.setDoj(doj);
        REC1.setContactNumber("1092387456");
        REC1.setEmail("rajesh@rediffmail.com");
        REC1.setRemarks("IT guy");
        REC1.setCreatedAt(created_at1);
        REC1.setUpdatedAt(updated_at1);

        EmployeeDto REC1dto = new EmployeeDto();
        REC1dto.setId(BigInteger.valueOf(1));
        REC1dto.setFirstName("Rajesh");
        REC1dto.setLastName("Gupta");
        REC1dto.setDob(dob);
        REC1dto.setDoj(doj);
        REC1dto.setContactNumber("1092387456");
        REC1dto.setEmail("rajesh@rediffmail.com");
        REC1dto.setRemarks("IT guy");
        REC1dto.setCreatedAt(created_at1);
        REC1dto.setUpdatedAt(updated_at1);

        when(this.typeMapDTO.map(REC1dto)).thenReturn(REC1);

        assertEquals(REC1, this.employeeMapper.fromDtoToEntity(REC1dto));
    }

    @Test
    public void fromEntityToDtoTest_success() {

        Employee REC1 = new Employee();
        LocalDateTime created_at1 = LocalDateTime.parse("2017-06-04T10:20");
        LocalDateTime updated_at1 = LocalDateTime.parse("2018-06-04T09:20");
        LocalDate dob = LocalDate.parse("2000-08-04");
        LocalDate doj = LocalDate.parse("2017-06-04");
        REC1.setId(BigInteger.valueOf(1));
        REC1.setFirstName("Rajesh");
        REC1.setLastName("Gupta");
        REC1.setDob(dob);
        REC1.setDoj(doj);
        REC1.setContactNumber("1092387456");
        REC1.setEmail("rajesh@rediffmail.com");
        REC1.setRemarks("IT guy");
        REC1.setCreatedAt(created_at1);
        REC1.setUpdatedAt(updated_at1);

        EmployeeDto REC1dto = new EmployeeDto();
        REC1dto.setId(BigInteger.valueOf(1));
        REC1dto.setFirstName("Rajesh");
        REC1dto.setLastName("Gupta");
        REC1dto.setDob(dob);
        REC1dto.setDoj(doj);
        REC1dto.setContactNumber("1092387456");
        REC1dto.setEmail("rajesh@rediffmail.com");
        REC1dto.setRemarks("IT guy");
        REC1dto.setCreatedAt(created_at1);
        REC1dto.setUpdatedAt(updated_at1);

        when(this.typeMapDTO2.map(REC1)).thenReturn(REC1dto);

        assertEquals(REC1dto, this.employeeMapper.fromEntityToDto(REC1));

    }
}
