package com.sim.portal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public record CreateCustomerRequest(
    @NotBlank String uniqueIdNumber,
    @NotBlank String firstName,
    @NotBlank String lastName,
    @Email @NotBlank String emailAddress,
    @NotNull LocalDate dateOfBirth,
    @NotBlank String idType,
    @NotBlank String address,
    @NotBlank String city,
    @NotBlank @Pattern(regexp="\\d{6}", message="Pincode must contain exactly 6 digits") String pincode,
    @NotBlank String state,
    @NotNull Integer simId
) {}
