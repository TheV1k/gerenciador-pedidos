package br.com.alura.exercicios.gerenciador_pedidos.dto.Produto;

import java.math.BigDecimal;

public record ProdutoResponseDTO(
        String nome,
        BigDecimal preco,
        String nomeCategoria,
        String nomeFornecedor
) {
}
