package com.capstone2.employee_management_system.Repository;

import com.capstone2.employee_management_system.Model.EmployeeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<EmployeeModel,Long> {
    EmployeeModel findTopByOrderByIdDesc();
    List<EmployeeModel> findByEmployeeId(String employeeId);
    @Query("select avg(emp.salary) from EmployeeModel emp")
    Double getAverageSalary();

    @Query("SELECT AVG(TIMESTAMPDIFF(YEAR, e.birthDate, CURRENT_DATE)) FROM EmployeeModel e")
    Double getAverageAge();

    Page<EmployeeModel> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<EmployeeModel> findByAge(int age, Pageable pageable);

    Page<EmployeeModel> findByDepartment_Id(Long departmentId, Pageable pageable);


}
