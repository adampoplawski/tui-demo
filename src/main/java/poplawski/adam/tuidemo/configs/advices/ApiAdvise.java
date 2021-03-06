package poplawski.adam.tuidemo.configs.advices;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import poplawski.adam.tuidemo.configs.api.ErrorResponse;
import poplawski.adam.tuidemo.exceptions.GitIntegrationException;
import poplawski.adam.tuidemo.exceptions.GitRequestLimitsExceeded;
import poplawski.adam.tuidemo.exceptions.GitUserNotFoundException;


@RestControllerAdvice
public class ApiAdvise extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {GitUserNotFoundException.class})
    protected ResponseEntity<Object> handle404(RuntimeException ex, WebRequest request) {
        return createResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {GitRequestLimitsExceeded.class})
    protected ResponseEntity<Object> handle429(RuntimeException ex, WebRequest request) {
        return createResponse(ex, HttpStatus.TOO_MANY_REQUESTS, request);
    }

    @ExceptionHandler(value = {GitIntegrationException.class})
    protected ResponseEntity<Object> handle503(RuntimeException ex, WebRequest request) {
        return createResponse(ex, HttpStatus.SERVICE_UNAVAILABLE, request);
    }

    private ResponseEntity<Object> createResponse(Exception ex, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(
                ex,
                ErrorResponse.builder()
                        .status(status.value())
                        .message(ex.getMessage())
                        .build(),
                new HttpHeaders(),
                status,
                request);
    }
}
