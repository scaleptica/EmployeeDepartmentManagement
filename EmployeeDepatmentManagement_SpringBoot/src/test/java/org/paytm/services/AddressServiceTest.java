package org.paytm.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.paytm.entities.Address;
import org.paytm.entities.Department;
import org.paytm.entities.Employee;
import org.paytm.repository.AddressRepository;
import org.paytm.repository.DepartmentRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {   //config, controller, mapper, services, mainApp require junit tests


    @MockBean
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    @BeforeEach
    void setup(){

        Address REC1 = new Address();
        REC1.setId(BigInteger.valueOf(1));
        REC1.setLine1("A42 Suvidha Bunglows");
        REC1.setCity("Mehsana");
        REC1.setState("Gujarat");
        REC1.setPinCode(382424);
        REC1.setCountry("India");
        REC1.setAddressType("OFFICE");

        Address REC2 = new Address();
        REC2.setId(BigInteger.valueOf(2));
        REC2.setLine1("350 Victoria Street");
        REC2.setCity("Toronto");
        REC2.setState("Ontario");
        REC2.setPinCode(876565);
        REC2.setCountry("Canada");
        REC2.setAddressType("HOME");

        List<Address> records = new ArrayList<>(Arrays.asList(REC1, REC2));

        when(this.addressRepository.findAll()).thenReturn(records);

    }

    @Test
    public void getAllAddressTest_success(){

        assertEquals(2, this.addressService.getAllAddresss().size());
    }

    @Test
    public void getAddressByIdTest_success() {
        String address_line1 = "350 Victoria Street";

        Address REC2 = new Address();
        REC2.setId(BigInteger.valueOf(2));
        REC2.setLine1("350 Victoria Street");
        REC2.setCity("Toronto");
        REC2.setState("Ontario");
        REC2.setPinCode(876565);
        REC2.setCountry("Canada");
        REC2.setAddressType("HOME");

        when(this.addressRepository.findById(BigInteger.valueOf(2)))
                .thenReturn(Optional.of(REC2));
        Address addressById = this.addressService.getAddressWithId(BigInteger.valueOf(2));
        assertEquals(address_line1, addressById.getLine1());
    }

    @Test
    public void addAddressTest_success() {
        Address REC2 = new Address();
        REC2.setId(BigInteger.valueOf(2));
        REC2.setLine1("350 Victoria Street");
        REC2.setCity("Toronto");
        REC2.setState("Ontario");
        REC2.setPinCode(876565);
        REC2.setCountry("Canada");
        REC2.setAddressType("HOME");

        when(this.addressRepository.save(REC2)).thenReturn(REC2);
        assertEquals(REC2, this.addressService.addAddress(REC2));
    }

    @Test
    public void deleteAddressTest() {
        Address REC2 = new Address();
        REC2.setId(BigInteger.valueOf(2));
        REC2.setLine1("350 Victoria Street");
        REC2.setCity("Toronto");
        REC2.setState("Ontario");
        REC2.setPinCode(876565);
        REC2.setCountry("Canada");
        REC2.setAddressType("HOME");

        this.addressService.deleteAddress(BigInteger.valueOf(2));
        Mockito.verify(addressRepository).deleteById(BigInteger.valueOf(2));
        //checking if the delete method from the repository has been called once or not
    }

}
