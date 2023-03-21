package org.paytm.entities;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "employee")
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private BigInteger id;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "middle_name")
  private String middleName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "dob", columnDefinition = "DATE", nullable = false)
  private LocalDate dob;

  @Column(name = "doj", columnDefinition = "DATE", nullable = false)
  private LocalDate doj;

  @Column(name = "remarks")
  private String remarks;

  @Column(name = "email", length = 50, nullable = false, unique = true)
  private String email;

  @Column(name = "contact_number", length = 11, nullable = false, unique = true)
  private String contactNumber;

  @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
  private LocalDateTime updatedAt;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "department_id")
  private Department department;  //many employees-->one department

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "address_id")
  private Address address;    //many employees-->one address

}
