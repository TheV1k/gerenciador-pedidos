package br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record PedidoRequestDTO(
        @NotNull
        String fornecedor,
        @NotEmpty(message = "A lista de itens não pode estar vazia")
        @Valid
        List<ItemPedidoRequestDTO> itemPedido,
        LocalDate dataPedido,
        LocalDate dataEntrega
) {
}
