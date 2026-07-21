package br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoriaRequestDTO(@NotBlank
@NotNull
                                  String nome) {
}
