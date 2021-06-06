package com.MyMoviePlan.exception;

import com.MyMoviePlan.model.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> handleException(final Exception exception,
                                                        final HttpServletRequest request,
                                                        final HttpServletResponse response) {
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");

        final int status = response.getStatus();

        final String exceptionMessage = exception.getMessage();
        if (statusCode == null || statusCode == 0) {
            statusCode = status;
            if (HttpStatus.valueOf(status).getReasonPhrase().equals("OK"))
                statusCode = 403;
        }

        final HttpStatus httpStatus = HttpStatus.valueOf(statusCode);

        final HttpResponse httpResponse =
                new HttpResponse(statusCode, httpStatus.getReasonPhrase(), exceptionMessage);
        return new ResponseEntity<HttpResponse>(httpResponse, httpStatus);
    }
}