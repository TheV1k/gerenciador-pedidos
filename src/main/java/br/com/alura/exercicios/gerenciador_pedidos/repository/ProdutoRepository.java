package br.com.alura.exercicios.gerenciador_pedidos.repository;

import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResumoDTO;
import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Produto findByNomeEqualsIgnoreCase(String nome);

    List<ProdutoResumoDTO> findByPrecoGreaterThanEqual(BigDecimal valorPesquisado);

    List<ProdutoResumoDTO> findByPrecoLessThanEqual(BigDecimal valorPesquisado);


    Produto findByNomeIgnoreCase(String nome);

    List<ProdutoResumoDTO> findByCategoriasNomeContainingIgnoreCaseOrderByPrecoDesc(String categoriaPesquisada);

    List<ProdutoResumoDTO> findByCategoriasNomeContainingIgnoreCaseOrderByPrecoAsc(String categoriaPesquisada);

    List<ProdutoResumoDTO> findByNomeContainingIgnoreCase(String produtoPesquisado);
    Long countByCategoriasNomeContainingIgnoreCase(String nome);

    List<ProdutoResumoDTO> findTop3ByOrderByPrecoDesc();

    List<ProdutoResumoDTO> findTop5ByCategoriasNomeContainingIgnoreCaseOrderByPrecoAsc(String categoriaPesquisada);


    List<ProdutoResumoDTO> findByFornecedorNomeContainingIgnoreCase(String buscarFornecedor);

    Produto findByNome(String excluiProduto);

    @Query("SELECT p FROM Produto p WHERE p.preco >= :valorPesquisado" )
    List<ProdutoRequestDTO> buscaProdutoMaiorValor(Double valorPesquisado);

    @Query("SELECT p FROM Produto p ORDER BY p.preco ASC")
    List<ProdutoResumoDTO> produtoValorCrescente();

    @Query("SELECT p FROM Produto p ORDER BY p.preco DESC")
    List<ProdutoResumoDTO> produtoValorDecrescente();

    @Query("SELECT p FROM Produto p WHERE p.nome ILIKE :letra%")
    List<ProdutoResumoDTO> produtoPelaInicial(String letra);

    @Query("SELECT p FROM Produto p JOIN p.categorias c WHERE p.nome ILIKE :pesquisa OR c.nome ILIKE :pesquisa")
    List<ProdutoResumoDTO> filtraNomeOuCategoria(@Param("pesquisa") String pesquisa);

    @Query(value = "select * from produtos ORDER BY valor DESC LIMIT 5", nativeQuery = true)
    List<ProdutoResponseDTO> cincoProudutosMaisCaros();

    @Query("SELECT AVG(p.preco) FROM Produto p")
    BigDecimal mediaDosProdutos();

    void delete(Optional<Produto> produto);

    List<ProdutoResumoDTO> buscaProdutoMaiorValor(BigDecimal valorPesquisado);

    List<ProdutoResumoDTO> cincoProdutosMaisCaros();
}
