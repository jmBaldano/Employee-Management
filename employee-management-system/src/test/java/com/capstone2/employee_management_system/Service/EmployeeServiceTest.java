package com.capstone2.employee_management_system.Service;

import com.capstone2.employee_management_system.Model.DepartmentModel;
import com.capstone2.employee_management_system.Model.EmployeeModel;
import com.capstone2.employee_management_system.Repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeModel employee;
    private DepartmentModel department;

    @BeforeEach
    void setUp() {
        employee = new EmployeeModel();
        employee.setId(1L);
        employee.setName("John");
        employee.setSalary(30000.0);
        employee.setBirthDate(LocalDate.now().minusYears(25));
        department = new DepartmentModel();
        department.setId(10L);
        employee.setDepartment(department);
    }

    @Test
    void shouldReturnAllEmployees() {
        List<EmployeeModel> employees = List.of(employee);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<EmployeeModel> result = employeeService.getAllEmployees();

        // Assert
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());

        verify(employeeRepository).findAll();
    }

    @Test
    void shouldSaveEmployee() {
        when(employeeRepository.save(employee)).thenReturn(employee);

        EmployeeModel saved = employeeService.createEmployee(employee);

        // Assert
        assertNotNull(saved);
        assertEquals("John", saved.getName());

        verify(employeeRepository).save(employee);
    }

    @Test
    void deleteEmployee() {
        Long employeeId = 1L;

        employeeService.deleteEmployee(employeeId);

        // Assert
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

    @Test
    void calculateAverageSalary() {
        when(employeeRepository.getAverageSalary()).thenReturn(50000.0);

        Double result = employeeService.calculateAverageSalary();

        // Assert
        assertEquals(50000.0, result);
    }
    @Test
    void calculateAverageAge() {
        // Arrange
        when(employeeRepository.getAverageAge()).thenReturn(30.0);

        // Act
        Double result = employeeService.calculateAverageAge();

        // Assert
        assertEquals(30.0, result);
    }
    @Test
    void searchByName() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        List<EmployeeModel> result = employeeService.searchByName("john");

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
    }
    @Test
    void searchByEmployeeId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.searchByEmployeeId(null);
        });

        assertEquals("Employee ID is required", exception.getMessage());
    }
    @Test
    void createEmployee() {
        EmployeeModel lastEmployee = new EmployeeModel();
        lastEmployee.setId(5L); // last ID in DB
        when(employeeRepository.findTopByOrderByIdDesc()).thenReturn(lastEmployee);
        when(employeeRepository.save(any(EmployeeModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EmployeeModel created = employeeService.createEmployee(employee);

        // Assert
        assertEquals("0006", created.getEmployeeId()); // next ID formatted as 4 digits
        assertEquals("John", created.getName());
        assertEquals(30000.0, created.getSalary());

        verify(employeeRepository).findTopByOrderByIdDesc();
        verify(employeeRepository).save(employee);
    }
    @Test
    void updateEmployee() {
        // Arrange
        EmployeeModel updatedEmployee = new EmployeeModel();
        updatedEmployee.setName("Jane");
        updatedEmployee.setSalary(40000.0);

        // Mock getEmployeeById
        when(employeeRepository.findById(employee.getId())).thenReturn(java.util.Optional.of(employee));
        when(employeeRepository.save(any(EmployeeModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EmployeeModel result = employeeService.updateEmployee(employee.getId(), updatedEmployee);

        // Assert
        assertEquals("Jane", result.getName());
        assertEquals(40000.0, result.getSalary());
        assertEquals(employee.getId(), result.getId());

        verify(employeeRepository).save(employee);
    }
    @Test
    void searchByAge_shouldReturnEmployeeWithMatchingAge() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        List<EmployeeModel> result = employeeService.searchByAge(25);

        // Assert
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
    }
    @Test
    void filterByDepartment() {

        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        List<EmployeeModel> result = employeeService.filterByDepartment(10L);

        // Assert
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
    }

}

