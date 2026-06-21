package br.com.alura.exercicios.gerenciador_pedidos.Exceptions;

import br.com.alura.exercicios.gerenciador_pedidos.models.Pedido;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Não encontrado",  ex.getMessage(), request);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateResourceException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Solicitação duplicada",  ex.getMessage(), request);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessRuleException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Regra de negócio violada", ex.getMessage(), request);
    }

    @ExceptionHandler(PrecoInvalidoException.class)
    public ResponseEntity<ErrorResponse> handlePreco(PrecoInvalidoException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Preço não pode ser negativo", ex.getMessage(), request);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorResponse> handleData(InvalidDataException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNPROCESSABLE_CONTENT, "Dados inválidos", ex.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String error, String message, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                error,
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(errorResponse);
    }
}
