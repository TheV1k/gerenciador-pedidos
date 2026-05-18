package br.com.alura.exercicios.gerenciador_pedidos.repository;

import br.com.alura.exercicios.gerenciador_pedidos.models.Categoria;
import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByNomeContainingIgnoreCase(String categoriaPesquisada);

    @Query("SELECT MAX(p.preco) " +
            "FROM Produto p " +
            "JOIN p.categorias c " +
            "WHERE c.nome = :categoria")
    Double calculaValorMaximo(@Param("categoria") String categoriaCalculada);

    @Query("SELECT c.nome, COUNT (p) " +
            "FROM Categoria c " +
            "JOIN c.produtos p " +
            "GROUP BY c.nome")
    List<Object[]> contarProdutosCategoria();

    @Query("SELECT c.nome, COUNT (p) " +
            "FROM Categoria c " +
            "JOIN c.produtos p " +
            "GROUP BY c.nome " +
            "HAVING COUNT (p) > 10")
    List<Object[]> categoriaMaisDeDezProdutos();
}
