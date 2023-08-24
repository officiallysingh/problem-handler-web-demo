package com.ksoot.problem.demo.config.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(staticName = "of")
public class CustomErrorResponse {
  private HttpStatus status;
  private String message;
}
