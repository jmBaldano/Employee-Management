package com.capstone2.employee_management_system.Controller;

import com.capstone2.employee_management_system.Model.DepartmentModel;
import com.capstone2.employee_management_system.Model.EmployeeModel;
import com.capstone2.employee_management_system.Repository.DepartmentRepository;
import com.capstone2.employee_management_system.Service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Page<EmployeeModel>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                employeeService.getAllEmployees(page, size)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeModel employee) {
        return ResponseEntity.ok(employeeService.createEmployee(employee));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeModel> updateEmployee(@PathVariable Long id, @RequestBody EmployeeModel employee) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Employee deleted successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String employeeId,
            @RequestParam(required = false ) String name,
            @RequestParam(required = false ) Integer age,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (employeeId != null && !employeeId.isEmpty()) {
            return ResponseEntity.ok(employeeService.searchByEmployeeId(employeeId, page, size));
        }

        if (name != null && !name.isEmpty()) {
            return ResponseEntity.ok(employeeService.searchByName(name, page, size));
        }

        if (age != null) {
            return ResponseEntity.ok(employeeService.searchByAge(age, page, size));
        }

        if (departmentId != null) {
            return ResponseEntity.ok(employeeService.filterByDepartment(departmentId, page, size));
        }

        return ResponseEntity.ok(employeeService.getAllEmployees(page, size));
    }


    @GetMapping("/averageSalary")
    public ResponseEntity<Double> getAverageSalary() {
        return ResponseEntity.ok(employeeService.calculateAverageSalary());
    }

    @GetMapping("/averageAge")
    public ResponseEntity<Double> getAverageAge(){
        return ResponseEntity.ok(employeeService.calculateAverageAge());
    }

    @GetMapping("/departments/all")
    public ResponseEntity<List<DepartmentModel>> getAllDepartments() {
        return ResponseEntity.ok(departmentRepository.findAll());
    }
}
