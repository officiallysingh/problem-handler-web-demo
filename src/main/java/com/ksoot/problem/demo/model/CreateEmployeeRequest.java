package com.ksoot.problem.demo.model;

import com.ksoot.problem.demo.util.AppConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateEmployeeRequest {

  @NotEmpty
  @Pattern(regexp = AppConstants.REGEX_ALPHABETS_AND_SPACES)
  @Schema(description = "Employee name", example = "John Rambo")
  private String name;

  @NotNull
  @Past
  @Schema(description = "Employee date of birth", example = "1983-06-06")
  private LocalDate dob;
}
