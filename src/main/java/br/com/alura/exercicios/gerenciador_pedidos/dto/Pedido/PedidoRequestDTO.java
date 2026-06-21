package br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record PedidoRequestDTO(
        @NotNull
        String fornecedor,
        @NotBlank
        List<ItemPedidoRequestDTO> itemPedido,
        LocalDate dataPedido,
        LocalDate dataEntrega
) {
}
