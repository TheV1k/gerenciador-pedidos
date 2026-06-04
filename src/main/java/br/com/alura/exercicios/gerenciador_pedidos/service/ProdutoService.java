package br.com.alura.exercicios.gerenciador_pedidos.service;
import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.PrecoInvalidoException;
import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.ResourceNotFoundException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResumoDTO;
import br.com.alura.exercicios.gerenciador_pedidos.models.Categoria;
import br.com.alura.exercicios.gerenciador_pedidos.models.Fornecedor;
import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import br.com.alura.exercicios.gerenciador_pedidos.repository.CategoriaRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.FornecedorRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository repositorioProduto;
    private final CategoriaRepository repositorioCategoria;
    private final FornecedorRepository repositorioFornecedor;

    public ProdutoService(ProdutoRepository repositorioProduto, CategoriaRepository repositorioCategoria, FornecedorRepository repositorioFornecedor) {
        this.repositorioProduto = repositorioProduto;
        this.repositorioCategoria = repositorioCategoria;
        this.repositorioFornecedor = repositorioFornecedor;
    }

    //Método para cadastro de produtos

    public ProdutoResponseDTO cadastrarProduto(ProdutoRequestDTO dto){

        if(dto.preco().compareTo(BigDecimal.ZERO) < 0){
            throw new PrecoInvalidoException("Preço não pode ser negativo");
        }

        Categoria categoria = repositorioCategoria
                .findByNomeContainingIgnoreCase(dto.nomeCategoria())
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        Fornecedor fornecedor = repositorioFornecedor
                .findByNomeContainingIgnoreCase(dto.nomeFornecedor()).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));

        Produto produto = new Produto(dto.nome(), dto.preco());

        produto.setCategorias(List.of(categoria));
        produto.setFornecedor(fornecedor);
        repositorioProduto.save(produto);

        return toResponseDTO(produto);
    }

    private ProdutoResponseDTO toResponseDTO(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getNome(),
                produto.getPreco(),
                produto.getCategorias().getFirst().getNome(),
                produto.getFornecedor().getNome()
        );
    }

    //Busca produto pelo nome

    public ProdutoResponseDTO buscarProduto(String produtoPesquisado) {

        Produto produto = repositorioProduto
                .findByNomeEqualsIgnoreCase(produtoPesquisado);

        return toResponseDTO(produto);
    }

    //Busca os produtos com valores maiores do que o informado

    public List<ProdutoResumoDTO> buscarValorMaior(BigDecimal valorPesquisado){

       return
               repositorioProduto.findByPrecoGreaterThanEqual(valorPesquisado);
    }

    //Busca valores menores do que o pesquisado

    public List<ProdutoResumoDTO> buscarMenoresValores(BigDecimal valorPesquisado){

        return
         repositorioProduto.findByPrecoLessThanEqual(valorPesquisado);

    }

    //Busca os três produtos mais caros

    public List<ProdutoResumoDTO>  tresProdutosMaisCaros() {

        return
                repositorioProduto.findTop3ByOrderByPrecoDesc();

    }

    // Busca os cinco produtos mais baratos de uma categoria

    public List<ProdutoResumoDTO> cincoProdutosMaisBaratosDeUmaCategoria(String categoriaPesquisada) {


        List<ProdutoResumoDTO> topCincoProdutosMaisBaratos = repositorioProduto
                .findTop5ByCategoriasNomeContainingIgnoreCaseOrderByPrecoAsc(categoriaPesquisada);

      return  topCincoProdutosMaisBaratos;
    }

    //Busca produto por parte do nome

    public List<ProdutoResumoDTO> buscarParteDoNome(String produtoPesquisado){

        List<ProdutoResumoDTO> produtoLocalizado =
                repositorioProduto.
                        findByNomeContainingIgnoreCase(produtoPesquisado);

        return produtoLocalizado;
    }

    //Busca os produtos de uma categoria e ordena do menor para o maior valor
     public List<ProdutoResumoDTO> listarPorMaiorValor(String categoriaPesquisada) {

        List<ProdutoResumoDTO> produtoOrdenadoValorMaior = repositorioProduto
                .findByCategoriasNomeContainingIgnoreCaseOrderByPrecoAsc(categoriaPesquisada);

        return produtoOrdenadoValorMaior;

    }


    //Busca os produtos de uma categoria e ordena do maior para o menor valor

    public List<ProdutoResumoDTO> listarPorMenorValor(String categoria) {

        List<ProdutoResumoDTO> produtoOrdenadoValorMenor = repositorioProduto
                .findByCategoriasNomeContainingIgnoreCaseOrderByPrecoDesc(categoria);

        return produtoOrdenadoValorMenor;

    }

    //Conta os produtos de uma categoria

    public Long contarProdutosDeUmaCategoria(String categoriaContada) {

        Long contaCategoria = repositorioProduto
                .countByCategoriasNomeContainingIgnoreCase(categoriaContada);

        return contaCategoria;


    }

    //Lista produtos por fornecedor

    public List<ProdutoResumoDTO> produtosPorFornecedor(String buscarFornecedor){

        List<ProdutoResumoDTO> produtoPorFornecedor = repositorioProduto
                .findByFornecedorNomeContainingIgnoreCase(buscarFornecedor);

        return  produtoPorFornecedor;
    }

    public void deletarProduto(Long id) {

        if (!repositorioProduto.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado");
        }

        repositorioProduto.deleteById(id);
    }

    //Lista produtos maiores do que determinado valor

    public List<ProdutoResumoDTO> buscaProdutoMaiorQueUmValor(BigDecimal valorPesquisado) {

        List<ProdutoResumoDTO> produtosMaiorValor = repositorioProduto
                .buscaProdutoMaiorValor(valorPesquisado);
        return produtosMaiorValor;

    }

    // Retorna a lista de produtos em ordem crescente
    public List<ProdutoResumoDTO> produtosEmOrdemCrescente() {
        List<ProdutoResumoDTO> produtoEmOrdemCrescente = repositorioProduto
                .produtoValorCrescente();
        return  produtoEmOrdemCrescente;
    }

    // Retorna a lista de produtos em ordem decrescente
    public List<ProdutoResumoDTO> produtosEmOrdemDecrescente() {
        List<ProdutoResumoDTO> produtoEmOrdemDecrescente = repositorioProduto
                .produtoValorDecrescente();
        return  produtoEmOrdemDecrescente;
    }

    //Busca produtos pela letra inicial
    public List<ProdutoResumoDTO> buscarProdutosPelaLetraInicial(String letra) {

        List<ProdutoResumoDTO> produtoPelaInicial = repositorioProduto.produtoPelaInicial(letra);
        return produtoPelaInicial;
    }

    //Calcula a média de valor de todos os produtos
    public BigDecimal calculaMediaDosProdutos() {

        return repositorioProduto.mediaDosProdutos();
    }

    //Busca produtos por ome ou categoria
    public List<ProdutoResumoDTO> buscarPorProdutoOuCategoria(String pesquisa) {

        List<ProdutoResumoDTO> buscaNomeOuCategoria = repositorioProduto.filtraNomeOuCategoria(pesquisa);

        return buscaNomeOuCategoria;

    }

    //Retorna os cinco produtos mais caros utilizando pesquisa nativa
    public List<ProdutoResumoDTO> buscarCincoMaisCaros() {

        return repositorioProduto.cincoProdutosMaisCaros();
    }
}
