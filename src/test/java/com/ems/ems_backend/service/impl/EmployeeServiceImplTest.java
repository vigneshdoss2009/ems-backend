package com.ems.ems_backend.service.impl;

import com.ems.ems_backend.dto.EmployeeRequestDto;
import com.ems.ems_backend.dto.EmployeeResponseDto;
import com.ems.ems_backend.entity.Employee;
import com.ems.ems_backend.exception.DuplicateResourceException;
import com.ems.ems_backend.exception.ResourceNotFoundException;
import com.ems.ems_backend.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private EmployeeRequestDto request() {
        return new EmployeeRequestDto(
                "Arun", "Kumar", "arun@example.com", "+919876543210", "Java Developer",
                LocalDate.of(2024, 1, 15), LocalDate.of(1998, 5, 10), new BigDecimal("45000.00"));
    }

    @Test
    void createEmployee_success() {
        when(employeeRepository.existsByEmail("arun@example.com")).thenReturn(false);
        when(employeeRepository.existsByMobileNumber("+919876543210")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee e = invocation.getArgument(0);
            e.setId(1L);                                  // pretend MySQL generated the id
            return e;
        });

        EmployeeResponseDto response = employeeService.createEmployee(request());

        assertEquals(1L, response.id());
        assertEquals("Arun", response.firstName());
        assertEquals("arun@example.com", response.email());

        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(captor.capture());
        assertEquals("Kumar", captor.getValue().getLastName());   // what the service sent to save()
    }

    @Test
    void createEmployee_duplicateEmail_throwsAndDoesNotSave() {
        when(employeeRepository.existsByEmail("arun@example.com")).thenReturn(true);

        DuplicateResourceException ex = assertThrows(DuplicateResourceException.class,
                () -> employeeService.createEmployee(request()));

        assertEquals("Email already exists: arun@example.com", ex.getMessage());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void createEmployee_duplicateMobile_throwsAndDoesNotSave() {
        when(employeeRepository.existsByEmail("arun@example.com")).thenReturn(false);
        when(employeeRepository.existsByMobileNumber("+919876543210")).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> employeeService.createEmployee(request()));

        verify(employeeRepository, never()).save(any());
    }

    @Test
    void getEmployeeById_found_returnsDto() {
        Employee e = new Employee();
        e.setId(1L);
        e.setFirstName("Arun");
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(e));

        EmployeeResponseDto response = employeeService.getEmployeeById(1L);

        assertEquals(1L, response.id());
        assertEquals("Arun", response.firstName());
    }

    @Test
    void getEmployeeById_notFound_throws() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> employeeService.getEmployeeById(99L));

        assertEquals("Employee not found with id: 99", ex.getMessage());
    }
}