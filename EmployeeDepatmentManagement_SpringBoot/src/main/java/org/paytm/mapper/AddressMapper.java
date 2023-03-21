package org.paytm.mapper;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.paytm.dto.AddressDto;
import org.paytm.dto.DepartmentDto;
import org.paytm.entities.Address;
import org.paytm.entities.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Slf4j
@Component
public class AddressMapper {

  @Autowired
  private ModelMapper modelMapper;

  private TypeMap<AddressDto, Address> typeMapDTO;
  private TypeMap<Address, AddressDto> typeMapDTO2;

  @PostConstruct
  protected void onPostConstruct() {
    this.initializeTypeMaps();
  }

  private void initializeTypeMaps() {
    log.info("initializing mapper to map AddressDto to Address...");
    this.typeMapDTO = this.modelMapper.createTypeMap(AddressDto.class, Address.class)
            .addMapping(AddressDto::getId, Address::setId)
            .addMapping(AddressDto::getLine1, Address::setLine1)
            .addMapping(AddressDto::getLine2, Address::setLine2)
            .addMapping(AddressDto::getLine3, Address::setLine3)
            .addMapping(AddressDto::getCity, Address::setCity)
            .addMapping(AddressDto::getState, Address::setState)
            .addMapping(AddressDto::getCountry, Address::setCountry)
            .addMapping(AddressDto::getPinCode, Address::setPinCode)
            .addMapping(AddressDto::getAddressType, Address::setAddressType);

    log.info("initializing mapper to map Address to AddressDto...");
    this.typeMapDTO2 = this.modelMapper.createTypeMap(Address.class, AddressDto.class)
            .addMapping(Address::getId, AddressDto::setId)
            .addMapping(Address::getLine1, AddressDto::setLine1)
            .addMapping(Address::getLine2, AddressDto::setLine2)
            .addMapping(Address::getLine3, AddressDto::setLine3)
            .addMapping(Address::getCity, AddressDto::setCity)
            .addMapping(Address::getState, AddressDto::setState)
            .addMapping(Address::getCountry, AddressDto::setCountry)
            .addMapping(Address::getPinCode, AddressDto::setPinCode)
            .addMapping(Address::getAddressType, AddressDto::setAddressType);
  }

  public Address fromDtoToEntity(AddressDto dto) {
    if (Objects.isNull(dto)) {
      return null;
    }
    Address entity = this.typeMapDTO.map(dto);
    return entity;
  }

  public AddressDto fromEntityToDto(Address entity) {
    if (Objects.isNull(entity)) {
      return null;
    }
    return this.typeMapDTO2.map(entity);
  }

  public Address setNewDetails(Address addressGivenByUser, Address addressInitial) {
    //modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    modelMapper.map(addressGivenByUser, addressInitial);
    return addressInitial;
  }
}
