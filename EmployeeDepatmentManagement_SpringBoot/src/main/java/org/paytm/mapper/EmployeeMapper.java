package org.paytm.mapper;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.paytm.dto.AddressDto;
import org.paytm.dto.EmployeeCsvDto;
import org.paytm.dto.EmployeeDto;
import org.paytm.entities.Address;
import org.paytm.entities.Department;
import org.paytm.entities.Employee;
import org.paytm.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmployeeMapper {

  @Autowired
  private ModelMapper modelMapper;
  private TypeMap<EmployeeDto, Employee> typeMapDTO;
  private TypeMap<Employee, EmployeeDto> typeMapDTO2;
  private TypeMap<EmployeeCsvDto, EmployeeDto> typeMapDTO3;

  @PostConstruct
  protected void onPostConstruct() {
    this.initializeTypeMaps();
  }

  @Lazy
  @Autowired
  private DepartmentService departmentService;

  @Autowired
  private AddressMapper addressMapper;

  private void initializeTypeMaps() {
    log.info("initializing mapper to map EmployeeDto to Employee...");
    Converter<BigInteger, Department> convertIdToDepartment =
            ctx -> this.departmentService.getDepartmentById(ctx.getSource());
    Converter<AddressDto, Address> convertAddressDtoToAddress =
            ctx -> this.addressMapper.fromDtoToEntity(ctx.getSource());

    this.typeMapDTO = this.modelMapper.createTypeMap(EmployeeDto.class, Employee.class)
            .addMapping(EmployeeDto::getId, Employee::setId)
            .addMapping(EmployeeDto::getFirstName, Employee::setFirstName)
            .addMapping(EmployeeDto::getMiddleName, Employee::setMiddleName)
            .addMapping(EmployeeDto::getLastName, Employee::setLastName)
            .addMapping(EmployeeDto::getDob, Employee::setDob)
            .addMapping(EmployeeDto::getDoj, Employee::setDoj)
            .addMapping(EmployeeDto::getRemarks, Employee::setRemarks)
            .addMapping(EmployeeDto::getEmail, Employee::setEmail)
            .addMapping(EmployeeDto::getContactNumber, Employee::setContactNumber)
            .addMapping(EmployeeDto::getCreatedAt, Employee::setCreatedAt)
            .addMapping(EmployeeDto::getUpdatedAt, Employee::setUpdatedAt)
            .addMappings(mapper -> mapper.using(convertAddressDtoToAddress).map(EmployeeDto::getAddressDto, Employee::setAddress))
            .addMappings(mapper -> mapper.using(convertIdToDepartment).map(EmployeeDto::getDepartmentId, Employee::setDepartment));

    log.info("initializing mapper to map Employee to EmployeeDto...");
    Converter<Department, BigInteger> convertDepartmentToId =
            ctx -> Objects.isNull(ctx.getSource()) ? null : ctx.getSource().getId();
    Converter<Address, AddressDto> convertAddressToAddressDto =
            ctx -> this.addressMapper.fromEntityToDto(ctx.getSource());

    this.typeMapDTO2 = this.modelMapper.createTypeMap(Employee.class, EmployeeDto.class)
            .addMapping(Employee::getId, EmployeeDto::setId)
            .addMapping(Employee::getFirstName, EmployeeDto::setFirstName)
            .addMapping(Employee::getMiddleName, EmployeeDto::setMiddleName)
            .addMapping(Employee::getLastName, EmployeeDto::setLastName)
            .addMapping(Employee::getDob, EmployeeDto::setDob)
            .addMapping(Employee::getDoj, EmployeeDto::setDoj)
            .addMapping(Employee::getRemarks, EmployeeDto::setRemarks)
            .addMapping(Employee::getEmail, EmployeeDto::setEmail)
            .addMapping(Employee::getCreatedAt, EmployeeDto::setCreatedAt)
            .addMapping(Employee::getUpdatedAt, EmployeeDto::setUpdatedAt)
            .addMapping(Employee::getContactNumber, EmployeeDto::setContactNumber)
            .addMappings(mapper -> mapper.using(convertAddressToAddressDto).map(Employee::getAddress, EmployeeDto::setAddressDto))
            .addMappings(mapper -> mapper.using(convertDepartmentToId).map(Employee::getDepartment, EmployeeDto::setDepartmentId));

    log.info("initializing mapper to map EmployeeCsvDto to Department...");
    this.typeMapDTO3 = this.modelMapper.createTypeMap(EmployeeCsvDto.class, EmployeeDto.class)
            .addMapping(EmployeeCsvDto::getId, EmployeeDto::setId)
            .addMapping(EmployeeCsvDto::getFirstName, EmployeeDto::setFirstName)
            .addMapping(EmployeeCsvDto::getMiddleName, EmployeeDto::setMiddleName)
            .addMapping(EmployeeCsvDto::getLastName, EmployeeDto::setLastName)
            .addMapping(EmployeeCsvDto::getDob, EmployeeDto::setDob)
            .addMapping(EmployeeCsvDto::getDoj, EmployeeDto::setDoj)
            .addMapping(EmployeeCsvDto::getRemarks, EmployeeDto::setRemarks)
            .addMapping(EmployeeCsvDto::getEmail, EmployeeDto::setEmail)
            .addMapping(EmployeeCsvDto::getContactNumber, EmployeeDto::setContactNumber)
            .addMapping(EmployeeCsvDto::getCreatedAt, EmployeeDto::setCreatedAt)
            .addMapping(EmployeeCsvDto::getUpdatedAt, EmployeeDto::setUpdatedAt)
            .addMapping(EmployeeCsvDto::getAddressDto, EmployeeDto::setAddressDto)
            .addMapping(EmployeeCsvDto::getDepartmentId, EmployeeDto::setDepartmentId);
  }

  //-----------------------------------------------------------------------------------------------------

  public Employee fromDtoToEntity(EmployeeDto dto) {
    if (Objects.isNull(dto)) {
      return null;
    }
    return this.typeMapDTO.map(dto);
  }

  public EmployeeDto fromEntityToDto(Employee entity) {
    if (Objects.isNull(entity)) {
      return null;
    }
    return this.typeMapDTO2.map(entity);
  }

  public List<EmployeeDto> fromEntityToDtoList(
      List<Employee> employeeList) { //used in employee controller
    List<EmployeeDto> employeeDtoList = employeeList.stream()
        .map(it -> fromEntityToDto(it))
        .collect(Collectors.toList());
    return employeeDtoList;
  }

  public EmployeeDto fromEmployeeCsvDtoToEmployeeDto(EmployeeCsvDto employeeCsvDto) {
    if (Objects.isNull(employeeCsvDto)) {
      return null;
    }
    return this.typeMapDTO3.map(employeeCsvDto);
  }

  public Employee setNewDetails(Employee employeeGivenByUser, Employee employeeInitial) {
    //modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    modelMapper.map(employeeGivenByUser, employeeInitial);
    return employeeInitial;
  }
}
