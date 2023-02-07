package org.paytm.mapper;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.paytm.entities.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    @Autowired
    private ModelMapper modelMapper;
    public Address setNewDetails(Address addressGivenByUser, Address addressInitial) {
        //modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(addressGivenByUser, addressInitial);
        return addressInitial;
    }
}
