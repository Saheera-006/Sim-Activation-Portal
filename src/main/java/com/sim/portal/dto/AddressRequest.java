package com.sim.portal.dto;

import static com.sim.portal.exception.Messages.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        Integer addressId,
        String uniqueIdNumber,

        @NotBlank(message = ADDRESS_REQUIRED)
        @Size(max = 25, message = ADDRESS_TOO_LONG)
        String address,

        @NotBlank(message = INVALID_CITY_STATE)
        @Pattern(regexp = "^[A-Za-z ]+$", message = INVALID_CITY_STATE)
        String city,

        @NotBlank(message = INVALID_PIN)
        @Pattern(regexp = "^\\d{6}$", message = INVALID_PIN)
        String pincode,

        @NotBlank(message = INVALID_CITY_STATE)
        @Pattern(regexp = "^[A-Za-z ]+$", message = INVALID_CITY_STATE)
        String state
) {}