package br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(
        String produto,
        Integer quantidade,
        BigDecimal precoUnitario
) { }
