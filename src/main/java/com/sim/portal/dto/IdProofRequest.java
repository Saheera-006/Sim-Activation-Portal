package com.sim.portal.dto;

import static com.sim.portal.exception.Messages.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record IdProofRequest(
        @NotBlank(message = ALL_FIELDS_MANDATORY) @Pattern(regexp = "^\\s*$|^\\d{16}$", message = INVALID_ID) String uniqueIdNumber,
        @NotBlank(message = ALL_FIELDS_MANDATORY) String firstName,
        @NotBlank(message = ALL_FIELDS_MANDATORY) String lastName,
        @NotBlank(message = ALL_FIELDS_MANDATORY) @Pattern(regexp = "^\\s*$|^\\d{4}-\\d{2}-\\d{2}$", message = INCORRECT_DOB) String dateOfBirth) { }
