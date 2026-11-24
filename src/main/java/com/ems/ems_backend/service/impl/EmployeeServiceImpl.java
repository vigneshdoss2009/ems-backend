package com.ems.ems_backend.service.impl;

import com.ems.ems_backend.dto.EmployeeRequestDto;
import com.ems.ems_backend.dto.EmployeeResponseDto;
import com.ems.ems_backend.entity.Employee;
import com.ems.ems_backend.exception.DuplicateResourceException;
import com.ems.ems_backend.exception.ResourceNotFoundException;
import com.ems.ems_backend.mapper.EmployeeMapper;
import com.ems.ems_backend.repository.EmployeeRepository;
import com.ems.ems_backend.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

//    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
//        this.employeeRepository = employeeRepository;
//    }


    // GetEmployeeById
    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return EmployeeMapper.toResponse(employee);
    }



    // CreateEmployee
    @Override
    @Transactional
    public EmployeeResponseDto createEmployee(EmployeeRequestDto request) {

        if(employeeRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already exists: " + request.email());
        }

        if(employeeRepository.existsByMobileNumber(request.mobileNumber())) {
            throw new DuplicateResourceException("Mobile number already exists: " + request.mobileNumber());
        }

        Employee employee = EmployeeMapper.toEntity(request);
        Employee saved = employeeRepository.save(employee);
        return EmployeeMapper.toResponse(saved);
    }


}
