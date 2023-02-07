package org.paytm.repository;

import org.paytm.entities.Address;
import org.paytm.exceptions.RecordAlreadyExistsException;
import org.paytm.exceptions.RecordNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface AddressRepository extends JpaRepository<Address, BigInteger> {
    boolean existsByName(String name) throws RecordAlreadyExistsException;
    boolean existsById(BigInteger id) throws RecordNotFoundException;
    //void deleteByEmployeeId(BigInteger employee_id);
}
