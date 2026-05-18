package br.com.alura.exercicios.gerenciador_pedidos.repository;

import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findByNomeEqualsIgnoreCase(String nome);

    List<Produto> findByPrecoGreaterThanEqual(double valorPesquisado);

    List<Produto> findByPrecoLessThanEqual(double valorPesquisado);


    Produto findByNomeIgnoreCase(String nome);

    List<Produto> findByCategoriasNomeContainingIgnoreCaseOrderByPrecoDesc(String categoriaPesquisada);

    List<Produto> findByCategoriasNomeContainingIgnoreCaseOrderByPrecoAsc(String categoriaPesquisada);

    List<Produto> findByNomeContainingIgnoreCase(String produtoPesquisado);
    Long countByCategoriasNomeContainingIgnoreCase(String nome);

    List<Produto> findTop3ByOrderByPrecoDesc();

    List<Produto> findTop5ByCategoriasNomeContainingIgnoreCaseOrderByPrecoAsc(String categoriaPesquisada);


    List<Produto> findByFornecedorNomeContainingIgnoreCase(String buscarFornecedor);

    Produto findByNome(String excluiProduto);

    @Query("SELECT p FROM Produto p WHERE p.preco >= :valorPesquisado" )
    List<Produto> buscaProdutoMaiorValor(Double valorPesquisado);

    @Query("SELECT p FROM Produto p ORDER BY p.preco ASC")
    List<Produto> produtoValorCrescente();

    @Query("SELECT p FROM Produto p ORDER BY p.preco DESC")
    List<Produto> produtoValorDecrescente();

    @Query("SELECT p FROM Produto p WHERE p.nome ILIKE :letra%")
    List<Produto> produtoPelaInicial(String letra);

    @Query("SELECT p.preco FROM Produto p ORDER BY p.preco")
    List<Double> mediaDosProdutos();

    @Query("SELECT p FROM Produto p JOIN p.categorias c WHERE p.nome ILIKE :pesquisa OR c.nome ILIKE :pesquisa")
    List<Produto> filtraNomeOuCategoria(@Param("pesquisa") String pesquisa);

    @Query(value = "select * from produtos ORDER BY valor DESC LIMIT 5", nativeQuery = true)
    List<Produto> cincoProudutosMaisCaros();
}
