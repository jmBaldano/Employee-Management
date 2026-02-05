package com.capstone2.employee_management_system.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Data
@Table(name="employee")
public class EmployeeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double salary;
    private LocalDate birthDate;
    private String employeeId;

    @Transient
    public int getAge(){
        if(birthDate == null){
            return 0;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departmentId")
    private DepartmentModel department;
}
