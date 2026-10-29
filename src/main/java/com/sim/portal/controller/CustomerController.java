package com.sim.portal.controller;
import com.sim.portal.entity.Customer;
import com.sim.portal.dto.*;
import com.sim.portal.entity.CustomerAddress;
import com.sim.portal.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/basic-validation")
    public ResponseEntity<MessageResponse> validateBasic(@Valid @RequestBody BasicDetailsRequest request) {
        return ResponseEntity.ok(customerService.validateBasicDetails(request));
    }

    @PostMapping("/personal-validation")
    public ResponseEntity<MessageResponse> validatePersonal(@Valid @RequestBody PersonalDetailsRequest request) {
        return ResponseEntity.ok(customerService.validatePersonalDetails(request));
    }

    @PutMapping("/address")
    public ResponseEntity<CustomerAddress> updateAddress(@Valid @RequestBody AddressRequest request) {
        return ResponseEntity.ok(customerService.saveOrUpdateAddress(request));
    }

    @PostMapping("/id-validation")
    public ResponseEntity<MessageResponse> validateIdProof(@Valid @RequestBody IdProofRequest request) {
        return ResponseEntity.ok(customerService.validateIdProofAndActivate(request));
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(
            @Valid @RequestBody Customer customer) {
        return ResponseEntity.status(201)
                .body(customerService.createCustomer(customer));
    }

}
