package com.ksoot.problem.demo.controller;

import com.ksoot.problem.demo.model.Pet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Pet", description = "management APIs. As per OpenAPI Spec")
@RequestMapping("/api")
public class PetStoreController {

  @Operation(
      operationId = "addPet",
      summary = "Creates a Pet",
      description = "Request is validated against the OpenAPI spec Pet schema with following contraints.</br>"
          + "<b>id</b>: Numeric value between 1 and 5. Optional.</br>"
          + "<b>name</b>: String value between 5 and 10 characters long. Required.</br>"
          + "<b>category</b>: Any string. Required.</br>"
          + "<b>tags</b>: An Array of strings. Optional.</br>"
          + "<b>status</b>: Enum with allowed values:  AVAILABLE, PENDING, SOLD. Optional.</br>",
      tags = {"Pet"})
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "201", description = "Pet created successfully"),
          @ApiResponse(responseCode = "400", description = "Bad request"),
          @ApiResponse(responseCode = "500", description = "Internal Server error")
      })
  @PostMapping("/pets")
  public ResponseEntity<Void> createEmployee(
      @Parameter(description = "Create Pet request", required = true) @RequestBody final Pet pet) {

    return ResponseEntity.created(null)
        .build();
  }
}
