package br.com.alura.exercicios.gerenciador_pedidos.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dataPedido;
    @Column(name = "data_entrega", nullable = true)
    private LocalDate dataEntrega;
    @Enumerated(EnumType.STRING)
    private Status statusPedido;
    private BigDecimal totalPedido;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens;

    public Pedido() {
    }

    public Pedido(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public Status getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(Status statusPedido) {
        this.statusPedido = statusPedido;
    }

    public BigDecimal getTotalPedido() {
        return totalPedido;
    }

    public void setTotalPedido(BigDecimal totalPedido) {
        this.totalPedido = totalPedido;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    @PrePersist
    public void prePersist() {
        if (dataPedido == null){
            dataPedido = LocalDate.now();
        }

    }
}
