package br.com.alura.exercicios.gerenciador_pedidos.repository;

import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findByNomeEqualsIgnoreCase(String nome);

    List<Produto> findByPrecoGreaterThanEqual(double valorPesquisado);

    List<Produto> findByPrecoLessThanEqual(double valorPesquisado);

    List<Produto> findByNomeContainsIgnoreCase(String produtoPesquisado);
    Produto findByNomeIgnoreCase(String nome);
}
