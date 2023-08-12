package com.ksoot.problem.demo.controller;

import com.ksoot.problem.demo.model.Pet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Pet", description = "APIs. As per OpenAPI Spec")
@RequestMapping("/api")
public class PetStoreController {

  @Operation(
      operationId = "addPet",
      summary = "Creates a Pet",
      tags = {"Pet"})
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "201", description = "Pet created successfully"),
          @ApiResponse(responseCode = "400", description = "Bad request"),
          @ApiResponse(responseCode = "500", description = "Internal Server error")
      })
  @PostMapping("/pets")
  public ResponseEntity<Void> createEmployee(
      @Parameter(description = "Create Pet request", required = true) final Pet pet) {
    return ResponseEntity.created(null)
        .build();
  }
}
