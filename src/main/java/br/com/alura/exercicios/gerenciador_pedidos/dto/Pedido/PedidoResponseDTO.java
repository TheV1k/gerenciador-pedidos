package br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PedidoResponseDTO(
        LocalDate dataPedido,
        LocalDate dataEntrega,
        br.com.alura.exercicios.gerenciador_pedidos.models.Status fornecedor,
        BigDecimal totalPedido,
        List<ItemPedidoResponseDTO> itens
) {
}
