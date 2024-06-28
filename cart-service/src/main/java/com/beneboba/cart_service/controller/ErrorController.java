package com.beneboba.cart_service.controller;

import com.beneboba.cart_service.exception.CartNotFound;
import com.beneboba.cart_service.model.BaseResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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

    @ExceptionHandler(CartNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<BaseResponse<String>> cartNotFoundException(CartNotFound exception){

        log.error("responseStatusException -> " + exception.getMessage());

        return Mono.just(new BaseResponse<String>(null, exception.getMessage()));
    }
}
