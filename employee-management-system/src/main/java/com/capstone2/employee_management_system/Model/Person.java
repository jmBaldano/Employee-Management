package com.capstone2.employee_management_system.Model;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.time.LocalDate;
import java.time.Period;

@MappedSuperclass
@Data
public abstract class Person {

    private String name;
    private LocalDate birthDate;

    // abstract method
    public abstract String getIdentifier();

    public int getAge() {
        if (birthDate == null) {
            return 0;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
