package br.com.alura.exercicios.gerenciador_pedidos.Exceptions;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
