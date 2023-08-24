package com.ksoot.problem.demo.config.error;

import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemUtils;
import com.ksoot.problem.spring.advice.validation.MethodArgumentNotValidAdviceTrait;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@ControllerAdvice  // Uncomment to see custom controller advice working
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
class CustomMethodArgumentNotValidExceptionHandler implements MethodArgumentNotValidAdviceTrait<NativeWebRequest, ResponseEntity<ProblemDetail>> {
  public static final String COLON = ": ";

  public ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(final MethodArgumentNotValidException exception, final NativeWebRequest request) {
    List<String> violations = processBindingResult(exception.getBindingResult());
    final String errors = violations.stream()
        .collect(Collectors.joining(", "));
    Problem problem = Problem.code(ProblemUtils.statusCode(HttpStatus.BAD_REQUEST)).title(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .detail(errors).build();
    return create(exception, request, HttpStatus.BAD_REQUEST,
        problem);
  }

  List<String> processBindingResult(final BindingResult bindingResult) {
    final List<String> fieldErrors =
        bindingResult.getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + COLON + fieldError.getDefaultMessage())
            .toList();
    final List<String> globalErrors =
        bindingResult.getGlobalErrors().stream()
            .map(
                objectError ->
                    objectError.getObjectName() + COLON + objectError.getDefaultMessage())
            .toList();

    final List<String> errors = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(fieldErrors)) {
      errors.addAll(fieldErrors);
    }
    if (CollectionUtils.isNotEmpty(globalErrors)) {
      errors.addAll(globalErrors);
    }
    return errors;
  }
}
