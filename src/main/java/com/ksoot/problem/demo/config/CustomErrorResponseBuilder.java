package com.ksoot.problem.demo.config;

import com.ksoot.problem.core.ErrorResponseBuilder;
import com.ksoot.problem.core.MediaTypes;
import com.ksoot.problem.core.Problem;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.context.request.NativeWebRequest;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static jakarta.servlet.RequestDispatcher.ERROR_EXCEPTION;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Component
class CustomErrorResponseBuilder implements ErrorResponseBuilder<NativeWebRequest, ResponseEntity<CustomErrorResponseBuilder.ErrorResponse>> {

  @Override
  public ResponseEntity<CustomErrorResponseBuilder.ErrorResponse> buildResponse(final Throwable throwable, final NativeWebRequest request,
                                                     final HttpStatus status, final HttpHeaders headers, final Problem problem) {
    CustomErrorResponseBuilder.ErrorResponse errorResponse = CustomErrorResponseBuilder.ErrorResponse.of(status, problem.getDetail());
    ResponseEntity<CustomErrorResponseBuilder.ErrorResponse> responseEntity = ResponseEntity
        .status(status).headers(headers).contentType(MediaTypes.PROBLEM).body(errorResponse);
    return responseEntity;
  }

  @Getter
  @AllArgsConstructor(staticName = "of")
  public static class ErrorResponse {
    private HttpStatus status;
    private String message;
  }
}
