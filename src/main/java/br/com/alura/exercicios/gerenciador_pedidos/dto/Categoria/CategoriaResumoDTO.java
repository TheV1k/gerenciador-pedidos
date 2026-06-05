package br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria;

import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResumoDTO;

import java.util.List;

public record CategoriaResumoDTO(String nome,
                                 List<ProdutoResumoDTO> produtos) {
}
