package com.sim.portal.service;

import com.sim.portal.dto.*;
import com.sim.portal.entity.*;
import com.sim.portal.exception.Messages;
import com.sim.portal.exception.SimPortalException;
import com.sim.portal.repository.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerIdentityRepository identityRepository;
    private final CustomerAddressRepository addressRepository;
    private final SimDetailsRepository simDetailsRepository;

    public CustomerService(
            CustomerRepository customerRepository,
            CustomerIdentityRepository identityRepository,
            CustomerAddressRepository addressRepository,
            SimDetailsRepository simDetailsRepository) {

        this.customerRepository = customerRepository;
        this.identityRepository = identityRepository;
        this.addressRepository = addressRepository;
        this.simDetailsRepository = simDetailsRepository;
    }

    /**
     * Create a customer and save matching identity details.
     */
    @Transactional
    public Customer createCustomer(Customer customer) {

        // Validate unique ID
        if (customer.getUniqueIdNumber() == null
                || customer.getUniqueIdNumber().isBlank()) {

            throw new SimPortalException(
                    "Unique ID number is required",
                    HttpStatus.BAD_REQUEST);
        }

        String uniqueId = customer.getUniqueIdNumber().trim();

        // Check duplicate customer or identity
        if (customerRepository.existsById(uniqueId)
                || identityRepository.existsById(uniqueId)) {

            throw new SimPortalException(
                    "Customer already exists",
                    HttpStatus.CONFLICT);
        }

        // Validate SIM ID
        if (customer.getSimId() == null
                || !simDetailsRepository.existsById(
                        customer.getSimId())) {

            throw new SimPortalException(
                    "Invalid SIM ID",
                    HttpStatus.BAD_REQUEST);
        }

        // Validate required customer details
        if (customer.getFirstName() == null
                || customer.getFirstName().isBlank()
                || customer.getLastName() == null
                || customer.getLastName().isBlank()
                || customer.getEmailAddress() == null
                || customer.getEmailAddress().isBlank()
                || customer.getDateOfBirth() == null
                || customer.getIdType() == null
                || customer.getIdType().isBlank()
                || customer.getState() == null
                || customer.getState().isBlank()) {

            throw new SimPortalException(
                    "Please provide all required customer details",
                    HttpStatus.BAD_REQUEST);
        }

        // Clean customer data
        customer.setUniqueIdNumber(uniqueId);
        customer.setFirstName(customer.getFirstName().trim());
        customer.setLastName(customer.getLastName().trim());
        customer.setEmailAddress(
                customer.getEmailAddress().trim());
        customer.setIdType(customer.getIdType().trim());
        customer.setState(customer.getState().trim());

        // 1. Save customer in Customer table
        Customer savedCustomer =
                customerRepository.save(customer);

        // 2. Create matching identity record
        CustomerIdentity identity = new CustomerIdentity();

        identity.setUniqueIdNumber(
                savedCustomer.getUniqueIdNumber());

        identity.setFirstName(
                savedCustomer.getFirstName());

        identity.setLastName(
                savedCustomer.getLastName());

        identity.setEmailAddress(
                savedCustomer.getEmailAddress());

        identity.setDateOfbirth(
                savedCustomer.getDateOfBirth());

        identity.setState(
                savedCustomer.getState());

        // 3. Save identity in CustomerIdentity table
        identityRepository.save(identity);

        return savedCustomer;
    }

    /**
     * Validate email and date of birth.
     */
    public MessageResponse validateBasicDetails(
            BasicDetailsRequest request) {

        LocalDate dob = parseDate(
                request.dob(),
                Messages.INVALID_DOB_FORMAT);

        customerRepository
                .findByEmailAddressAndDateOfBirth(
                        request.email().trim(), dob)
                .orElseThrow(() -> new SimPortalException(
                        Messages.NO_REQUEST_PLACED,
                        HttpStatus.NOT_FOUND));

        return new MessageResponse(
                "Customer basic details validated successfully");
    }

    /**
     * Validate first name, last name and email.
     */
    public MessageResponse validatePersonalDetails(
            PersonalDetailsRequest request) {

        Customer customer = customerRepository
                .findFirstByFirstNameIgnoreCaseAndLastNameIgnoreCase(
                        request.firstName().trim(),
                        request.lastName().trim())
                .orElseThrow(() -> new SimPortalException(
                        Messages.NO_CUSTOMER_FOUND,
                        HttpStatus.NOT_FOUND));

        if (customer.getEmailAddress() == null
                || !customer.getEmailAddress()
                        .equalsIgnoreCase(
                                request.confirmEmail().trim())) {

            throw new SimPortalException(
                    Messages.INVALID_CONFIRM_EMAIL,
                    HttpStatus.BAD_REQUEST);
        }

        return new MessageResponse(
                "Customer personal details validated successfully");
    }

    /**
     * Save or update customer address.
     */
    @Transactional
    public CustomerAddress saveOrUpdateAddress(
            AddressRequest request) {

        CustomerAddress address =
                request.addressId() == null
                        ? new CustomerAddress()
                        : addressRepository
                                .findById(request.addressId())
                                .orElseGet(CustomerAddress::new);

        address.setAddress(request.address().trim());
        address.setCity(request.city().trim());
        address.setPincode(request.pincode().trim());
        address.setState(request.state().trim());

        CustomerAddress saved =
                addressRepository.save(address);

        if (request.uniqueIdNumber() != null
                && !request.uniqueIdNumber().isBlank()) {

            Customer customer = customerRepository
                    .findById(
                            request.uniqueIdNumber().trim())
                    .orElseThrow(() -> new SimPortalException(
                            Messages.NO_CUSTOMER_FOUND,
                            HttpStatus.NOT_FOUND));

            customer.setCustomerAddress(saved);
            customerRepository.save(customer);
        }

        return saved;
    }

    /**
     * Validate ID proof and activate SIM.
     */
    @Transactional
    public MessageResponse validateIdProofAndActivate(
            IdProofRequest request) {

        String uniqueId = request.uniqueIdNumber().trim();

        // Find identity record
        CustomerIdentity identity = identityRepository
                .findById(uniqueId)
                .orElseThrow(() -> new SimPortalException(
                        Messages.INVALID_DETAILS,
                        HttpStatus.BAD_REQUEST));

        // Find customer record
        Customer customer = customerRepository
                .findById(uniqueId)
                .orElseThrow(() -> new SimPortalException(
                        Messages.INVALID_DETAILS,
                        HttpStatus.BAD_REQUEST));

        String first = request.firstName().trim();
        String last = request.lastName().trim();

        // Validate names against both tables
        boolean namesMatch =
                first.equalsIgnoreCase(identity.getFirstName())
                && last.equalsIgnoreCase(identity.getLastName())
                && first.equalsIgnoreCase(customer.getFirstName())
                && last.equalsIgnoreCase(customer.getLastName());

        if (!namesMatch) {
            throw new SimPortalException(
                    Messages.INVALID_DETAILS,
                    HttpStatus.BAD_REQUEST);
        }

        // Validate date of birth
        LocalDate dob = parseDate(
                request.dateOfBirth(),
                Messages.INCORRECT_DOB);

        if (!dob.equals(customer.getDateOfBirth())
                || !dob.equals(identity.getDateOfbirth())) {

            throw new SimPortalException(
                    Messages.INCORRECT_DOB,
                    HttpStatus.BAD_REQUEST);
        }

        // Find associated SIM
        SimDetails sim = simDetailsRepository
                .findById(customer.getSimId())
                .orElseThrow(() -> new SimPortalException(
                        Messages.INVALID_DETAILS,
                        HttpStatus.BAD_REQUEST));

        // Prevent activating an already active SIM
        if ("active".equalsIgnoreCase(sim.getSimStatus())) {
            throw new SimPortalException(
                    Messages.SIM_ALREADY_ACTIVE,
                    HttpStatus.CONFLICT);
        }

        // Activate SIM
        sim.setSimStatus("active");
        simDetailsRepository.save(sim);

        return new MessageResponse(
                "ID proof validated. SIM activated successfully");
    }

    /**
     * Parse date in yyyy-MM-dd format.
     */
    private LocalDate parseDate(
            String value,
            String errorMessage) {

        try {
            return LocalDate.parse(value.trim());

        } catch (DateTimeParseException
                | NullPointerException ex) {

            throw new SimPortalException(
                    errorMessage,
                    HttpStatus.BAD_REQUEST);
        }
    }
}