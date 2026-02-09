package com.capstone2.employee_management_system.Service;

import com.capstone2.employee_management_system.Model.EmployeeModel;
import com.capstone2.employee_management_system.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public EmployeeModel createEmployee(EmployeeModel employee) {
        // get the last employee
        EmployeeModel lastEmployee = employeeRepository.findTopByOrderByIdDesc();

        // next number (1 if DB empty)
        long nextId = (lastEmployee != null) ? lastEmployee.getId() + 1 : 1;

        // format as 4 digits
        employee.setEmployeeId(String.format("%04d", nextId));

        // save to DB
        return employeeRepository.save(employee);
    }

    public List<EmployeeModel> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public EmployeeModel getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public EmployeeModel updateEmployee(Long id, EmployeeModel updatedEmployee) {
        EmployeeModel employee = getEmployeeById(id);
        employee.setName(updatedEmployee.getName());
        employee.setSalary(updatedEmployee.getSalary());
        employee.setBirthDate(updatedEmployee.getBirthDate());
        employee.setDepartment(updatedEmployee.getDepartment());
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public List<EmployeeModel> searchByName(String name) {
        return employeeRepository.findAll().stream()
                .filter(e -> e.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }
    public List<EmployeeModel> searchByEmployeeId(String employeeId) {

        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID is required");
        }

        List<EmployeeModel> result = employeeRepository.findByEmployeeId(employeeId);

        if (result.isEmpty()) {
            throw new RuntimeException("Employee not found");
        }

        return result;
    }


    public List<EmployeeModel> searchByAge(int age) {
        return employeeRepository.findAll().stream()
                .filter(e -> e.getAge() == age)
                .toList();
    }


    public List<EmployeeModel> filterByDepartment(Long departmentId) {
        return employeeRepository.findAll().stream()
                .filter(e -> e.getDepartment() != null && e.getDepartment().getId().equals(departmentId))
                .toList();
    }

    public Double calculateAverageSalary() {
        Double avg = employeeRepository.getAverageSalary();
        return avg != null ? avg : 0.0;
    }

    public Double calculateAverageAge(){
        Double avg = employeeRepository.getAverageAge();
        return  avg != null ? avg : 0.0;
    }

}
