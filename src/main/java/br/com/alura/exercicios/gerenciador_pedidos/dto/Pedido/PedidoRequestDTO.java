package br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido;
import java.time.LocalDate;
import java.util.List;

public record PedidoRequestDTO(
        String nomeFornecedor,
        List<ItemPedidoRequestDTO> itemPedido,
        LocalDate dataPedido,
        LocalDate dataEntrega
) {
}
