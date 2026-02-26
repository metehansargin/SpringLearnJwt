package com.metehansargin.jwt.controller;

import com.metehansargin.jwt.dto.DtoEmployee;
import com.metehansargin.jwt.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DtoEmployee> getFindById(@Valid @PathVariable(name = "id") Long id){
        return ResponseEntity.ok(employeeService.getFindById(id));
    }
}
