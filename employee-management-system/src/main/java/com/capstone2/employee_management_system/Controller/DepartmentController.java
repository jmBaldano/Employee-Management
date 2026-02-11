package com.capstone2.employee_management_system.Controller;

import com.capstone2.employee_management_system.Model.DepartmentModel;
import com.capstone2.employee_management_system.Service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<DepartmentModel> create(@RequestBody DepartmentModel dept) {
        return ResponseEntity.ok(
                departmentService.createDepartment(dept.getDepartment())
        );
    }

    // READ (all)
    @GetMapping
    public ResponseEntity<List<DepartmentModel>> getAll() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    // READ (by id)
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentModel> getById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentModel> update(
            @PathVariable Long id,
            @RequestBody DepartmentModel dept) {

        return ResponseEntity.ok(
                departmentService.updateDepartment(id, dept.getDepartment())
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}

