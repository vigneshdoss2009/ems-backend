package com.ems.ems_backend.controller;

import com.ems.ems_backend.dto.EmployeeRequestDto;
import com.ems.ems_backend.dto.EmployeeResponseDto;
import com.ems.ems_backend.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;


    @GetMapping("{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(@RequestBody EmployeeRequestDto request) {
        EmployeeResponseDto created = employeeService.createEmployee(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }
}
