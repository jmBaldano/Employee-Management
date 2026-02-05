package com.capstone2.employee_management_system.Model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="department")
@Data
public class DepartmentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String department;
}
