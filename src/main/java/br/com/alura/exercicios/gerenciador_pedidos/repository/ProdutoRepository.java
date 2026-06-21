package br.com.alura.exercicios.gerenciador_pedidos.repository;

import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResumoDTO;
import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
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
    List<Produto> findByPrecoGreaterThanEqual(BigDecimal valorPesquisado);

    //busca produto com valor menor do que o informado
    List<Produto> findByPrecoLessThanEqual(BigDecimal valorPesquisado);

//Busca por nome
    Produto findByNomeIgnoreCase(String nome);

    List<Produto> findByCategoriasNomeContainingIgnoreCaseOrderByPrecoDesc(String categoriaPesquisada);

    List<Produto> findByCategoriasNomeContainingIgnoreCaseOrderByPrecoAsc(String categoriaPesquisada);

    List<Produto> findByNomeContainingIgnoreCase(String produtoPesquisado);

  //Lista os três produtos mais caros
    List<Produto> findTop3ByOrderByPrecoDesc();

    List<Produto> findTop5ByCategoriasNomeContainingIgnoreCaseOrderByPrecoAsc(String categoriaPesquisada);


    List<Produto> findByFornecedorNomeContainingIgnoreCase(String buscarFornecedor);


    @Query("SELECT p FROM Produto p WHERE p.preco >= :valorPesquisado" )
    List<Produto> buscaProdutoMaiorValor(BigDecimal valorPesquisado);

    @Query("SELECT p FROM Produto p ORDER BY p.preco ASC")
    List<Produto> produtoValorCrescente();

    @Query("SELECT p FROM Produto p ORDER BY p.preco DESC")
    List<Produto> produtoValorDecrescente();

    @Query("SELECT p FROM Produto p WHERE p.nome ILIKE :letra%")
    List<Produto> produtoPelaInicial(String letra);

    @Query("SELECT p FROM Produto p JOIN p.categorias c WHERE p.nome ILIKE :pesquisa OR c.nome ILIKE :pesquisa")
    List<Produto> filtraNomeOuCategoria(@Param("pesquisa") String pesquisa);



    @Query("""
       SELECT p
       FROM Produto p
       ORDER BY p.preco DESC
       LIMIT 5
       """)
    List<Produto> cincoProdutosMaisCaros();
}
