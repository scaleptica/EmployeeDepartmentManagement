package org.paytm.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.paytm.dto.EmployeeCsvDto;
import org.paytm.dto.EmployeeCsvDto;
import org.paytm.entities.Address;
import org.paytm.entities.Department;
import org.paytm.entities.Employee;
import org.paytm.exceptions.InternalServerSqlException;
import org.paytm.exceptions.RecordAlreadyExistsException;
import org.paytm.exceptions.RecordNotFoundException;
import org.paytm.mapper.EmployeeMapper;
import org.paytm.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class EmployeeService {

  private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);
  @Autowired
  private EmployeeRepository employeeRepository;
  @Autowired
  private EmployeeMapper employeeMapper;
  @Autowired
  private AddressService addressService;


  public Employee identityEmployeeFinder(BigInteger idCode, String replyMessage)
          throws RecordNotFoundException {
    Employee foundInDb = this.employeeRepository.findById(idCode)    //redundant  in line 37, 52
            .orElseThrow(() -> new RecordNotFoundException(replyMessage));
    Employee employeeDetails = this.employeeRepository.findById(idCode).get();
    return employeeDetails;
  }

  public List<Employee> getAllEmployees(Integer pageNumber, Integer pageSize) {
    if(pageNumber!=null && pageSize!=null) {
      Pageable p = PageRequest.of(pageNumber, pageSize);
      Page<Employee> pageEmployee = this.employeeRepository.findAll(p);
      List<Employee> employeeList = pageEmployee.getContent();
      return employeeList;
    }else{
      log.info("Page number and page size not specifies...");
      return this.employeeRepository.findAll();
    }
  }

  public Employee getEmployeeWithId(BigInteger id) throws RecordNotFoundException {
    Employee employeeReceived = identityEmployeeFinder(id, "Id not found...");
    return employeeReceived;
  }

  @Transactional  //How to use it here?, because code should be from same repos for transactional to work, here they are from different repo/service
  public Employee addEmployee(Employee employee)
          throws RecordAlreadyExistsException, InternalServerSqlException {
    try {
      if (this.employeeRepository.existsByEmailAndContactNumber(employee.getEmail(),
              employee.getContactNumber())) {
        throw new RecordAlreadyExistsException("Employee exists already");
      }
    } catch (RuntimeException ex) {
      throw ex;
    }

    Address savedAddress = null;
    if(employee.getAddress()!=null){
      savedAddress = this.addressService.addAddress(employee.getAddress());
      log.info("Address added to DB");
    }
    //employee.setAddress(savedAddress);  //is this required??

    Employee createdEmployee = null;
    try {
      createdEmployee = this.employeeRepository.save(employee);
      log.info("New employee added...");
    } catch (DataAccessException ex) {
      throw new InternalServerSqlException("Failed to save employee details...");
    }
    return createdEmployee;
  }

  @Transactional
  public Employee updateEmployee(BigInteger id, Employee employee)
          throws RecordNotFoundException, InternalServerSqlException {

    Employee employeeFound = identityEmployeeFinder(id, "Id not found...");

    Address newSavedAddress = null;
    if(employee.getAddress()!=null) {
      Address newAddress = employee.getAddress();
      newSavedAddress = this.addressService.updateAddress(newAddress.getId(), employee.getAddress());
    }
    employee.setAddress(newSavedAddress); //again, is this required??

    Employee employeeAfterSet = this.employeeMapper.setNewDetails(employee, employeeFound);

    Employee updatedEmployee = null;
    try {
      updatedEmployee = this.employeeRepository.save(employeeAfterSet);
      log.info("Employ details updated");
    } catch (DataAccessException ex) {
      throw new InternalServerSqlException("Failed to save employee details...");
    }
    return employeeAfterSet;
  }

  public String deleteEmployee(BigInteger employeeId) throws RecordNotFoundException {
    Employee employeeFound = identityEmployeeFinder(employeeId, "Id not found...");
    employeeFound.setDepartment(null);  //fetch empid record, set employee employee to null- logical deletion - soft delete
    employeeFound.setAddress(null);
    try {
      this.employeeRepository.deleteById(employeeId);
      log.info("Employee data deleted successfully");
      return "Department data deleted successfully!  " + employeeId.toString();
    } catch (EmptyResultDataAccessException ex) {
      throw new RecordNotFoundException("Id does not exist...");
    }
  }

  public void setAllEmployeeDepartmentNull(BigInteger employee_id)
          throws InternalServerSqlException {
    if (this.employeeRepository.existsByDepartmentId(employee_id)) {
      List<Employee> employeeArrayList = new ArrayList<>();
      employeeArrayList = this.employeeRepository.findByDepartmentId(employee_id);
      log.info("Department deleting, setting all employee departments to null");
      for (Employee employee : employeeArrayList) {
        employee.setDepartment(null);
        this.employeeRepository.save(employee);
      }
    }
  }
}
