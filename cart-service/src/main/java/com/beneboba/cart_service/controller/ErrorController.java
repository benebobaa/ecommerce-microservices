package com.beneboba.cart_service.controller;

import com.beneboba.cart_service.exception.CartNotFound;
import com.beneboba.cart_service.exception.ProductNotFound;
import com.beneboba.cart_service.model.BaseResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<BaseResponse<String>> constraintViolationException(ConstraintViolationException exception){

        log.error("constraintViolationException -> " + exception.getMessage());

        return Mono.just(new BaseResponse<String>(null, exception.getMessage()));
    }

    @ExceptionHandler({CartNotFound.class, ProductNotFound.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<BaseResponse<String>> cartNotFoundException(Exception exception){

        log.error("responseStatusException -> " + exception.getMessage());

        return Mono.just(new BaseResponse<String>(null, exception.getMessage()));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseEntity<BaseResponse<String>>> handleResponseStatusException(ResponseStatusException exception) {
        log.error("responseStatusException -> " + exception.getMessage());

        HttpStatusCode status = exception.getStatusCode();
        BaseResponse<String> response = new BaseResponse<>(null, exception.getReason());
        return Mono.just(new ResponseEntity<>(response, status));
    }
}
