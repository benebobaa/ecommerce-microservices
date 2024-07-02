package com.beneboba.payment_service.exception;

import com.beneboba.payment_service.model.event.BaseResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class  GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<BaseResponse<Void>> constraintViolationException(ConstraintViolationException exception){

        log.error("constraintViolationException -> " + exception.getMessage());

        return Mono.just(new BaseResponse<>(null, exception.getMessage()));
    }

    @ExceptionHandler({TransactionNotFound.class, OrderNotFound.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<BaseResponse<Void>> transactionNotFoundException(Exception exception){

        log.error("transactionNotFoundException -> " + exception.getMessage());

        return Mono.just(new BaseResponse<>(null, exception.getMessage()));
    }
}
