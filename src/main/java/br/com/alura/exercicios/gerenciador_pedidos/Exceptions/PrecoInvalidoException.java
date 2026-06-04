package br.com.alura.exercicios.gerenciador_pedidos.Exceptions;

public class PrecoInvalidoException extends RuntimeException {
    public PrecoInvalidoException(String message) {
        super(message);
    }
}
