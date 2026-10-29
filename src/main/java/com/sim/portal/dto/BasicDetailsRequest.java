package com.sim.portal.dto;

import static com.sim.portal.exception.Messages.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record BasicDetailsRequest(
        @NotBlank(message = EMAIL_DOB_REQUIRED)
        @Pattern(regexp = "^\\s*$|^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,3}$", message = INVALID_EMAIL) String email,
        @NotBlank(message = EMAIL_DOB_REQUIRED)
        @Pattern(regexp = "^\\s*$|^\\d{4}-\\d{2}-\\d{2}$", message = INVALID_DOB_FORMAT) String dob) { }
