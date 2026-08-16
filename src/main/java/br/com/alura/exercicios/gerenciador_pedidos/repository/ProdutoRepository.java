package br.com.alura.exercicios.gerenciador_pedidos.repository;

import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

   //Busca produto por ome
    Optional<Produto> findByNomeEqualsIgnoreCase(String nome);

    //Busca com valor maior do que o informado
    List<Produto> findByPrecoGreaterThanEqual(BigDecimal valorPesquisado, Pageable pageable);

    //busca produto com valor menor do que o informado
    List<Produto> findByPrecoLessThanEqual(BigDecimal valorPesquisado, Pageable pageable);

//Busca por nome
    Produto findByNomeIgnoreCase(String nome);

    List<Produto> findByCategoriasNomeContainingIgnoreCaseOrderByPrecoDesc(String categoriaPesquisada, Pageable pageable);

    List<Produto> findByCategoriasNomeContainingIgnoreCaseOrderByPrecoAsc(String categoriaPesquisada, Pageable pageable);

    List<Produto> findByNomeContainingIgnoreCase(String produtoPesquisado, Pageable pageable);

  //Lista os três produtos mais caros
    List<Produto> findTop3ByOrderByPrecoDesc();

    List<Produto> findTop5ByCategoriasNomeContainingIgnoreCaseOrderByPrecoAsc(String categoriaPesquisada);


    List<Produto> findByFornecedorNomeContainingIgnoreCase(String buscarFornecedor, Pageable pageable);


    @Query("SELECT p FROM Produto p WHERE p.preco >= :valorPesquisado" )
    List<Produto> buscaProdutoMaiorValor(BigDecimal valorPesquisado, Pageable pageable);

    @Query("SELECT p FROM Produto p ORDER BY p.preco ASC")
    List<Produto> produtoValorCrescente(Pageable pageable);

    @Query("SELECT p FROM Produto p ORDER BY p.preco DESC")
    List<Produto> produtoValorDecrescente(Pageable pageable);

    @Query("SELECT p FROM Produto p WHERE p.nome ILIKE :letra%")
    List<Produto> produtoPelaInicial(String letra, Pageable pageable);

    @Query("SELECT p FROM Produto p JOIN p.categorias c WHERE p.nome ILIKE :pesquisa OR c.nome ILIKE :pesquisa")
    List<Produto> filtraNomeOuCategoria(@Param("pesquisa") String pesquisa, Pageable pageable);



    @Query("""
       SELECT p
       FROM Produto p
       ORDER BY p.preco DESC
       LIMIT 5
       """)
    List<Produto> cincoProdutosMaisCaros();

    boolean existsByNomeIgnoreCase(@NotBlank String nome);

    List<Produto> findByNomeIgnoreCaseIn(List<String> nomesProdutos);
}
