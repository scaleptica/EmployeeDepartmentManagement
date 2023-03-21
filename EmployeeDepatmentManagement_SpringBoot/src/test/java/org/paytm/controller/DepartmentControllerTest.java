package org.paytm.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.paytm.MainApp;
import org.paytm.controllers.DepartmentController;
import org.paytm.entities.Department;
import org.paytm.mapper.DepartmentMapper;
import org.paytm.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.util.ReflectionUtils.setField;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(DepartmentController.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
//        classes = {MainApp.class, DepartmentControllerTest.class})
//@AutoConfigureMockMvc
//@TestPropertySource(locations = "classpath:application.properties")
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private DepartmentService departmentService;


//    @InjectMocks
//    private DepartmentController departmentController;

    //how to write tests for updating and csvDataPart

    @BeforeEach
    void setup(){


//        setField(
//                departmentController,2
//                "departmentMapper",
//                new DepartmentMapper());
        Department REC1 = new Department();
        LocalDateTime created_at1 = LocalDateTime.parse("2000-04-04T10:20");
        LocalDateTime updated_at1 = LocalDateTime.parse("2001-06-04T09:20");
        REC1.setId(BigInteger.valueOf(1));
        REC1.setName("Research and Development");
        REC1.setRemarks("Scientists");
        REC1.setCreatedAt(created_at1);
        REC1.setUpdatedAt(updated_at1);

        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void addDepartmentTest_success() throws Exception {
        Department REC1 = new Department();
        LocalDateTime created_at1 = LocalDateTime.parse("2000-04-04T10:20");
        LocalDateTime updated_at1 = LocalDateTime.parse("2001-06-04T09:20");
        REC1.setId(BigInteger.valueOf(1));
        REC1.setName("Research and Development");
        REC1.setRemarks("Scientists");
        REC1.setCreatedAt(created_at1);
        REC1.setUpdatedAt(updated_at1);

//        Mockito.when(this.departmentService.addDepartment(REC1)).thenReturn(department);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/department/create")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));

    }

    @Test
    public void getAllDepartments_success() throws Exception{

        Department REC1 = new Department();
        LocalDateTime created_at1 = LocalDateTime.parse("2000-04-04T10:20");
        LocalDateTime updated_at1 = LocalDateTime.parse("2001-06-04T09:20");
        REC1.setId(BigInteger.valueOf(1));
        REC1.setName("Research and Development");
        REC1.setRemarks("Scientists");
        REC1.setCreatedAt(created_at1);
        REC1.setUpdatedAt(updated_at1);

        Department REC2 = new Department();
        LocalDateTime created_at2 = LocalDateTime.parse("2000-04-04T10:20");
        LocalDateTime updated_at2 = LocalDateTime.parse("2001-06-04T09:20");
        REC2.setId(BigInteger.valueOf(2));
        REC2.setName("IT and Tech");
        REC2.setRemarks("Engineers");
        REC2.setCreatedAt(created_at2);
        REC2.setUpdatedAt(updated_at2);

        List<Department> records = new ArrayList<>(Arrays.asList(REC1, REC2));
//        Mockito.when(this.departmentService.getAllDepartments(null, null))
//                .thenReturn(records);

        mockMvc.perform(get("/department/list")
                        .contentType(MediaType.APPLICATION_JSON)
                )       .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect((ResultMatcher) jsonPath("$", hasSize(2)))
                .andExpect((ResultMatcher) jsonPath("$[1].name", is("IT and Tech")));
    }

    @Test
    public void getDepartmentWithId_success() throws Exception{

        Department REC1 = new Department();
        LocalDateTime created_at1 = LocalDateTime.parse("2000-04-04T10:20");
        LocalDateTime updated_at1 = LocalDateTime.parse("2001-06-04T09:20");
        REC1.setId(BigInteger.valueOf(1));
        REC1.setName("Research and Development");
        REC1.setRemarks("Scientists");
        REC1.setCreatedAt(created_at1);
        REC1.setUpdatedAt(updated_at1);

        Department record = REC1;
//        Mockito.when(this.departmentService.getDepartmentWithId(REC1.getId())).thenReturn(record);

        mockMvc.perform(get("/department/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )       .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect((ResultMatcher) jsonPath("$", Matchers.notNullValue()))
                .andExpect((ResultMatcher) jsonPath("$.name", is("Research and Development")));
    }


    @Test
    public void deleteDepartment_success() throws Exception{
//        Mockito.when(this.departmentService.deleteDepartment(BigInteger.valueOf(1))).thenReturn("SUCCESS");
        mockMvc.perform(delete("/department/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
