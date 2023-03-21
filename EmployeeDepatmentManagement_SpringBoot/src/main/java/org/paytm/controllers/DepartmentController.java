package org.paytm.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.paytm.dto.DepartmentCsvDto;
import org.paytm.dto.DepartmentDto;
import org.paytm.entities.Department;
import org.paytm.exceptions.BadRequestException;
import org.paytm.exceptions.InternalServerSqlException;
import org.paytm.exceptions.RecordAlreadyExistsException;
import org.paytm.exceptions.RecordNotFoundException;
import org.paytm.mapper.DepartmentMapper;
import org.paytm.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(value = "/department")
public class DepartmentController {

  @Autowired
  private DepartmentService departmentService;

  //@Autowired
  @Autowired(required=true)
  private DepartmentMapper departmentMapper;

  ///list?pagenumber={}&pagesize={}
  @GetMapping(value = "/list")
  public List<DepartmentDto> getAllDepartments(  //lookup search indexes as well (REFER TO GIT CODE)
      @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
      @RequestParam(value = "pageSize", required = false) Integer pageSize
  ) {
    List<Department> departmentList = this.departmentService.getAllDepartments(pageNumber,
        pageSize);
    List<DepartmentDto> departmentDtoList = this.departmentMapper.fromEntityToDtoList(
        departmentList);
    return departmentDtoList;
  }

  @Cacheable(value = "DEPARTMENT", key = "#departmentId", unless = "#result == null")
  @GetMapping(value = "/{departmentId}")
  public DepartmentDto getDepartmentWithId(@PathVariable BigInteger departmentId)
      throws RecordNotFoundException {
    Department departmentById = this.departmentService.getDepartmentWithId(departmentId);
    DepartmentDto departmentByIdDto = this.departmentMapper.fromEntityToDto(departmentById);
    return departmentByIdDto;
  }

  @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
  public DepartmentDto addDepartment(@RequestBody DepartmentDto departmentDto)
      throws BadRequestException, RecordAlreadyExistsException, InternalServerSqlException {
    try {
      if (Objects.isNull(departmentDto.getName())) {
        throw new BadRequestException("Name not provided...");
      }
    } catch (RuntimeException ex) {
      throw ex;
    }
    Department departmentToBeAdded = this.departmentMapper.fromDtoToEntity(departmentDto);
    Department addedDepartment = this.departmentService.addDepartment(departmentToBeAdded);
    DepartmentDto addedDepartmentDto = this.departmentMapper.fromEntityToDto(addedDepartment);
    return addedDepartmentDto;
  }

  @PutMapping(value = "/{departmentId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public DepartmentDto updateDepartment(@RequestBody DepartmentDto departmentDto,
      @PathVariable BigInteger departmentId)
      throws BadRequestException, RecordNotFoundException, InternalServerSqlException {
    try {
      if (Objects.isNull(departmentDto.getName())) {
        throw new BadRequestException("Name not provided...");
      }
    } catch (RuntimeException ex) {
      throw ex;
    }
    Department departmentToBeUpdated = this.departmentMapper.fromDtoToEntity(departmentDto);
    Department updatedDepartment = this.departmentService.updateDepartment(departmentId,
        departmentToBeUpdated);
    DepartmentDto updatedDepartmentDto = this.departmentMapper.fromEntityToDto(updatedDepartment);
    return updatedDepartmentDto;
  }

  @DeleteMapping(value = "/{departmentId}")
  public String deleteDepartment(@PathVariable BigInteger departmentId)
      throws RecordNotFoundException {
    return this.departmentService.deleteDepartment(departmentId);
  }


  @PostMapping(value = "/read/file")
  public Map<String, Object> migrateData(@Validated @RequestParam("file") MultipartFile file) {
    return this.departmentService.csvDataMigration(file);
  }

//  @PostMapping(value = "/produce/kafka/file")
//  public Map<String, Object> migrateDataViaKafka(@Validated @RequestParam("file") MultipartFile file) {
//    Map<String, Object> map = new HashMap<>();
//    map = this.departmentService.kafkaCsvMigration(file, map);
//    return map;
//  }

}
