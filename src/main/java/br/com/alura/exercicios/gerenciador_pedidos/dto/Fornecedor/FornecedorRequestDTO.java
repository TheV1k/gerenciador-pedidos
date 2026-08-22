package br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record FornecedorRequestDTO(

        @NotBlank
        String nome,
        @NotBlank
        String cnpj,
        @NotBlank
        String endereco,
        @NotBlank
        @Email
        String email) {
}
