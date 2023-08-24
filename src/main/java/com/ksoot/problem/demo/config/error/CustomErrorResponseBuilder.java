package com.ksoot.problem.demo.config.error;

import com.ksoot.problem.core.ErrorResponseBuilder;
import com.ksoot.problem.core.MediaTypes;
import com.ksoot.problem.core.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

//@Component  // Uncomment to see custom error response working
class CustomErrorResponseBuilder implements ErrorResponseBuilder<NativeWebRequest, ResponseEntity<CustomErrorResponse>> {

  @Override
  public ResponseEntity<CustomErrorResponse> buildResponse(final Throwable throwable, final NativeWebRequest request,
                                                           final HttpStatus status, final HttpHeaders headers, final Problem problem) {
    CustomErrorResponse errorResponse = CustomErrorResponse.of(status, problem.getDetail());
    ResponseEntity<CustomErrorResponse> responseEntity = ResponseEntity
        .status(status).headers(headers).contentType(MediaTypes.PROBLEM).body(errorResponse);
    return responseEntity;
  }
}
