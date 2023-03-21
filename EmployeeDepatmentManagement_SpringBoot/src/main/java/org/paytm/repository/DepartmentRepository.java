package org.paytm.repository;

import java.math.BigInteger;
import org.paytm.entities.Department;
import org.paytm.exceptions.RecordAlreadyExistsException;
import org.paytm.exceptions.RecordNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, BigInteger> {

  boolean existsByName(String name) throws RecordAlreadyExistsException;

  boolean existsById(BigInteger id) throws RecordNotFoundException;
}
