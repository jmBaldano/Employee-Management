package com.capstone2.employee_management_system.Service;

import com.capstone2.employee_management_system.Model.DepartmentModel;
import com.capstone2.employee_management_system.Repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final MessageSource messageSource;

    public DepartmentService(DepartmentRepository departmentRepository, MessageSource messageSource) {
        this.departmentRepository = departmentRepository;
        this.messageSource = messageSource;
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, Locale.getDefault());
    }


    public DepartmentModel createDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException(getMessage("department.required"));
        }

        if (departmentRepository.existsByDepartment(department)) {
            throw new RuntimeException(getMessage("department.alreadyexists"));
        }

        return departmentRepository.save(new DepartmentModel(department));
    }

    public List<DepartmentModel> getAllDepartments() {
        return departmentRepository.findAll();
    }


    public DepartmentModel getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        getMessage("department.notfound")
                ));
    }


    public DepartmentModel updateDepartment(Long id, String department) {
        DepartmentModel dept = getDepartmentById(id);

        if (!dept.getDepartment().equalsIgnoreCase(department)
                && departmentRepository.existsByDepartment(department)) {
            throw new RuntimeException(getMessage("department.alreadyexists"));
        }

        dept.setDepartment(department);
        return departmentRepository.save(dept);
    }


    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException(getMessage("department.notfound"));
        }
        departmentRepository.deleteById(id);
    }
}



