package br.com.alura.exercicios.gerenciador_pedidos.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Pedido pedido;

    @ManyToOne
    private Produto produto;

    private Integer quantidade;

    private BigDecimal precoUnitario;

    public ItemPedido(Pedido pedido, Produto produto, Integer quantidade) {
        this.pedido = pedido;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}