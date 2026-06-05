package br.com.alura.exercicios.gerenciador_pedidos.models;

import java.util.Map;

public enum Status {

    ENVIADO("Pedido Enviado"),
    ENTREGUE("Pedido Entregue");

    private String descricao;

    Status(String descricao) {
        this.descricao = descricao;
    }

    Status(){}

    public static final Map<String, Status> MAPA_STATUS = Map.of(
            "Pedido Enviado", ENVIADO,
            "Pedido Entregue", ENTREGUE
    );
}
