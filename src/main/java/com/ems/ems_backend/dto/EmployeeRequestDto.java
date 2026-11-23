package com.ems.ems_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeRequestDto(
        String firstName,
        String lastName,
        String email,
        String mobileNumber,
        String designation,
        LocalDate joinedAt,
        LocalDate dateOfBirth,
        BigDecimal salary
) {
}
