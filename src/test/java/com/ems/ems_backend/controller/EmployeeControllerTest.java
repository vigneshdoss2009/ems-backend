package com.ems.ems_backend.controller;

import com.ems.ems_backend.dto.EmployeeResponseDto;
import com.ems.ems_backend.exception.DuplicateResourceException;
import com.ems.ems_backend.exception.ResourceNotFoundException;
import com.ems.ems_backend.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    private static final String REQUEST_JSON = """
            {
              "firstName": "Arun",
              "lastName": "Kumar",
              "email": "arun@example.com",
              "mobileNumber": "+919876543210",
              "designation": "Java Developer",
              "joinedAt": "2024-01-15",
              "dateOfBirth": "1998-05-10",
              "salary": 45000.00
            }
            """;

    private EmployeeResponseDto response() {
        return new EmployeeResponseDto(1L, "Arun", "Kumar", "arun@example.com", "+919876543210",
                "Java Developer", LocalDate.of(2024, 1, 15), LocalDate.of(1998, 5, 10),
                LocalDateTime.now(), LocalDateTime.now(), new BigDecimal("45000.00"));
    }

    @Test
    void createEmployee_returns201WithLocation() throws Exception {
        when(employeeService.createEmployee(any())).thenReturn(response());

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/employees/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("arun@example.com"));
    }

    @Test
    void createEmployee_duplicate_returns409() throws Exception {
        when(employeeService.createEmployee(any()))
                .thenThrow(new DuplicateResourceException("Email already exists: arun@example.com"));

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Email already exists: arun@example.com"))
                .andExpect(jsonPath("$.path").value("/api/employees"));
    }

    @Test
    void createEmployee_malformedJson_returns400() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ not valid json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getEmployeeById_found_returns200() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(response());

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Arun"));
    }

    @Test
    void getEmployeeById_notFound_returns404() throws Exception {
        when(employeeService.getEmployeeById(99L))
                .thenThrow(new ResourceNotFoundException("Employee not found with id: 99"));

        mockMvc.perform(get("/api/employees/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found with id: 99"));
    }

    @Test
    void getEmployeeById_invalidId_returns400() throws Exception {
        mockMvc.perform(get("/api/employees/abc"))
                .andExpect(status().isBadRequest());
    }
}