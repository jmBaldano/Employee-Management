package com.capstone2.employee_management_system.Service;

import com.capstone2.employee_management_system.Model.EmployeeModel;
import com.capstone2.employee_management_system.Repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.message.DefaultFlowMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Locale;


@Service
@AllArgsConstructor
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    private final MessageSource messageSource;
    private MessageService messageService;

    public EmployeeModel createEmployee(EmployeeModel employee) {
        // Validate age
        LocalDate birthDate = employee.getBirthDate();
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < 18) {
            throw new IllegalArgumentException(messageService.getMessage("employee.invalidage"));
        }

        // get the last employee
        EmployeeModel lastEmployee = employeeRepository.findTopByOrderByIdDesc();

        // next number (1 if DB empty)
        long nextId = (lastEmployee != null) ? lastEmployee.getId() + 1 : 1;

        // format as 4 digits
        employee.setEmployeeId(String.format("%04d", nextId));

        // save to DB
        return employeeRepository.save(employee);
    }

//    public List<EmployeeModel> getAllEmployees() {
//        return employeeRepository.findAll();
//    }

    public Page<EmployeeModel> getAllEmployees(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            Page<EmployeeModel> result = employeeRepository.findAll(pageable);

            if (result.isEmpty()) {
                throw new RuntimeException(messageService.getMessage("employee.noemployees"));
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException(messageService.getMessage("system.error"), e);
        }
    }

    public EmployeeModel getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException(getMessage("employee.notfound")));
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
        try {
            employeeRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            // Thrown if the employee with the given id does not exist
            throw new RuntimeException(messageService.getMessage("employee.notfound"), e);
        } catch (Exception e) {
            // Any unexpected error
            throw new RuntimeException(messageService.getMessage("system.error"), e);
        }
    }


//    public List<EmployeeModel> searchByName(String name) {
//        return employeeRepository.findAll().stream()
//                .filter(e -> e.getName().toLowerCase().contains(name.toLowerCase()))
//                .toList();
//    }

    public Page<EmployeeModel> searchByName(String name, int page, int size) {
        try {
            // Validate input
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException(messageService.getMessage("employee.nonameprovided"));
            }

            Pageable pageable = PageRequest.of(page, size);
            Page<EmployeeModel> result = employeeRepository.findByNameContainingIgnoreCase(name, pageable);

            if (result.isEmpty()) {
                throw new RuntimeException(messageService.getMessage("employee.namenoresults"));
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException(messageService.getMessage("system.error"), e);
        }
    }


    public Page<EmployeeModel> searchByEmployeeId(String employeeId, int page, int size) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException(messageService.getMessage("employee.invalidinput"));
        }

        Page<EmployeeModel> result = employeeRepository.findByEmployeeId(employeeId, PageRequest.of(page, size));

        if (result.isEmpty()) {
            throw new RuntimeException(messageService.getMessage("employeeID.notfound"));
        }

        return result;
    }


//    public List<EmployeeModel> searchByAge(int age) {
//        return employeeRepository.findAll().stream()
//                .filter(e -> e.getAge() == age)
//                .toList();
//    }


    public Page<EmployeeModel> searchByAge(int age, int page, int size) {
        try {
            // Validate input
            if (age <= 0) {
                throw new IllegalArgumentException(messageService.getMessage("employee.invalidage"));
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            Page<EmployeeModel> result = employeeRepository.findByAge(age, pageable);

            if (result.isEmpty()) {
                throw new RuntimeException(messageService.getMessage("employee.noagefound"));
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException(messageService.getMessage("system.error"), e);
        }
    }




//    public List<EmployeeModel> filterByDepartment(Long departmentId) {
//        return employeeRepository.findAll().stream()
//                .filter(e -> e.getDepartment() != null && e.getDepartment().getId().equals(departmentId))
//                .toList();
//    }


    public Page<EmployeeModel> filterByDepartment(Long departmentId, int page, int size) {
        try {
            // Validate input
            if (departmentId == null) {
                throw new IllegalArgumentException(messageService.getMessage("employee.nodepartmentprovided"));
            }

            Pageable pageable = PageRequest.of(page, size);
            Page<EmployeeModel> result = employeeRepository.findByDepartment_Id(departmentId, pageable);

            if (result.isEmpty()) {
                throw new RuntimeException(messageService.getMessage("employee.departmentnoresults"));
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException(messageService.getMessage("system.error"), e);
        }
    }




    public Double calculateAverageSalary() {
        Double avg = employeeRepository.getAverageSalary();
        return avg != null ? avg : 0.0;
    }

    public Double calculateAverageAge(){
        Double avg = employeeRepository.getAverageAge();
        return  avg != null ? avg : 0.0;
    }



    private String getMessage(String code) {
        return messageSource.getMessage(code, null, Locale.getDefault());
    }

}
