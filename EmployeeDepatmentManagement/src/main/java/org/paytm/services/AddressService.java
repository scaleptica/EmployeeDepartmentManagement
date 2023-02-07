package org.paytm.services;

import org.paytm.MainApp;
import org.paytm.entities.Address;
import org.paytm.entities.Address;
import org.paytm.entities.Department;
import org.paytm.exceptions.InternalServerSqlException;
import org.paytm.exceptions.RecordAlreadyExistsException;
import org.paytm.exceptions.RecordNotFoundException;
import org.paytm.mapper.AddressMapper;
import org.paytm.repository.AddressRepository;
import org.paytm.repository.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressMapper addressMapper;

    private static final Logger log = LoggerFactory.getLogger(AddressService.class);

    //left
    public List<Address> getAllAddresss(){
        List<Address> addressList = this.addressRepository.findAll();
        return addressList;
    }


    public Address getAddressWithId(BigInteger addressId) throws RecordNotFoundException {
        Address addressReceived = identityFinder(addressId, "Id not found...");
        return addressReceived;
    }

    public Address addAddress(Address address) throws RecordAlreadyExistsException, InternalServerSqlException {  //create

        try {
            if(!this.addressRepository.existsByName(address.getLine1())){  //exists by does not throw exception, it throws boolean only
                throw new RecordNotFoundException("Id does not exist");}
        } catch(RuntimeException ex) {
            throw ex;
        }

        Address createdAddress;
        try {
            createdAddress = this.addressRepository.save(address); //save always throws DataAccessException
        }catch (DataAccessException ex){
            throw new InternalServerSqlException("Failed to save address details...");
        }
        return createdAddress;
    }


    public Address updateAddress(BigInteger addressId, Address address) throws RecordNotFoundException, InternalServerSqlException {
        Address addressFound = identityFinder(addressId, "Id not found...");
        Address addressDetailsAfterSet = this.addressMapper.setNewDetails(address, addressFound);   //use model mappers
        Address updatedAddress = null;  //always initialize
        try {
            updatedAddress = this.addressRepository.save(addressDetailsAfterSet);
        }catch (DataAccessException ex){
            throw new InternalServerSqlException("Failed to save address details...");
        }
        return updatedAddress;
    }

    public void deleteAddress(BigInteger addressId) throws RecordNotFoundException {   //check if address exist in address or not --> 2nd
        try {
            this.addressRepository.deleteById(addressId);
            System.out.println("Data deleted successfully!!!");
        } catch (EmptyResultDataAccessException ex) {
            throw new RecordNotFoundException("Id does not exist...");
        }
    }

    public Address identityFinder(BigInteger idCode, String replyMessage)throws RecordNotFoundException{
        Address foundInDb = this.addressRepository.findById(idCode)    //redundant  in line 37, 52
                .orElseThrow(() -> new RecordNotFoundException(replyMessage));
        return foundInDb;
    }

//instead of employee_id, shouldn't we check address_id,as connection of address with employee_id seems not possible
    /*public void deleteAddressWithEmployeeId(BigInteger employee_id) throws InternalServerSqlException{
        try{
            this.addressRepository.deleteByEmployeeId(employee_id);    //deletes all entries with the given emp_id
        }catch (DataAccessException ex){
            log.error("Could not be deleted...");
            throw new InternalServerSqlException("Employee Deletion via department id failed...");
        }
    }*/
}
