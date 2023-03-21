package org.paytm.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.paytm.config.AsyncConfig;
import org.paytm.dto.DepartmentCsvDto;
import org.paytm.entities.Department;
import org.paytm.exceptions.InternalServerSqlException;
import org.paytm.exceptions.RecordAlreadyExistsException;
import org.paytm.exceptions.RecordNotFoundException;
//import org.paytm.kafka.TopicKafkaProducer;
import org.paytm.mapper.DepartmentMapper;
import org.paytm.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;


@Service
public class DepartmentService {

  private static final Logger log = LoggerFactory.getLogger(DepartmentService.class);
  @Autowired
  private DepartmentRepository departmentRepository;
  @Autowired
  private DepartmentMapper departmentMappers;
  @Autowired
  private EmployeeService employeeService;

//  @Autowired
//  private TopicKafkaProducer topicKafkaProducer;

  @Autowired
  private AsyncConfig asyncConfig;

  public Department checkDepartmentById(BigInteger idCode, String replyMessage)
      throws RecordNotFoundException {
    Department foundInDb = this.departmentRepository.findById(idCode)    //redundant  in line 37, 52
        .orElseThrow(() -> new RecordNotFoundException(replyMessage));
    return foundInDb;
  }

  public Department getDepartmentById(BigInteger idCode)
          throws RecordNotFoundException {
    Department foundInDb = this.departmentRepository.findById(idCode)
            .orElseThrow(() -> new RecordNotFoundException("Not found"));
    return foundInDb;
  }

  public List<Department> getAllDepartments(Integer pageNumber, Integer pageSize) {
    if(pageNumber!=null && pageSize!=null) {
      Pageable p = PageRequest.of(pageNumber, pageSize);
      Page<Department> pageDepartment = this.departmentRepository.findAll(p);
      List<Department> departmentList = pageDepartment.getContent();
      return departmentList;
    }else{
      log.info("Page number and page size not specifies...");
      return this.departmentRepository.findAll();
    }
  }


  public Department getDepartmentWithId(BigInteger departmentId) throws RecordNotFoundException {
    Department departmentReceived = checkDepartmentById(departmentId, "Id not found...");
    return departmentReceived;
  }

  public Department addDepartment(Department department)
      throws RecordAlreadyExistsException, InternalServerSqlException {  //create
    try {
      if (this.departmentRepository.existsByName(department.getName())) {
        throw new RecordAlreadyExistsException("Name exists already");
      }
    } catch (RuntimeException ex) {
      throw ex;
    }

    Department createdDepartment = null;
    try {
      createdDepartment = this.departmentRepository.save(department);
      log.info("Department created...");
    } catch (DataAccessException ex) {
      throw new InternalServerSqlException("Failed to save department details...");
    }
    return createdDepartment;
  }

  public Department updateDepartment(BigInteger departmentId, Department department)
      throws RecordNotFoundException, InternalServerSqlException {
    Department departmentFound = checkDepartmentById(departmentId, "Id not found...");
    Department departmentDetailsAfterSet = this.departmentMappers.setNewDetails(department, departmentFound);

    Department updatedDepartment;
    try {
      updatedDepartment = this.departmentRepository.save(departmentDetailsAfterSet);
      log.info("Department details updated");
    } catch (DataAccessException ex) {
      throw new InternalServerSqlException("Failed to save department details...");
    }
    return updatedDepartment;
  }


  public String deleteDepartment(BigInteger departmentId) throws RecordNotFoundException, InternalServerSqlException {   //check if employees exist in department or not --> 2nd
    this.employeeService.setAllEmployeeDepartmentNull(departmentId); //setting employee dept_ids to null
    try {
      this.departmentRepository.deleteById(departmentId);
      log.info("Department data deleted successfully!!!");
      return "Department data deleted successfully!  " + departmentId.toString();
    } catch (EmptyResultDataAccessException ex) {
      log.error("Department Id not found, check input...");
      throw new RecordNotFoundException("Id does not exist...");
    }
  }

  @Async("asyncExecutor")
  public Map<String, Object> csvDataMigration(MultipartFile file) {
    Map<String, Object> map = new HashMap<>();
    if (file.isEmpty()) {
      map.put("message", "Please select a department CSV file to upload.");
      return map;
    }else{
      return mainCsvMigration(file, map);
    }
  }

  public Map<String, Object> mainCsvMigration(MultipartFile file, Map<String, Object> map){
    try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

      CsvToBean<DepartmentCsvDto> dataInBeanFormat = new CsvToBeanBuilder(reader)
              .withType(DepartmentCsvDto.class)
              .withIgnoreLeadingWhiteSpace(true)
              .build();

      // convert `CsvToBean` object to list of data
      List<DepartmentCsvDto> listData = dataInBeanFormat.parse();

      for (DepartmentCsvDto csvData : listData) {
        Department department = null;
        department = this.departmentMappers.fromDepartmentCsvDtoToDepartment(csvData);
        department.setCreatedAt(LocalDateTime.now());
        department.setUpdatedAt(LocalDateTime.now());
        this.addDepartment(department);
      }
      map.put("Records", listData);
      return map;
    } catch (Exception ex) {
      log.error("Exception ex :: ", ex);
      map.put("message", "An error occurred while processing the CSV file.");
      return map;
    }
  }

//  public Map<String, Object> kafkaCsvMigration(MultipartFile file, Map<String, Object> map){
//    List<DepartmentCsvDto> list = new ArrayList<>();
//    try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
//
//      CsvToBean<DepartmentCsvDto> dataInBeanFormat = new CsvToBeanBuilder(reader)
//              .withType(DepartmentCsvDto.class)
//              .withIgnoreLeadingWhiteSpace(true)
//              .build();
//
//      // convert `CsvToBean` object to list of data
//      List<DepartmentCsvDto> listData = dataInBeanFormat.parse();
//
//      for (DepartmentCsvDto csvData : listData) {
//        try{
//          this.topicKafkaProducer.departmentCsvDtoRead(csvData);
//        }catch(Exception ex)
//        {
//          list.add(csvData);
//        }
//      }
//      map.put("Unprocessed Records", list);
//      return map;
//    } catch (Exception ex) {
//      log.error("Exception ex :: ", ex);
//      map.put("message", "An error occurred while processing the CSV file.");
//      return map;
//    }
//  }

}
