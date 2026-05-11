package br.com.alura.exercicios.gerenciador_pedidos.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome")
    private String nome;

    @ManyToMany(mappedBy = "categorias", fetch = FetchType.EAGER)
    private List <Produto> produtos = new ArrayList<>();



    public Categoria(){}

    public Categoria(String nome) {
        this.nome = nome;
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

    @Override
    public String toString() {
        return "id: " + id +
                "nome: " + nome + '\'' +
                "produto: " + produtos;
    }
}
