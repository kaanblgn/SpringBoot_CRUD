package com.kaanb.springboot.controller;


import com.kaanb.springboot.exception.ResourceNotFoundException;
import com.kaanb.springboot.model.Employee;
import com.kaanb.springboot.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    /** Main focus of the project is to do CRUD operations on PostgreSQL via Spring Boot architecture.
     *
     * CRUD is standing for;
     * CREATE
     * READ
     * UPDATE
     * DELETE
     *
     * This project will have all of this operation controller via Rest API/


    /** getAllEmployees
     *
     * First controller is getAllEmployees which is the controller for returning all the employee information in the
     * repository.
     *
     *
     * Addition to that, it sorts the List upon calling, for the future CRUD operations.*/

    @GetMapping("/getAllEmployees")
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll().stream().sorted(Comparator.comparingLong(Employee::getId))
                .collect(Collectors.toList());
    }



    /** CREATE employee
     *
     * Second controller is the first CRUD operator Create. It simply helps us to write an index to the database
     *
      */

    @PostMapping("/saveEmployee")
    public Employee createEmployee(@RequestBody Employee employee){
        return employeeRepository.save(employee);
    }



    /** READ employee
     *
     * READ operator. It simply helps us to view an employee entry, which specified by its unique ID
     *
     * also, there is an exception thrower for some useful cases.
     *
     */
    @GetMapping("/getEmployeeById/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist within database: " + id));
        return ResponseEntity.ok(employee);
    }


    /** UPDATE employee
     *
     * UPDATE operator. It simply helps us to update attributes of an employee entry, which specified by its unique ID
     *
     */
    @PutMapping("/updateEmployeeById/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id,@RequestBody Employee employeeDetails){
        Employee updateEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist within database: " + id));

        updateEmployee.setFirstName(employeeDetails.getFirstName());
        updateEmployee.setLastName(employeeDetails.getLastName());
        updateEmployee.setEmailId(employeeDetails.getEmailId());

        employeeRepository.save(updateEmployee);

        return ResponseEntity.ok(updateEmployee);
    }


    /** DELETE employee
     *
     * DELETE operator. Last CRUD operator. It simply helps us to delete any entry from the employee table, which
     * specified by its unique ID
     *
     */
    @DeleteMapping("/deleteEmployeeById/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable Long id){

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist within database: " + id));

        employeeRepository.delete(employee);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
