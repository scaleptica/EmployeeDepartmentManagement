package org.paytm.services;

import org.paytm.MainApp;
import org.paytm.dto.DepartmentDto;
import org.paytm.entities.Department;
import org.paytm.exceptions.InternalServerSqlException;
import org.paytm.exceptions.RecordAlreadyExistsException;
import org.paytm.exceptions.RecordNotFoundException;
import org.paytm.mapper.DepartmentMapper;
import org.paytm.repository.DepartmentRepository;
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
import java.util.List;
import java.util.Objects;


@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentMapper departmentMappers;

    @Autowired
    private EmployeeService employeeService;

    private static final Logger log = LoggerFactory.getLogger(DepartmentService.class);


    public Department getDepartmentById(BigInteger idCode, String replyMessage)throws RecordNotFoundException{
        Department foundInDb = this.departmentRepository.findById(idCode)    //redundant  in line 37, 52
                .orElseThrow(() -> new RecordNotFoundException(replyMessage));
        return foundInDb;
    }

    public List<Department> getAllDepartments(Integer pageNumber, Integer pageSize){    //if pagenumber and size not received then simply call findAll()
        Pageable p = PageRequest.of(pageNumber, pageSize);
        Page<Department> pageDepartment = this.departmentRepository.findAll(p);
        List<Department> departmentList = pageDepartment.getContent();
        return departmentList;
    }


    public Department getDepartmentWithId(BigInteger departmentId) throws RecordNotFoundException {
        Department departmentReceived = getDepartmentById(departmentId, "Id not found...");
        return departmentReceived;
    }

    public Department addDepartment(Department department) throws RecordAlreadyExistsException, InternalServerSqlException {  //create
        try {
            if(!this.departmentRepository.existsByName(department.getName())){
                throw new RecordNotFoundException("Name does not exist");}
        } catch(RuntimeException ex) {
            throw ex;
        }

        Department createdDepartment = null;
        try {
            createdDepartment = this.departmentRepository.save(department); //save always throws DataAccessException
        }catch (DataAccessException ex){
            throw new InternalServerSqlException("Failed to save department details...");
        }
        return createdDepartment;
    }


    public Department updateDepartment(BigInteger departmentId, Department department) throws RecordNotFoundException, InternalServerSqlException {
        Department departmentFound = getDepartmentById(departmentId, "Id not found...");
        Department departmentDetailsAfterSet = this.departmentMappers.setNewDetails(department, departmentFound);   //using model mappers

        Department updatedDepartment;
        try {
            updatedDepartment = this.departmentRepository.save(departmentDetailsAfterSet);
        }catch (DataAccessException ex){
            throw new InternalServerSqlException("Failed to save department details...");
        }
        return updatedDepartment;
    }

    //check
    public String deleteDepartment(BigInteger departmentId) throws RecordNotFoundException, InternalServerSqlException{   //check if employees exist in department or not --> 2nd
        //this.employeeService.deleteEmployeesWithDeptId(departmentId);
        try{
            this.departmentRepository.deleteById(departmentId);
            log.info("Department data deleted successfully!!!");
            return "Department data deleted successfully!  " + departmentId.toString();
        }catch(EmptyResultDataAccessException ex){
            throw new RecordNotFoundException("Id does not exist...");
        }
    }

}
