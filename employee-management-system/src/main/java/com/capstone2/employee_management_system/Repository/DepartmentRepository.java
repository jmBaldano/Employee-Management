package com.capstone2.employee_management_system.Repository;

import com.capstone2.employee_management_system.Model.DepartmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentModel, Long> {
    boolean existsByDepartment(String department);

}
