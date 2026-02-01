package com.sydorenko.vigvam.manager.configuration;

import com.sun.jdi.request.DuplicateRequestException;
import com.sydorenko.vigvam.manager.dto.response.ErrorResponseDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllExceptions (Exception exception){
        log.error("|?| Unexpected ERROR |?|: ", exception);
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

    @ExceptionHandler(InvalidBearerTokenException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidBearerToken(InvalidBearerTokenException exception){
        log.error("ERROR: incorrect Token JWT: ", exception);
        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                "Необхідно зареєструватися, або повторно увійти в обліковий запис.",
                exception.toString()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthentication( AuthenticationException exception){
        log.error("ERROR: User has not login: ", exception);
        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                exception.getMessage(),
                exception.toString()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthRole(
            AuthorizationDeniedException exception,
            HttpServletRequest request) {

        log.error("Access Denied for user at path: {}", request.getRequestURI());

        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                "Ви не маєте доступу до цих даних, зверніться до адміністратора!",
                "Path: " + request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDto> handleResponseStatus(ResponseStatusException exception) {
        log.error("ResponseStatusException: status {}, reason: {}",
                exception.getStatusCode(), exception.getReason());

        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                exception.getReason(),
                exception.toString()
        );
        return ResponseEntity.status(exception.getStatusCode()).body(error);
    }


    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponseDto> handleNullPointer (NullPointerException exception){
        log.error("ERROR: Attribute is null: ", exception);
        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                "Необхідно заповнити поля",
                exception.toString()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateEntity (DuplicateRequestException exception){
        log.error("ERROR:duplicate entity: ", exception);
        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                exception.getMessage(),
                exception.toString()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

}
