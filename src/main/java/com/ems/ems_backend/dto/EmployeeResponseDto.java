package com.ems.ems_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmployeeResponseDto(

        Long id,
        String firstName,
        String lastName,
        String email,
        String mobileNumber,
        String designation,
        LocalDate joinedAt,
        LocalDate dateOfBirth,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        BigDecimal salary

) { }
