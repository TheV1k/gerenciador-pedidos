package br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido;

import br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor.FornecedorResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PedidoResponseDTO(
        Long id,
        LocalDate dataPedido,
        LocalDate dataEntrega,
        FornecedorResponseDTO fornecedor,
        br.com.alura.exercicios.gerenciador_pedidos.models.Status status,
        BigDecimal totalPedido,
        List<ItemPedidoResponseDTO> itens
) {
}
