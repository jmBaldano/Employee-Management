package com.capstone2.employee_management_system.Repository;

import com.capstone2.employee_management_system.Model.AdminModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AdminModel, Long> {
    //checks if the username is unique or existing
    Optional<AdminModel> findByUsername(String username);
}
