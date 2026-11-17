package com.ems.ems_backend.mapper;

import com.ems.ems_backend.dto.EmployeeRequestDto;
import com.ems.ems_backend.dto.EmployeeResponseDto;
import com.ems.ems_backend.entity.Employee;

public final class EmployeeMapper {

    private EmployeeMapper() { }


    public static Employee toEntity(EmployeeRequestDto dto) {

        if(dto == null) {
            return null;
        }

        Employee employee = new Employee();

        employee.setFirstName(dto.firstName());
        employee.setLastName(dto.lastName());
        employee.setEmail(dto.email());
        employee.setMobileNumber(dto.mobileNumber());
        employee.setDesignation(dto.designation());
        employee.setDateOfBirth(dto.dateOfBirth());
        employee.setJoinedAt(dto.joinedAt());
        employee.setSalary(dto.salary());
        return employee;

    }



    public static EmployeeResponseDto toResponse(Employee employee) {
        return new EmployeeResponseDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getMobileNumber(),
                employee.getDesignation(),
                employee.getJoinedAt(),
                employee.getDateOfBirth(),
                employee.getCreatedAt(),
                employee.getUpdatedAt(),
                employee.getSalary()
        );
    }
}
