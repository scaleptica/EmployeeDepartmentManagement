package org.paytm.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.paytm.entities.Address;
import org.paytm.entities.Employee;
import org.paytm.repository.EmployeeRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {


    @MockBean
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setup(){



    }

    @Test
    public void getAllEmployeeTest_success(){

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

        Employee REC2 = new Employee();
        REC2.setId(BigInteger.valueOf(2));
        REC2.setFirstName("Suresh");
        REC2.setLastName("Sharma");
        REC2.setDob(dob);
        REC2.setDoj(doj);
        REC2.setContactNumber("1056723984");
        REC2.setEmail("suresh@gmail.com");
        REC2.setRemarks("HR guy");
        REC2.setCreatedAt(created_at1);
        REC2.setUpdatedAt(updated_at1);

        List<Employee> records = new ArrayList<>(Arrays.asList(REC1, REC2));

        when(this.employeeRepository.findAll()).thenReturn(records);

        assertEquals(2, this.employeeService.getAllEmployees(null, null).size());
    }

    @Test
    public void getEmployeeByIdTest_success() {
        String contact_number = "1092387456";

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

        when(this.employeeRepository.findById(BigInteger.valueOf(2)))
                .thenReturn(Optional.of(REC1));
        Employee employeeById = this.employeeService.getEmployeeWithId(BigInteger.valueOf(2));
        assertEquals(contact_number, employeeById.getContactNumber());
    }

    @Test
    public void addEmployeeTest_success() {
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

        when(this.employeeRepository.save(REC1)).thenReturn(REC1);
        assertEquals(REC1, this.employeeService.addEmployee(REC1));
    }

    @Test
    public void deleteEmployeeTest() {
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

        this.employeeService.deleteEmployee(BigInteger.valueOf(1));
        Mockito.verify(employeeRepository).deleteById(BigInteger.valueOf(1));
        //checking if the delete method from the repository has been called once or not
    }
}
