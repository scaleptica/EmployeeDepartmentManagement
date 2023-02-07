package org.paytm.controllers;

import org.paytm.dto.DepartmentDto;
import org.paytm.entities.Department;
import org.paytm.exceptions.BadRequestException;
import org.paytm.exceptions.InternalServerSqlException;
import org.paytm.exceptions.RecordAlreadyExistsException;
import org.paytm.exceptions.RecordNotFoundException;
import org.paytm.mapper.DepartmentMapper;
import org.paytm.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentMapper departmentMapper;

    ///list?pagenumber={}&pagesize={}
    @GetMapping(value = "/list")
    public List<DepartmentDto> getAllDepartments(  //lookup search indexes as well (REFER TO GIT CODE)
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize
    ){
        List<Department> departmentList = this.departmentService.getAllDepartments(pageNumber, pageSize);
        List<DepartmentDto> departmentDtoList = this.departmentMapper.fromEntityToDtoList(departmentList);
        return departmentDtoList;
    }

    @GetMapping(value = "/{departmentId}")
    public DepartmentDto getDepartmentWithId(@PathVariable BigInteger departmentId) throws RecordNotFoundException{
        Department departmentById = this.departmentService.getDepartmentWithId(departmentId);
        DepartmentDto departmentByIdDto = this.departmentMapper.fromEntityToDto(departmentById);
        return departmentByIdDto;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public DepartmentDto addDepartment( @RequestBody DepartmentDto departmentDto) throws BadRequestException, RecordAlreadyExistsException, InternalServerSqlException {
        try {
            if(Objects.isNull(departmentDto.getName())){
                throw new BadRequestException("Name not provided...");}
        } catch(RuntimeException ex) {
            throw ex;
        }
        Department departmentToBeAdded = this.departmentMapper.fromDtoToEntity(departmentDto);
        Department addedDepartment = this.departmentService.addDepartment(departmentToBeAdded);
        DepartmentDto addedDepartmentDto = this.departmentMapper.fromEntityToDto(addedDepartment);
        return addedDepartmentDto;
    }

    @PutMapping(value = "/{departmentId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DepartmentDto updateDepartment( @RequestBody DepartmentDto departmentDto, @PathVariable BigInteger departmentId) throws BadRequestException, RecordNotFoundException, InternalServerSqlException {
        try {
            if(Objects.isNull(departmentDto.getName())){
                throw new BadRequestException("Name not provided...");}
        } catch(RuntimeException ex) {
            throw ex;
        }
        Department departmentToBeUpdated = this.departmentMapper.fromDtoToEntity(departmentDto);
        Department updatedDepartment = this.departmentService.updateDepartment(departmentId, departmentToBeUpdated);
        DepartmentDto updatedDepartmentDto = this.departmentMapper.fromEntityToDto(updatedDepartment);
        return updatedDepartmentDto;
    }

    @DeleteMapping(value = "/{departmentId}")
    public String deleteDepartment(@PathVariable BigInteger departmentId) throws RecordNotFoundException{
        return this.departmentService.deleteDepartment(departmentId);
    }
}
