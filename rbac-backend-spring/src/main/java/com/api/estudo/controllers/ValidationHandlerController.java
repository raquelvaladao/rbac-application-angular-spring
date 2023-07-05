package com.api.estudo.controllers;

import com.api.estudo.dto.ApiResponse;
import com.api.estudo.exceptions.EntityNotFoundException;
import com.api.estudo.exceptions.ForbiddenException;
import com.api.estudo.exceptions.InvalidInputException;
import org.postgresql.util.PSQLException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class ValidationHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        ApiResponse error = ApiResponse.builder()
                .code("ACESSO_NEGADO")
                .message("Usuário não tem a role necessária para acessar esse recurso")
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Object> handleInvalidInputException(InvalidInputException ex) {
        ApiResponse error = ApiResponse.builder()
                .code("INPUT_INVALIDO")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
        String formattedMessageWithConstraint = getConstraint(message);

        ApiResponse errorMessage = ApiResponse.builder()
                .code("VIOLACAO_DE_SQL")
                .message(formattedMessageWithConstraint)
                .build();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<Object> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        String message = NestedExceptionUtils.getMostSpecificCause(e).getMessage();

        ApiResponse errorMessage = ApiResponse.builder()
                .code("ACESSO_NEGADO")
                .message(message)
                .build();
        return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e) {
        String message = NestedExceptionUtils.getMostSpecificCause(e).getMessage();

        ApiResponse errorMessage = ApiResponse.builder()
                .code("USUARIO_NAO_ENCONTRADO")
                .message(message)
                .build();
        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Object> handleForbiddenException(ForbiddenException e) {
        String message = NestedExceptionUtils.getMostSpecificCause(e).getMessage();

        ApiResponse errorMessage = ApiResponse.builder()
                .code("ACESSO_NAO_PERMITIDO_PARA_ROLE")
                .message(message)
                .build();
        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<Object> handlePSQLException(PSQLException e) {
        String message = NestedExceptionUtils.getMostSpecificCause(e).getMessage();

        ApiResponse errorMessage = ApiResponse.builder()
                .code("OPERACAO_FALHOU")
                .message(message)
                .build();
        return new ResponseEntity<>(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(JpaSystemException.class)
    public ResponseEntity<Object> handleJpaSystemException(JpaSystemException e) {
        String message = NestedExceptionUtils.getMostSpecificCause(e).getMessage();

        ApiResponse errorMessage = ApiResponse.builder()
                .code("OPERACAO_FALHOU")
                .message(message)
                .build();
        return new ResponseEntity<>(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private String getConstraint(String message) {
        String extractedKey = "";
        Pattern pattern = Pattern.compile("\"(.*?)\"");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            extractedKey = " (" + matcher.group(1) + ")";
        }
        return "Uma restrição para uma chave" + extractedKey + " foi encontrada";
    }
}