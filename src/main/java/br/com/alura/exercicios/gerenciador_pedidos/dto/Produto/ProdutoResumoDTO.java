package br.com.alura.exercicios.gerenciador_pedidos.dto.Produto;

import java.math.BigDecimal;

public record ProdutoResumoDTO(
        String nome,
        BigDecimal preco,
        String categoria,
        String fornecedor
) {
}
