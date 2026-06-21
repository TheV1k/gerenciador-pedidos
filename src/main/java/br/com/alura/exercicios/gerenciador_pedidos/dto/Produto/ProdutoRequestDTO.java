package br.com.alura.exercicios.gerenciador_pedidos.dto.Produto;

import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProdutoRequestDTO(
        @NotBlank
        String nome,
        @NotNull
        @Positive
        BigDecimal preco,
        @NotNull
        @NotBlank
        String nomeCategoria,
        @NotNull
        String nomeFornecedor
) {}
