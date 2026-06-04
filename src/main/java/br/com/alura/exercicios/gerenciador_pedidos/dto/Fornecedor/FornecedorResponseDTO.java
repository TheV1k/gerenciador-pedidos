package br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor;

public record FornecedorResponseDTO(Long id,
                                    String nome,
                                    String cnpj,
                                    String endereco,
                                    String email ){
}
