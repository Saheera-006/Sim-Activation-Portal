package com.sim.portal.dto;

import com.sim.portal.exception.Messages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateSimRequest(
        @NotBlank(message = Messages.INVALID_SIM_DETAILS)
        @Pattern(regexp = "\\d{13}", message = Messages.INVALID_SIM_DETAILS)
        String simNumber,
        @NotBlank(message = Messages.INVALID_SIM_DETAILS)
        @Pattern(regexp = "\\d{10}", message = Messages.INVALID_SIM_DETAILS)
        String serviceNumber,
        @Pattern(regexp = "(?i)active|inactive", message = "SIM status must be active or inactive")
        String simStatus) {
}
