package org.paytm.repository;

import java.math.BigInteger;
import org.paytm.entities.Address;
import org.paytm.exceptions.RecordNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, BigInteger> {

  boolean existsById(BigInteger id) throws RecordNotFoundException;
  //void deleteByEmployeeId(BigInteger employee_id);
}
