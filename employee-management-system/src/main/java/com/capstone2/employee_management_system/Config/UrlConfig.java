package com.capstone2.employee_management_system.Config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:url.properties")
@Getter
public class UrlConfig {

    // Employees
    @Value("${employees.base}")
    private String employeesBase;

    @Value("${employees.all}")
    private String employeesAll;

    @Value("${employees.byId}")
    private String employeesById;

    @Value("${employees.search}")
    private String employeesSearch;

    @Value("${employees.averageSalary}")
    private String employeesAverageSalary;

    @Value("${employees.averageAge}")
    private String employeesAverageAge;

    @Value("${employees.departmentsAll}")
    private String employeesDepartmentsAll;

    // Auth
    @Value("${auth.base}")
    private String authBase;

    @Value("${auth.register}")
    private String authRegister;

    @Value("${auth.login}")
    private String authLogin;


    // Dynamic URL helper
    public String employeesById(Long id) {
        return employeesById.replace("{id}", String.valueOf(id));
    }
}
