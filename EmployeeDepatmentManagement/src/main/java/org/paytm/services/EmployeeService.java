package org.paytm.services;

import org.paytm.MainApp;
import org.paytm.dto.EmployeeDto;
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
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeMapper employeeMapper;  //similarly inject address service
    @Autowired
    private AddressService addressService;
    @Autowired
    private DepartmentService departmentService;

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    public Employee identityEmployeeFinder(BigInteger idCode, String replyMessage) throws RecordNotFoundException{
        Employee foundInDb = this.employeeRepository.findById(idCode)    //redundant  in line 37, 52
                .orElseThrow(() -> new RecordNotFoundException(replyMessage));
        Employee employeeDetails = this.employeeRepository.findById(idCode).get();
        return employeeDetails;
    }

    public List<Employee> getAllEmployees(Integer pageNumber, Integer pageSize){

        Pageable p = PageRequest.of(pageNumber, pageSize);
        Page<Employee> pageEmployee = this.employeeRepository.findAll(p);
        List<Employee> employeeList = pageEmployee.getContent();
        return employeeList;

        /*List<Employee> employeeArrayList = new ArrayList<>();
        this.employeeRepository.findAll()
                .forEach(employeeArrayList::add);
        return employeeArrayList;*/
    }

    public Employee getEmployeeWithId(BigInteger id) throws RecordNotFoundException {
        Employee employeeReceived = identityEmployeeFinder(id, "Id not found...");
        return employeeReceived;
    }

    public Employee addEmployee(Employee employee) throws RecordAlreadyExistsException, InternalServerSqlException {
        try {
            if(!this.employeeRepository.existsByEmailAndMobile(employee.getEmail(), employee.getContactNumber())){
                throw new RecordAlreadyExistsException("Employee exists already");}
        } catch(RuntimeException ex) {
            throw ex;
        }

        Address savedAddress = addressService.addAddress(employee.getAddress());
        employee.setAddress(savedAddress);

        Employee createdEmployee = null;  //save details in address first, then assign that address to employee, then save employee
        try {
            createdEmployee = this.employeeRepository.save(employee);
        }catch (DataAccessException ex){
            throw new InternalServerSqlException("Failed to save employee details...");
        }
        return createdEmployee;
    }

    public Employee updateEmployee(BigInteger id, Employee employee) throws RecordNotFoundException, InternalServerSqlException {

        Employee employeeFound = identityEmployeeFinder(id, "Id not found...");

        Address newAddress = employee.getAddress(); //handle exception, dont do multi gets, possible nullptr exception
        Address newSavedAddress = addressService.updateAddress(newAddress.getId(), employee.getAddress());
        employee.setAddress(newSavedAddress);

        Employee employeeAfterSet = this.employeeMapper.setNewDetails(employee, employeeFound); //allow null value

        Employee updatedEmployee = null;
        try {
            updatedEmployee = this.employeeRepository.save(employeeAfterSet);
        }catch (DataAccessException ex){
            throw new InternalServerSqlException("Failed to save department details...");
        }
        return employeeAfterSet;
    }

    public void deleteEmployee(BigInteger employeeId) throws RecordNotFoundException {
        //this.addressService.deleteAddressWithEmployeeId(employeeId);
        //how are we relating address and employeeId, shouldn't we use address_id instead??
        try{
            this.employeeRepository.deleteById(employeeId);
            System.out.println("Data deleted successfully!!!");
        }catch(EmptyResultDataAccessException ex){
            throw new RecordNotFoundException("Id does not exist...");
        }
    }

    //todo=> delete employee directly, and check it is deleting entry from both dept and address table

    public void deleteEmployeesWithDeptId(BigInteger department_id) throws InternalServerSqlException{
        //todo=> find list of employees whose department id matches with the incoming department id - done
        // update identified/matched employees department_id with null -
        if(this.employeeRepository.existsByDepartmentId(department_id)) {
            try{
                List<Employee> employeeArrayList = new ArrayList<>();
                employeeArrayList = this.employeeRepository.findByDepartmentId(department_id);
                for(Employee employee : employeeArrayList){
                    employee.setDepartment(null);
                }
                //save list to database
            }catch (RuntimeException ex){
                log.error("Could not be deleted...");
                throw new InternalServerSqlException("Employee Deletion via department id failed...");
            }
        }
    }
    //write csv api...
}
