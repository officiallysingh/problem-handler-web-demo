package com.ksoot.problem.demo.controller;

import com.ksoot.problem.Problems;
import com.ksoot.problem.demo.model.CreateEmployeeRequest;
import com.ksoot.problem.demo.model.Employee;
import com.ksoot.problem.demo.model.EmployeeResponse;
import com.ksoot.problem.demo.repository.EmployeeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Employee", description = "management APIs. Using PostgreSQL")
@RequiredArgsConstructor
public class EmployeeController {

  private final EmployeeRepository employeeRepository;

  @Operation(
      operationId = "create-employee",
      summary = "Creates a Employee",
      tags = {"Employee"})
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "201", description = "Employee created successfully"),
          @ApiResponse(responseCode = "400", description = "Bad request"),
          @ApiResponse(responseCode = "500", description = "Internal Server error")
      })
  @PostMapping("/employees")
  public ResponseEntity<Void> createEmployee(
      @Parameter(description = "Create Employee request", required = true) @RequestBody @Valid final CreateEmployeeRequest request) {
    Employee employee = this.employeeRepository.save(Employee.of(request.getName(), request.getDob()));
    return ResponseEntity.created(
            linkTo(methodOn(EmployeeController.class).getEmployee(employee.id())).withSelfRel().toUri())
        .build();
  }

  @GetMapping("/employees/{id}")
  @Operation(
      operationId = "get-employee-by-id",
      summary = "Gets an Employee",
      tags = {"Employee"})
  public ResponseEntity<EmployeeResponse> getEmployee(
      @Parameter(description = "Employee Id ", required = true) @PathVariable(name = "id") final Long id) {
    return this.employeeRepository
        .findById(id)
        .map(EmployeeResponse::of)
        .map(ResponseEntity::ok)
        .orElseThrow(Problems::notFound);
  }
}
