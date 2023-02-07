package org.paytm.mapper;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.paytm.dto.DepartmentDto;
import org.paytm.dto.EmployeeDto;
import org.paytm.entities.Department;
import org.paytm.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class EmployeeMapper {

    @Autowired
    private ModelMapper modelMapper;

    @PostConstruct
    protected void onPostConstruct() {
        this.initializeTypeMaps();
    }

    private TypeMap<EmployeeDto, Employee> typeMapDTO;

    private void initializeTypeMaps() {
        log.info("initializing mapper to map DepartmentDto to Department...");
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
                .addMapping(EmployeeDto::getAddressDtoLine1, Employee::setAddress)  //how to set line1, 2, & 3
                .addMapping(EmployeeDto::getAddressDtoLine1, Employee::setAddress)
                .addMapping(EmployeeDto::getAddressDtoLine1, Employee::setAddress)
                .addMapping(EmployeeDto::getDepartmentId, Employee::setDepartment); //how to set 'department_id' specifically
    }

    public Employee fromDtoToEntity(EmployeeDto dto) {
        if (Objects.isNull(dto))
            return null;
        return this.typeMapDTO.map(dto);
    }


    //-----------------------------------------------------------------------------------------------------

    private TypeMap<Employee, EmployeeDto> typeMapDTO2;

    private void initializeTypeMaps2() {
        log.info("initializing mapper to map Department to DepartmentDto...");
        /*Converter<Set<Application>, Set<Long>> converterApplicationsToIDs = ctx -> Objects.isNull(ctx.getSource()) ?
                null :
                ctx.getSource().stream()
                        .filter(Objects::nonNull)
                        .map(dto -> dto.getId())
                        .collect(Collectors.toSet());*/
        this.typeMapDTO2 = this.modelMapper.createTypeMap(Employee.class, EmployeeDto.class)
                .addMapping(Employee::getId, EmployeeDto::setId)
                .addMapping(Employee::getFirstName, EmployeeDto::setFirstName)
                .addMapping(Employee::getMiddleName, EmployeeDto::setMiddleName)
                .addMapping(Employee::getLastName, EmployeeDto::setLastName)
                .addMapping(Employee::getDob, EmployeeDto::setDob)
                .addMapping(Employee::getDoj, EmployeeDto::setDoj)
                .addMapping(Employee::getRemarks, EmployeeDto::setRemarks)
                .addMapping(Employee::getEmail, EmployeeDto::setEmail)
                .addMapping(Employee::getContactNumber, EmployeeDto::setContactNumber); //how to set address and department data??
        //.addMappings(mapper -> mapper.using(converterApplicationsToIDs).map(Offer::getApplications, OfferDto::setApplications));
    }

    public EmployeeDto fromEntityToDto(Employee entity) {
        if (Objects.isNull(entity))
            return null;
        return this.typeMapDTO2.map(entity);
    }

    public List<EmployeeDto> fromEntityToDtoList(List<Employee> employeeList) { //used in employee controller
        List<EmployeeDto> employeeDtoList = employeeList.stream()
                .map(it -> fromEntityToDto(it))
                .collect(Collectors.toList());
        return employeeDtoList;
    }

    public Employee setNewDetails(Employee employeeGivenByUser, Employee employeeInitial) {
        //modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(employeeGivenByUser, employeeInitial);
        return employeeInitial;
    }
}
