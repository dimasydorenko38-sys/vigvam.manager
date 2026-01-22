package com.sydorenko.vigvam.manager.configuration;

import com.sydorenko.vigvam.manager.dto.response.ErrorResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllExceptions (Exception exception){
        log.error("|?| Unexpected ERROR |?|: ", exception);

        // TODO: add ErrorEntity this user from Auth Security;

        ErrorResponseDto responseDto = new ErrorResponseDto(
                LocalDateTime.now(),
                "Виникла невідома помилка, спробуйте пізніше, або зверніться до адміністратора",
                exception.toString()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
    }

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<ErrorResponseDto> handleDatabaseConnectionError(DataAccessResourceFailureException exception) {
        log.error("DATABASE CONNECTION LOST: ", exception);

        ErrorResponseDto response = new ErrorResponseDto(
                LocalDateTime.now(),
                "Сервіс тимчасово недоступний (помилка зв'язку з БД).",
                exception.getMostSpecificCause().getMessage()
        );

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleDataUniqueRequest (DataIntegrityViolationException exception){
        log.error("ERROR: The data violates the database conditions: ", exception);
        String fullMessage = exception.getMostSpecificCause().getMessage();
        String userMessage = "Помилка при збереженні даних.";


        if (fullMessage.contains("duplicate key") || fullMessage.contains("AlreadyExists")) {
            userMessage = "Запис із такими даними вже існує в системі.";
        } else if (fullMessage.contains("violates foreign key constraint")) {
            userMessage = "Неможливо виконати дію, це може вплинути на інші дані.";
        } else if (fullMessage.contains("violates not-null constraint")) {
            userMessage = "Не всі обов'язкові поля заповнені.";
        }

        ErrorResponseDto responseDto = new ErrorResponseDto(
                LocalDateTime.now(),
                userMessage,
                fullMessage
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseDto);
    };

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRequest(IllegalArgumentException exception) {
        log.warn("ERROR: BadRequest: ", exception);

        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                exception.getMessage(),
                exception.toString()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFound(EntityNotFoundException exception) {
        log.error("ERROR: Not found Entity: ", exception);
        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                exception.getMessage(),
                exception.toString()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    public ResponseEntity<ErrorResponseDto> handleAuthRole(AuthorizationDeniedException exception){
        log.error("ERROR: Access Denied: ", exception);
        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                "Ви не маєте доступу до цих даних, зверніться до адміністратора для отримання додаткових прав!",
                exception.toString()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

}
