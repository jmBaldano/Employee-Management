package com.capstone2.employee_management_system.Controller;

import com.capstone2.employee_management_system.Model.DepartmentModel;
import com.capstone2.employee_management_system.Model.EmployeeModel;
import com.capstone2.employee_management_system.Repository.DepartmentRepository;
import com.capstone2.employee_management_system.Service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentRepository departmentRepository;

    public ResponseEntity<Page<EmployeeModel>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                employeeService.getAllEmployees(page, size)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeModel> getEmployeeById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(employeeService.getEmployeeById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeModel employee) {
        try {
            LocalDate birthDate = employee.getBirthDate();
            int age = Period.between(birthDate, LocalDate.now()).getYears();

            if (age < 18) {
                throw new IllegalArgumentException("Employee must be at least 18 years old.");
            }

            return ResponseEntity.ok(employeeService.createEmployee(employee));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Failed to create employee");
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
    public ResponseEntity<Page<EmployeeModel>> search(
            @RequestParam(required = false ) String name,
            @RequestParam(required = false ) Integer age,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {

            if (name != null) {
                return ResponseEntity.ok(
                        employeeService.searchByName(name, page, size)
                );
            }

            if (age != null) {
                return ResponseEntity.ok(
                        employeeService.searchByAge(age, page, size)
                );
            }

            if (departmentId != null) {
                return ResponseEntity.ok(
                        employeeService.filterByDepartment(departmentId, page, size)
                );
            }

            return ResponseEntity.ok(employeeService.getAllEmployees(page, size));

        } catch (IllegalArgumentException e) {
            // input problems
            return ResponseEntity.badRequest().body(null);

        } catch (RuntimeException e) {
            // not found / server error
            return ResponseEntity.status(404).body(null);
        }
    }


    @GetMapping("/department/{deptId}")
    public ResponseEntity<Page<EmployeeModel>> filterByDepartment(
            @PathVariable Long deptId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            return ResponseEntity.ok(
                    employeeService.filterByDepartment(deptId, page, size)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/averageSalary")
    public ResponseEntity<Double> getAverageSalary() {
        return ResponseEntity.ok(employeeService.calculateAverageSalary());
    }

    @GetMapping("/averageAge")
    public ResponseEntity<Double> getAverageAge(){
        return ResponseEntity.ok(employeeService.calculateAverageAge());
    }
}
