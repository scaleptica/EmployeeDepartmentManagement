package org.paytm.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.paytm.dto.AddressDto;
import org.paytm.entities.Address;
import org.paytm.services.AddressService;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AddressMapperTest {

    @Mock
    private TypeMap<AddressDto, Address> typeMapDTO;

    @Mock
    private TypeMap<Address, AddressDto> typeMapDTO2;

    @InjectMocks
    private AddressMapper addressMapper;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void fromDtoToEntityTest_success() {

        Address REC1 = new Address();
        REC1.setId(BigInteger.valueOf(1));
        REC1.setLine1("A42 Suvidha Bunglows");
        REC1.setCity("Mehsana");
        REC1.setState("Gujarat");
        REC1.setPinCode(382424);
        REC1.setCountry("India");
        REC1.setAddressType("OFFICE");

        AddressDto REC1dto = new AddressDto();
        REC1dto.setId(BigInteger.valueOf(1));
        REC1dto.setLine1("A42 Suvidha Bunglows");
        REC1dto.setCity("Mehsana");
        REC1dto.setState("Gujarat");
        REC1dto.setPinCode(382424);
        REC1dto.setCountry("India");
        REC1dto.setAddressType("OFFICE");

        when(this.typeMapDTO.map(REC1dto)).thenReturn(REC1);

        assertEquals(REC1, this.addressMapper.fromDtoToEntity(REC1dto));
    }

    @Test
    public void fromEntityToDtoTest_success() {

        Address REC1 = new Address();
        REC1.setId(BigInteger.valueOf(1));
        REC1.setLine1("A42 Suvidha Bunglows");
        REC1.setCity("Mehsana");
        REC1.setState("Gujarat");
        REC1.setPinCode(382424);
        REC1.setCountry("India");
        REC1.setAddressType("OFFICE");

        AddressDto REC1dto = new AddressDto();
        REC1dto.setId(BigInteger.valueOf(1));
        REC1dto.setLine1("A42 Suvidha Bunglows");
        REC1dto.setCity("Mehsana");
        REC1dto.setState("Gujarat");
        REC1dto.setPinCode(382424);
        REC1dto.setCountry("India");
        REC1dto.setAddressType("OFFICE");

        when(this.typeMapDTO2.map(REC1)).thenReturn(REC1dto);

        assertEquals(REC1dto, this.addressMapper.fromEntityToDto(REC1));

    }
}
