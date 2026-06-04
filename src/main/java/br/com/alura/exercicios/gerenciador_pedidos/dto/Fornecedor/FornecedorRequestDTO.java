package br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor;

public record FornecedorRequestDTO(
        String nome,
        String cnpj,
        String endereco,
        String email) {
}
