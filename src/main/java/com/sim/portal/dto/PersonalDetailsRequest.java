package com.sim.portal.dto;

import static com.sim.portal.exception.Messages.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PersonalDetailsRequest(
        @NotBlank(message = INVALID_NAME) @Pattern(regexp = "^\\s*$|^[A-Za-z]{1,15}$", message = INVALID_NAME) String firstName,
        @NotBlank(message = INVALID_NAME) @Pattern(regexp = "^\\s*$|^[A-Za-z]{1,15}$", message = INVALID_NAME) String lastName,
        @NotBlank(message = INVALID_CONFIRM_EMAIL) String confirmEmail) { }
