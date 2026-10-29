package com.sim.portal.repository;

import com.sim.portal.entity.Customer;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByEmailAddressAndDateOfBirth(String emailAddress, LocalDate dateOfBirth);
    Optional<Customer> findFirstByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
}
