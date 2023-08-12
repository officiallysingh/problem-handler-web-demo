package com.ksoot.problem.demo.repository;

import com.ksoot.problem.demo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
