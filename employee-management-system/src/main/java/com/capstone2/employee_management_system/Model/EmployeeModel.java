package com.capstone2.employee_management_system.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="employee")
public class EmployeeModel extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double salary;
    private String employeeId;

    @Override
    public String getIdentifier() {
        return employeeId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departmentId")
    private DepartmentModel department;
}