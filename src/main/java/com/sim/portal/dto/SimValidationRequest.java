package com.sim.portal.dto;

import static com.sim.portal.exception.Messages.INVALID_SIM_DETAILS;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SimValidationRequest(
        @NotNull(message = INVALID_SIM_DETAILS) @Pattern(regexp = "\\d{13}", message = INVALID_SIM_DETAILS) String simNumber,
        @NotNull(message = INVALID_SIM_DETAILS) @Pattern(regexp = "\\d{10}", message = INVALID_SIM_DETAILS) String serviceNumber) { }
