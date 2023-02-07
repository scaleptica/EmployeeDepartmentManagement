package org.paytm.entities;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private BigInteger id;

    @Column(name = "first_name", columnDefinition = "NOT NULL")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name", columnDefinition = "NOT NULL")
    private String lastName;

    @Column(name = "dob", columnDefinition = "NOT NULL")
    private LocalDate dob;

    @Column(name = "doj", columnDefinition = "NOT NULL")
    private LocalDate doj;

    @Column(name = "remarks")
    private String remarks;

    //@Email
    @Column(name = "email", length = 50, nullable = false, unique = true, columnDefinition = "NOT NULL")
    private String email;

    @Column(name = "contact_number", length = 11, nullable = false, unique = true, columnDefinition = "NOT NULL")
    private String contactNumber;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;  //many employees-->one department

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id") //should be one way
    private Address address;    //many employees-->one address

}
