package com.capstone2.employee_management_system.Service;

import com.capstone2.employee_management_system.Model.EmployeeModel;
import com.capstone2.employee_management_system.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public EmployeeModel createEmployee(EmployeeModel employee) {
        // get the last employee
        EmployeeModel lastEmployee = employeeRepository.findTopByOrderByIdDesc();

        // next number (1 if DB empty)
        long nextId = (lastEmployee != null) ? lastEmployee.getId() + 1 : 1;

        // format as 4 digits
        employee.setEmployeeId(String.format("%04d", nextId));

        // save to DB
        return employeeRepository.save(employee);
    }


}
