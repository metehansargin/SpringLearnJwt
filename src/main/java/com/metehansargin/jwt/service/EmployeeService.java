package com.metehansargin.jwt.service;

import com.metehansargin.jwt.dto.DtoDepartment;
import com.metehansargin.jwt.dto.DtoEmployee;
import com.metehansargin.jwt.entity.Department;
import com.metehansargin.jwt.entity.Employee;
import com.metehansargin.jwt.repository.EmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public DtoEmployee getFindById(Long id){
        DtoEmployee dtoEmployee=new DtoEmployee();
        DtoDepartment dtoDepartment=new DtoDepartment();
        Optional<Employee> optionalEmployee=employeeRepository.findById(id);

        if (optionalEmployee.isEmpty()){
            return null;
        }
        Employee employee=optionalEmployee.get();
        Department department=optionalEmployee.get().getDepartment();

        BeanUtils.copyProperties(employee,dtoEmployee);
        BeanUtils.copyProperties(department,dtoDepartment);

        dtoEmployee.setDtoDepartment(dtoDepartment);
        return dtoEmployee;
    }
}
