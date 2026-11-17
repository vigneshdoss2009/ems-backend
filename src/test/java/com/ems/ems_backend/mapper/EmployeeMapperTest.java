package com.ems.ems_backend.mapper;

import com.ems.ems_backend.dto.EmployeeRequestDto;
import com.ems.ems_backend.dto.EmployeeResponseDto;
import com.ems.ems_backend.entity.Employee;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeMapperTest {

    @Test
    void toEntity_copiesAllFields_andLeavesServerFieldsEmpty() {
        // Arrange: input with a DIFFERENT value in every field
        EmployeeRequestDto dto = new EmployeeRequestDto(
                "Arun",                         // firstName
                "Kumar",                        // lastName
                "arun@example.com",             // email
                "+919876543210",                // mobileNumber
                "Java Developer",               // designation
                LocalDate.of(2024, 1, 15),      // joinedAt
                LocalDate.of(1998, 5, 10),      // dateOfBirth
                new BigDecimal("45000.00"));    // salary

        // Act
        Employee e = EmployeeMapper.toEntity(dto);

        // Assert
        assertEquals("Arun", e.getFirstName());
        assertEquals("Kumar", e.getLastName());
        assertEquals("arun@example.com", e.getEmail());
        assertEquals("+919876543210", e.getMobileNumber());
        assertEquals("Java Developer", e.getDesignation());
        assertEquals(LocalDate.of(2024, 1, 15), e.getJoinedAt());
        assertEquals(LocalDate.of(1998, 5, 10), e.getDateOfBirth());
        assertEquals(new BigDecimal("45000.00"), e.getSalary());

        assertNull(e.getId());
        assertNull(e.getCreatedAt());
        assertNull(e.getUpdatedAt());
    }

    @Test
    void toResponse_copiesAllFields() {
        // Arrange
        Employee e = new Employee();
        e.setId(1L);
        e.setFirstName("Arun");
        e.setLastName("Kumar");
        e.setEmail("arun@example.com");
        e.setMobileNumber("+919876543210");
        e.setDesignation("Java Developer");
        e.setJoinedAt(LocalDate.of(2024, 1, 15));
        e.setDateOfBirth(LocalDate.of(1998, 5, 10));
        e.setSalary(new BigDecimal("45000.00"));
        e.setCreatedAt(LocalDateTime.of(2026, 11, 17, 10, 0));
        e.setUpdatedAt(LocalDateTime.of(2026, 11, 17, 11, 30));

        // Act
        EmployeeResponseDto r = EmployeeMapper.toResponse(e);

        // Assert
        assertEquals(1L, r.id());
        assertEquals("Arun", r.firstName());
        assertEquals("Kumar", r.lastName());
        assertEquals("arun@example.com", r.email());
        assertEquals("+919876543210", r.mobileNumber());
        assertEquals("Java Developer", r.designation());
        assertEquals(LocalDate.of(2024, 1, 15), r.joinedAt());
        assertEquals(LocalDate.of(1998, 5, 10), r.dateOfBirth());
        assertEquals(new BigDecimal("45000.00"), r.salary());
        assertEquals(LocalDateTime.of(2026, 11, 17, 10, 0), r.createdAt());
        assertEquals(LocalDateTime.of(2026, 11, 17, 11, 30), r.updatedAt());
    }


    @Test
    void nullInput_returnsNull() {
        assertNull(EmployeeMapper.toEntity(null));
        assertNull(EmployeeMapper.toResponse(null));
    }
}