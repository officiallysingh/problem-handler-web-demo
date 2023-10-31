package com.ksoot.problem.demo.controller;

import com.ksoot.problem.core.ApplicationException;
import com.ksoot.problem.core.ApplicationProblem;
import com.ksoot.problem.core.MultiProblem;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.Problems;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.SocketTimeoutException;
import java.time.OffsetDateTime;
import java.util.NoSuchElementException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Validated
@RestController
@Tag(name = "Problem Demo", description = "APIs")
@RequestMapping("/problems")
class DemoProblemController {

    @Operation(
        summary = "Throws exception if file sie exceeds 1KB",
        tags = {"Problem Demo"})
    @PostMapping(path= "/uploadfile", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
        @Parameter(description = "Input file", required = true)
        @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok("File uploaded successfully");
    }

    @Operation(
        summary = "Throws unchecked exception",
        tags = {"Problem Demo"})
    @RequestMapping(path = "/handler-throwable", method = GET)
    ResponseEntity<String> throwable() {
        throw new IllegalArgumentException("Expected argument invalid", new IllegalStateException("Dummy cause"));
    }

    @Operation(
        summary = "Throws unchecked exception with nested cause",
        tags = {"Problem Demo"})
    @RequestMapping(path = "/handler-throwable-nested", method = GET)
    ResponseEntity<String> throwableNestedCause() {
        throw new IllegalArgumentException("expected", new IllegalStateException(new MyException()));
    }

    @Operation(
        summary = "Throws unchecked exception with nested cause, cause intern has another cause",
        tags = {"Problem Demo"})
    @RequestMapping(path = "/handler-throwable-deep-nested", method = GET)
    ResponseEntity<String> throwableDeepNestedCause() {
        throw new MyExceptionWithReason(new IllegalArgumentException("expected", new IllegalStateException()));
    }

    @Operation(
        summary = "Throws unchecked exception with nested exception",
        tags = {"Problem Demo"})
    @RequestMapping(path = "/nested-throwable", method = GET)
    ResponseEntity<String> nestedThrowable() {
        try {
            try {
                throw newNoSuchElement();
            } catch (final NoSuchElementException e) {
                throw newIllegalArgument(e);
            }
        } catch (final IllegalArgumentException e) {
            throw newIllegalState(e);
        }
    }

    @Operation(
        summary = "Throws conversion exception if OffsetDateTime is not in valid format",
        tags = {"Problem Demo"})
    @RequestMapping(path = "/handler-datetime-conversion", method = GET)
    ResponseEntity<Void> conversion(@RequestParam("dateTime") final OffsetDateTime dateTime) {
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Throws Socket timeout exception",
        tags = {"Problem Demo"})
    @RequestMapping(method = GET, path = "/socket-timeout")
    ResponseEntity<String> socketTimeout() throws SocketTimeoutException {
        throw new SocketTimeoutException("Request took longer than expected");
    }

    @Operation(
        summary = "Throws Constraint violation exception",
        tags = {"Problem Demo"})
    @RequestMapping(path = "/handler-constraint-violation", method = POST)
    ResponseEntity<String> validRequestBody(@Valid @RequestBody final UserRequest user) {
        return ResponseEntity.ok("done");
    }


    @Operation(
        summary = "Throws exception due to invalid query string",
        tags = {"Problem Demo"})
    @RequestMapping(path = "/handler-invalid-query-strings", method = GET)
    ResponseEntity<String> validRequestQueryStrings(@Valid final PageRequest pageRequest) {
        return ResponseEntity.ok("done");
    }

    @Operation(
        summary = "Expects JSON request, throws exception if request has a different media type",
        tags = {"Problem Demo"})
    @RequestMapping(path = "/handler-json-body", method = POST, consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<String> handleRequestBody(@RequestBody TestBody body) {
        return ResponseEntity.ok("done");
    }


    @Operation(
        summary = "Throws new Problem instance",
        tags = {"Problem Demo"})
    @RequestMapping(path = "/throw-problem", method = GET)
    ResponseEntity<String> throwProblem() {
        throw Problems.newInstance("sample.problem").detailArgs("PARAM")
                .throwAble(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
    }

    @Operation(
        summary = "Throws checked problem i.e. ApplicationException",
        tags = {"Problem Demo"})
    @RequestMapping(path = "/throw-problem-checked", method = GET)
    ResponseEntity<String> throwProblemChecked() throws ApplicationException {
        throw Problems.newInstance("sample.problem.checked").throwAbleChecked(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
    }


    @Operation(
        summary = "Throws multiple problems",
        tags = {"Problem Demo"})
    @RequestMapping(path = "/throw-multiple-problems", method = GET)
    ResponseEntity<String> throwMultipleProblems() {
        ApplicationException problemOne = Problems.newInstance("sample.problem.one").throwAbleChecked();
        ApplicationProblem problemTwo = Problems.newInstance(AppErrors.REMOTE_HOST_NOT_AVAILABLE).detailArgs("http://some.remote.host.com").throwAble();

        MultiProblem problems = Problems.ofExceptions(HttpStatus.MULTI_STATUS, problemOne, problemTwo);

        Problem problemThree = Problems.newInstance("3456", "Bad Request", "Invalid request received, Please retry with correct input")
            .parameter("additional-attribute", "Some additional attribute").build();
        problems.add(problemThree);
        Exception exception = new IllegalStateException("Just for testing exception");
        problems.add(exception);

        Problem problem = Problems.newInstance("111", "Dummy", "Hardcode attributes broblem").build();
        problems.add(problem);
        throw problems;
    }

    @Operation(
        summary = "Throws problem with additional attributes",
        tags = {"Problem Demo"})
    @RequestMapping(path = "/throw-problem-with-additional-attribute", method = GET)
    ResponseEntity<String> throwProblemWithAdditionalAttribute()  {
        Problem problem = Problems.newInstance("3456", "Bad Request", "Invalid request received, Please retry with correct input")
                .parameter("additional-attribute", "Some additional attribute").build();
        throw Problems.throwAble(HttpStatus.BAD_REQUEST, problem);
    }

    @Getter
    @Setter
    private static class TestBody {
        private String test;
    }

    @Valid
    @Getter
    @Setter
    public static final class UserRequest {

        @Size(min = 3, max = 10)
        private String name;

        @Size(min = 2, max = 5)
        private String designation;

        @NotNull
        @Valid
        private Address address;
    }

    @Getter
    @Setter
    @Valid
    public static final class Address {

        @NotEmpty
        private String city;

        @NotEmpty
        private String state;
    }

    @Data
    static final class PageRequest {

        @Min(value = 0)
        private int page = 0;

        @Min(value = 1)
        private int size = 50;
    }

    @Value
    public static class User {
        String name;
    }
    private IllegalStateException newIllegalState(final Exception e) {
        throw new IllegalStateException("Illegal State", e);
    }

    private IllegalArgumentException newIllegalArgument(final Exception e) {
        throw new IllegalArgumentException("Illegal Argument", e);
    }

    private NoSuchElementException newNoSuchElement() {
        throw new NoSuchElementException("No such element");
    }

    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    private static final class MyException extends RuntimeException {
        public MyException() {
        }

        public MyException(final Throwable cause) {
            super(cause);
        }
    }

    @ResponseStatus(code = HttpStatus.NOT_IMPLEMENTED, reason = "Test reason")
    private static final class MyExceptionWithReason extends RuntimeException {
        public MyExceptionWithReason(final Throwable cause) {
            super(cause);
        }
    }
}
