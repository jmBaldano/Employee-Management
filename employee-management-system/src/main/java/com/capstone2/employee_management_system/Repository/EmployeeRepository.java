package com.capstone2.employee_management_system.Repository;

import com.capstone2.employee_management_system.Model.EmployeeModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<EmployeeModel,Long> {
    EmployeeModel findTopByOrderByIdDesc();
    List<EmployeeModel> findByEmployeeId(String employeeId);
}
