package org.paytm.controllers;

import org.paytm.dto.DepartmentDto;
import org.paytm.dto.EmployeeDto;
import org.paytm.entities.Department;
import org.paytm.entities.Employee;
import org.paytm.exceptions.BadRequestException;
import org.paytm.exceptions.InternalServerSqlException;
import org.paytm.exceptions.RecordNotFoundException;
import org.paytm.mapper.DepartmentMapper;
import org.paytm.mapper.EmployeeMapper;
import org.paytm.services.DepartmentService;
import org.paytm.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/employee")
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
    ){  //This is a get-API/get request
        List<Employee> employeeList = this.employeeService.getAllEmployees(pageNumber, pageSize);
        List<EmployeeDto> employeeDtoList = this.employeeMapper.fromEntityToDtoList(employeeList);
        return employeeDtoList;
    }

    @GetMapping(value = "/{id}")
    public EmployeeDto getEmployeeWithId(@PathVariable BigInteger id) throws RecordNotFoundException{
        Employee employeeById = this.employeeService.getEmployeeWithId(id);
        EmployeeDto employeeByIdDto = this.employeeMapper.fromEntityToDto(employeeById);
        return employeeByIdDto;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto addEmployee( @RequestBody EmployeeDto employeeDto)throws BadRequestException, RecordNotFoundException, InternalServerSqlException{
        try {
            if(Objects.isNull(employeeDto.getFirstName())){ //for first name??
                throw new BadRequestException("Name not provided...");}
        } catch(RuntimeException ex) {
            throw ex;
        }
        Employee employeeToBeAdded = this.employeeMapper.fromDtoToEntity(employeeDto);
        Employee addedEmployee = this.employeeService.addEmployee(employeeToBeAdded);
        EmployeeDto addedEmployeeDto = this.employeeMapper.fromEntityToDto(addedEmployee);
        return addedEmployeeDto;
    }

    @PutMapping(value = "/{id}")
    public EmployeeDto updateEmployee( @RequestBody EmployeeDto employeeDto, @PathVariable BigInteger id) throws BadRequestException, RecordNotFoundException, InternalServerSqlException {
        try {
            if(Objects.isNull(employeeDto.getFirstName())){
                throw new BadRequestException("Name not provided...");}
        } catch(RuntimeException ex) {
            throw ex;
        }
        Employee employeeToBeUpdated = this.employeeMapper.fromDtoToEntity(employeeDto);
        Employee updatedEmployee = this.employeeService.updateEmployee(id, employeeToBeUpdated);
        EmployeeDto updatedEmployeeDto = this.employeeMapper.fromEntityToDto(updatedEmployee);
        return updatedEmployeeDto;
    }

    @DeleteMapping(value = "/{id}")
    public void deleteEmployee(@PathVariable BigInteger id) throws RecordNotFoundException{
        this.employeeService.deleteEmployee(id);
    }
    @PutMapping(value = "/{id}/department/{department_id}")
    public EmployeeDto setEmployeeDepartment(@PathVariable BigInteger id, BigInteger department_id){
        Employee employeeFound = this.employeeService.identityEmployeeFinder(id, "Employee Id not found");
        Department departmentFound = this.departmentService.getDepartmentById(department_id, "Department Id not found");
        employeeFound.setDepartment(departmentFound);
        Employee updatedEmployeeEntity = this.employeeService.updateEmployee(id, employeeFound);
        return this.employeeMapper.fromEntityToDto(updatedEmployeeEntity);
    }

    /*@PutMapping(value = "/{id}/department/{department_id}")
    public EmployeeDto ss@PathVariable BigInteger id, String department_name){
        fetch emp details by id - done
        then check if dept id exists on the basis of the dept_id - Done
        get employee on the basis of emp id, if exists - done
        get dept basis on the dept id, - Done
        employee.setDepartment(department) - Done
        employeeRepo.save
    }*/
}
