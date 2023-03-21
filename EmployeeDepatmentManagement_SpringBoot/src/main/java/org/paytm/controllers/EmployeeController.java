package org.paytm.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.paytm.dto.EmployeeCsvDto;
import org.paytm.dto.EmployeeDto;
import org.paytm.entities.Department;
import org.paytm.entities.Employee;
import org.paytm.exceptions.BadRequestException;
import org.paytm.exceptions.InternalServerSqlException;
import org.paytm.exceptions.RecordNotFoundException;
import org.paytm.mapper.EmployeeMapper;
import org.paytm.services.DepartmentService;
import org.paytm.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {

  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private DepartmentService departmentService;

  @Autowired
  private EmployeeMapper employeeMapper;

  @GetMapping(value = "/list")
  public List<EmployeeDto> getAllEmployees(
      @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
      @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize
  ) {
    List<Employee> employeeList = this.employeeService.getAllEmployees(pageNumber, pageSize);
    List<EmployeeDto> employeeDtoList = this.employeeMapper.fromEntityToDtoList(employeeList);
    return employeeDtoList;
  }

  @GetMapping(value = "/{id}")
  public EmployeeDto getEmployeeWithId(@PathVariable BigInteger id) throws RecordNotFoundException {
    Employee employeeById = this.employeeService.getEmployeeWithId(id);
    EmployeeDto employeeByIdDto = this.employeeMapper.fromEntityToDto(employeeById);
    return employeeByIdDto;
  }

  @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
  public EmployeeDto addEmployee(@RequestBody EmployeeDto employeeDto)
      throws BadRequestException, RecordNotFoundException, InternalServerSqlException {
    Employee employeeToBeAdded = this.employeeMapper.fromDtoToEntity(employeeDto);
    Employee addedEmployee = this.employeeService.addEmployee(employeeToBeAdded);
    EmployeeDto addedEmployeeDto = this.employeeMapper.fromEntityToDto(addedEmployee);
    return addedEmployeeDto;
  }

  @PutMapping(value = "/{id}")
  public EmployeeDto updateEmployee(@RequestBody EmployeeDto employeeDto,
      @PathVariable BigInteger id)
      throws BadRequestException, RecordNotFoundException, InternalServerSqlException {
    Employee employeeToBeUpdated = this.employeeMapper.fromDtoToEntity(employeeDto);
    Employee updatedEmployee = this.employeeService.updateEmployee(id, employeeToBeUpdated);
    EmployeeDto updatedEmployeeDto = this.employeeMapper.fromEntityToDto(updatedEmployee);
    return updatedEmployeeDto;
  }

  @DeleteMapping(value = "/{id}")
  public void deleteEmployee(@PathVariable BigInteger id) throws RecordNotFoundException {
    this.employeeService.deleteEmployee(id);
  }

  @PutMapping(value = "/{id}/department/{department_id}")
  public EmployeeDto setEmployeeDepartment(@PathVariable BigInteger id, @PathVariable BigInteger department_id) {
    Employee employeeFound = this.employeeService.identityEmployeeFinder(id, "Employee Id not found");
    Department departmentFound = this.departmentService.checkDepartmentById(department_id, "Department Id not found");
    employeeFound.setDepartment(departmentFound);
    Employee updatedEmployeeEntity = this.employeeService.updateEmployee(id, employeeFound);
    return this.employeeMapper.fromEntityToDto(updatedEmployeeEntity);
  }

}
