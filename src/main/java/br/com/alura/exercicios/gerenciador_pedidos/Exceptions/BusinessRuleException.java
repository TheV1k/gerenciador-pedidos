package br.com.alura.exercicios.gerenciador_pedidos.Exceptions;

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
