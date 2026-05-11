package br.com.alura.exercicios.gerenciador_pedidos.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produtos")
public class Produto {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome", unique = true)
    private String nome;
    @Column(name = "valor")
    private Double preco;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fornecedor_id")
    Fornecedor fornecedor;


    @ManyToMany
    @JoinTable(
            name = "produto_categoria",
            joinColumns = @JoinColumn(name = "produtos_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )

    private List<Categoria> categorias = new ArrayList<>();

    public Produto(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public  Produto(){}
    public Produto(String nome, Double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    @Override
    public String toString() {
        return
                "id: " + id + " | " +
                " nome: " + nome  + " | " +
                " preco: " + preco
                ;
    }
}
