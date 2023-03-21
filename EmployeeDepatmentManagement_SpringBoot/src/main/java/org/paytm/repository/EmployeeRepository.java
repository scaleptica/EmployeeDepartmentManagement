package org.paytm.repository;

import java.math.BigInteger;
import java.util.List;
import org.paytm.entities.Employee;
import org.paytm.exceptions.RecordNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, BigInteger> {

  boolean existsByEmailAndContactNumber(String email, String number);

  boolean existsByDepartmentId(BigInteger department_id);

  boolean existsById(BigInteger id) throws RecordNotFoundException;

  List<Employee> findByDepartmentId(BigInteger department_id);

}