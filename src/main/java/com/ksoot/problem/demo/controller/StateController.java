package com.ksoot.problem.demo.controller;

import com.ksoot.problem.core.Problems;
import com.ksoot.problem.demo.model.CreateStateRequest;
import com.ksoot.problem.demo.model.StateResponse;
import com.ksoot.problem.demo.model.State;
import com.ksoot.problem.demo.repository.StateRepository;
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
@Tag(name = "State", description = "management APIs. Using MongoDB")
@RequiredArgsConstructor
public class StateController {

  private final StateRepository stateRepository;

  @Operation(
      operationId = "create-state",
      summary = "Creates a State",
      tags = {"State"})
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "201", description = "State created successfully"),
          @ApiResponse(responseCode = "400", description = "Bad request"),
          @ApiResponse(responseCode = "500", description = "Internal Server error")
      })
  @PostMapping("/states")
  public ResponseEntity<Void> createState(
      @Parameter(description = "Create State request", required = true) @RequestBody @Valid final CreateStateRequest request) {
    State state = State.of(request.getCode(), request.getName(), request.getGstCode());
    state = this.stateRepository.save(state);
    return ResponseEntity.created(
            linkTo(methodOn(StateController.class).getState(state.id())).withSelfRel().toUri())
        .build();
  }

  @GetMapping("/states/{idOrCode}")
  @Operation(
      operationId = "get-state-by-id-or-code",
      summary = "Gets a State",
      tags = {"State"})
  public ResponseEntity<StateResponse> getState(
      @Parameter(description = "State Id or Code", required = true) @PathVariable(name = "idOrCode") final String idOrCode) {
    return this.stateRepository
        .findByIdOrCode(idOrCode)
        .map(StateResponse::of)
        .map(ResponseEntity::ok)
        .orElseThrow(Problems::notFound);
  }
}
