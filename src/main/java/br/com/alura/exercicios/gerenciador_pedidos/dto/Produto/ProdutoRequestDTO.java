package br.com.alura.exercicios.gerenciador_pedidos.dto.Produto;

import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;

import java.math.BigDecimal;

public record ProdutoRequestDTO(
        String nome,
        BigDecimal preco,
        String nomeCategoria,
        String nomeFornecedor
) {}
