package com.capstone2.employee_management_system.Controller;

import com.capstone2.employee_management_system.Model.DepartmentModel;
import com.capstone2.employee_management_system.Model.EmployeeModel;
import com.capstone2.employee_management_system.Repository.DepartmentRepository;
import com.capstone2.employee_management_system.Service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentRepository departmentRepository;

    @GetMapping
    public ResponseEntity<List<EmployeeModel>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeModel> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PostMapping
    public ResponseEntity<EmployeeModel> createEmployee(@RequestBody EmployeeModel employee) {
        try {
            EmployeeModel created = employeeService.createEmployee(employee);
            return ResponseEntity.ok(created);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeModel> updateEmployee(@PathVariable Long id, @RequestBody EmployeeModel employee) {
        try {
            EmployeeModel updated = employeeService.updateEmployee(id, employee);
            return ResponseEntity.ok(updated);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok("Employee deleted successfully");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Delete failed");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<EmployeeModel>> search(
            @RequestParam(required = false) String employeeId,
            @RequestParam(name ="employeeId", required = false ) String name
    ) {
        if (employeeId != null && !employeeId.isEmpty()) {
            return ResponseEntity.ok(
                    employeeService.searchByEmployeeId(employeeId)
            );
        }

        if (name != null && !name.isEmpty()) {
            return ResponseEntity.ok(
                    employeeService.searchByName(name)
            );
        }

        return ResponseEntity.badRequest().build();
    }


    @GetMapping("/department/{deptId}")
    public ResponseEntity<List<EmployeeModel>> filterByDepartment(@PathVariable Long deptId) {
        return ResponseEntity.ok(employeeService.filterByDepartment(deptId));
    }

    @GetMapping("/departments/all")
    public ResponseEntity<List<DepartmentModel>> getAllDepartments() {
        return ResponseEntity.ok(departmentRepository.findAll());
    }
}
