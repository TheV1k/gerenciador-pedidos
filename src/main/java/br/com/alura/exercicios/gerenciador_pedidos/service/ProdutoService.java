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
                .findAllByNomeContainingIgnoreCase(dto.nomeCategoria())
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

    //Cadastra lote de produtos
    public List<ProdutoResponseDTO> cadastrarEmLote(List<ProdutoRequestDTO> dtos) {

        List<Produto> produtos = dtos.stream()
                .map(dto -> {

                    if (dto.preco().compareTo(BigDecimal.ZERO) < 0) {
                        throw new PrecoInvalidoException("Preço não pode ser negativo");
                    }

                    Categoria categoria = repositorioCategoria
                            .findAllByNomeContainingIgnoreCase(dto.nomeCategoria())
                            .stream()
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

                    Fornecedor fornecedor = repositorioFornecedor
                            .findByNomeContainingIgnoreCase(dto.nomeFornecedor())
                            .stream()
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));

                    Produto produto = new Produto(dto.nome(), dto.preco());

                    produto.setCategorias(List.of(categoria));
                    produto.setFornecedor(fornecedor);

                    return produto;
                })
                .toList();

        repositorioProduto.saveAll(produtos);

        return produtos.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    //Converte para ResponseDTO
    private ProdutoResponseDTO toResponseDTO(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getNome(),
                produto.getPreco(),
                produto.getCategorias().get(0).getNome(),
                produto.getFornecedor().getNome()
        );
    }

    //Converte para ResumoDTO

    private  ProdutoResumoDTO toResumoDTO(Produto produto){

        return new ProdutoResumoDTO(
                produto.getNome(),
                produto.getPreco(),
                produto.getCategorias().get(0).getNome(),
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
               repositorioProduto.findByPrecoGreaterThanEqual(valorPesquisado).stream().map(this::toResumoDTO).toList();
    }

    //Busca valores menores do que o pesquisado

    public List<ProdutoResumoDTO> buscarMenoresValores(BigDecimal valorPesquisado){

        return
         repositorioProduto.findByPrecoLessThanEqual(valorPesquisado).stream().map(this::toResumoDTO).toList();

    }

    //Busca os três produtos mais caros

    public List<ProdutoResumoDTO>  tresProdutosMaisCaros() {

        return
                repositorioProduto.findTop3ByOrderByPrecoDesc().stream().map(this::toResumoDTO).toList();

    }

    // Busca os cinco produtos mais baratos de uma categoria

    public List<ProdutoResumoDTO> cincoProdutosMaisBaratosDeUmaCategoria(String categoriaPesquisada) {


       return repositorioProduto
                .findTop5ByCategoriasNomeContainingIgnoreCaseOrderByPrecoAsc(categoriaPesquisada).stream().map(this::toResumoDTO).toList();


    }

    //Busca produto por parte do nome

    public List<ProdutoResumoDTO> buscarParteDoNome(String produtoPesquisado){


              return   repositorioProduto.
                        findByNomeContainingIgnoreCase(produtoPesquisado).stream().map(this::toResumoDTO).toList();


    }

    //Busca os produtos de uma categoria e ordena do menor para o maior valor
     public List<ProdutoResumoDTO> ordenaDoMenorParaOMaior (String categoriaPesquisada) {

      return repositorioProduto
                .findByCategoriasNomeContainingIgnoreCaseOrderByPrecoAsc(categoriaPesquisada).stream().map(this::toResumoDTO).toList();



    }


    //Busca os produtos de uma categoria e ordena do maior para o menor valor

    public List<ProdutoResumoDTO> ordenaDoMaiorParaOMenor(String categoria) {

      return repositorioProduto
                .findByCategoriasNomeContainingIgnoreCaseOrderByPrecoDesc(categoria).stream().map(this::toResumoDTO).toList();



    }


    //Lista produtos por fornecedor

    public List<ProdutoResumoDTO> produtosPorFornecedor(String buscarFornecedor){

        return   repositorioProduto
                .findByFornecedorNomeContainingIgnoreCase(buscarFornecedor).stream().map(this::toResumoDTO).toList();


    }



    //Lista produtos maiores do que determinado valor

    public List<ProdutoResumoDTO> buscaProdutoMaiorQueUmValor(BigDecimal valorPesquisado) {

        return repositorioProduto
                .buscaProdutoMaiorValor(valorPesquisado).stream().map(this::toResumoDTO).toList();


    }

    // Retorna a lista de produtos em ordem crescente
    public List<ProdutoResumoDTO> produtosEmOrdemCrescente() {
      return repositorioProduto
                .produtoValorCrescente().stream().map(this::toResumoDTO).toList();

    }

    // Retorna a lista de produtos em ordem decrescente
    public List<ProdutoResumoDTO> produtosEmOrdemDecrescente() {
       return repositorioProduto
                .produtoValorDecrescente().stream().map(this::toResumoDTO).toList();

    }

    //Busca produtos pela letra inicial
    public List<ProdutoResumoDTO> buscarProdutosPelaLetraInicial(String letra) {

       return repositorioProduto.produtoPelaInicial(letra).stream().map(this::toResumoDTO).toList();

    }


    //Busca produtos por nome ou categoria
    public List<ProdutoResumoDTO> buscarPorProdutoOuCategoria(String pesquisa) {

       return repositorioProduto.filtraNomeOuCategoria(pesquisa).stream().map(this::toResumoDTO).toList();



    }

    //Retorna os cinco produtos mais caros utilizando pesquisa nativa
    public List<ProdutoResumoDTO> buscarCincoMaisCaros() {

        return repositorioProduto.cincoProdutosMaisCaros().stream().map(this::toResumoDTO).toList();
    }

    public ProdutoResponseDTO buscarProdutoPorId(Long id) {

        Produto produto = repositorioProduto.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado."));

        return toResponseDTO(produto);
    }

    public void deletarProduto(Long id) {

        if (!repositorioProduto.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado");
        }

        repositorioProduto.deleteById(id);
    }
}
