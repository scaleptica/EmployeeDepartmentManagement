package org.paytm.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.paytm.config.AsyncConfig;
import org.paytm.entities.Department;
import org.paytm.repository.DepartmentRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.multipart.MultipartFile;


import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @MockBean
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;


    ////how to write tests for updating and csvDataPart

    @BeforeEach
    void setup(){


        setField(
                departmentService,
                "employeeService",
                new EmployeeService());

        setField(
                departmentService,
                "asyncConfig",
                new AsyncConfig());

        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void getAllDepartmentTest_success(){

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

        when(this.departmentRepository.findAll()).thenReturn(records);

        assertEquals(2, this.departmentService.getAllDepartments(null, null).size());
    }

    @Test
    public void getDepartmentByIdTest_success() {
        String department_name = "IT and Tech";

        Department REC2 = new Department();
        LocalDateTime created_at2 = LocalDateTime.parse("2000-04-04T10:20");
        LocalDateTime updated_at2 = LocalDateTime.parse("2001-06-04T09:20");
        REC2.setId(BigInteger.valueOf(2));
        REC2.setName("IT and Tech");
        REC2.setRemarks("Engineers");
        REC2.setCreatedAt(created_at2);
        REC2.setUpdatedAt(updated_at2);

        when(this.departmentRepository.findById(BigInteger.valueOf(2)))
                .thenReturn(Optional.of(REC2));
        Department departmentById = this.departmentService.getDepartmentWithId(BigInteger.valueOf(2));
        assertEquals(department_name, departmentById.getName());
    }

    @Test
    public void addDepartmentTest_success() {
        Department REC1 = new Department();
        LocalDateTime created_at1 = LocalDateTime.parse("2000-04-04T10:20");
        LocalDateTime updated_at1 = LocalDateTime.parse("2001-06-04T09:20");
        REC1.setId(BigInteger.valueOf(1));
        REC1.setName("Research and Development");
        REC1.setRemarks("Scientists");
        REC1.setCreatedAt(created_at1);
        REC1.setUpdatedAt(updated_at1);
        when(this.departmentRepository.save(REC1)).thenReturn(REC1);
        assertEquals(REC1, this.departmentService.addDepartment(REC1));
    }

    @Test
    public void deleteDepartmentTest_success() {
        Department REC1 = new Department();
        LocalDateTime created_at1 = LocalDateTime.parse("2000-04-04T10:20");
        LocalDateTime updated_at1 = LocalDateTime.parse("2001-06-04T09:20");
        REC1.setId(BigInteger.valueOf(1));
        REC1.setName("Research and Development");
        REC1.setRemarks("Scientists");
        REC1.setCreatedAt(created_at1);
        REC1.setUpdatedAt(updated_at1);

        this.departmentService.deleteDepartment(BigInteger.valueOf(1));
        Mockito.verify(departmentRepository).deleteById(BigInteger.valueOf(1));
        //checking if the delete method from the repository has been called once or not
    }

    /*@Test
    public void testAsyncMethod() {
        // Create a mock of ThreadPoolTaskExecutor
        ThreadPoolTaskExecutor executorMock = Mockito.mock(ThreadPoolTaskExecutor.class);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "file.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello World".getBytes()
        );

        // Call the async method
        this.departmentService.csvDataMigration(file);

        // Verify that the executor was used
        Mockito.verify(executorMock).execute(Mockito.any(Runnable.class));
    }*/
}