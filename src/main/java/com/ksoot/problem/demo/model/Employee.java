package com.ksoot.problem.demo.model;

import com.ksoot.problem.demo.util.AppConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Accessors(chain = true, fluent = true)
@Entity
@Table(name = "employees",
    uniqueConstraints = {@UniqueConstraint(name = "unqEmployeesName", columnNames = {"name"})})
@NoArgsConstructor
public class Employee {

  @Id
  @GeneratedValue
  private Long id;

  @NotEmpty
  @Size(max = 50)
  @Pattern(regexp = AppConstants.REGEX_ALPHABETS_AND_SPACES)
  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @NotNull
  @Past
  @Column(name = "dob", nullable = false)
  private LocalDate dob;

  public static Employee of(final String name, final LocalDate dob) {
    Employee employee = new Employee();
    employee.name = name;
    employee.dob = dob;
    return employee;
  }
}
