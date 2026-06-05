package br.com.alura.exercicios.gerenciador_pedidos.repository;

import br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria.CategoriaResumoDTO;
import br.com.alura.exercicios.gerenciador_pedidos.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<CategoriaResumoDTO> findByNomeContainingIgnoreCase(String categoria);

    List<Categoria> findAllByNomeContainingIgnoreCase(String nome);

    @Query("SELECT MAX(p.preco) " +
            "FROM Produto p " +
            "JOIN p.categorias c " +
            "WHERE c.nome = :categoria")
    BigDecimal calculaValorMaximo(@Param("categoria") String categoriaCalculada);

    @Query("SELECT c.nome, COUNT (p) " +
            "FROM Categoria c " +
            "JOIN c.produtos p " +
            "GROUP BY c.nome")
    List<CategoriaResumoDTO> contarProdutosCategoria();

    @Query("SELECT c.nome, COUNT (p) " +
            "FROM Categoria c " +
            "JOIN c.produtos p " +
            "GROUP BY c.nome " +
            "HAVING COUNT (p) > 10")
    List<CategoriaResumoDTO> categoriaMaisDeDezProdutos();
}
