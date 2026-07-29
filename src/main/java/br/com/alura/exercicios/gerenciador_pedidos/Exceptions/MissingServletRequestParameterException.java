package br.com.alura.exercicios.gerenciador_pedidos.Exceptions;

public class MissingServletRequestParameterException extends RuntimeException {
  public MissingServletRequestParameterException(String message) {
    super(message);
  }
}
