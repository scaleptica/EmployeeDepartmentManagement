package org.paytm.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.paytm.controllers.DepartmentController;
import org.paytm.controllers.EmployeeController;
import org.paytm.entities.Department;
import org.paytm.entities.Employee;
import org.paytm.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    //how to write tests for updating and csvDataPart

    @BeforeEach
    void setup(){
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

        initMocks(this);
    }

    @Test
    public void addEmployeeTest_success() throws Exception {
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

        Mockito.when(employeeService.addEmployee(REC1)).thenReturn(REC1);

        mockMvc.perform(post("/employee/create")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void getAllEmployees_success() throws Exception{

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
        Mockito.when(this.employeeService.getAllEmployees(null, null))
                .thenReturn(records);

        mockMvc.perform(get("/employee/list")
                        .contentType(MediaType.APPLICATION_JSON)
                )       .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect((ResultMatcher) jsonPath("$", hasSize(2)))
                .andExpect((ResultMatcher) jsonPath("$[1].contact_number", is("1056723984")));
    }

    @Test
    public void getEmployeeWithId_success() throws Exception{

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

        Employee record = REC1;
        Mockito.when(this.employeeService.getEmployeeWithId(REC1.getId())).thenReturn(record);

        mockMvc.perform(get("/employee/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )       .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect((ResultMatcher) jsonPath("$", Matchers.notNullValue()))
                .andExpect((ResultMatcher) jsonPath("$.contact_number", is("1092387456")));
    }


    @Test
    public void deleteEmployee_success() throws Exception{
        Mockito.when(this.employeeService.deleteEmployee(BigInteger.valueOf(1))).thenReturn("SUCCESS");
        mockMvc.perform(delete("/employee/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
