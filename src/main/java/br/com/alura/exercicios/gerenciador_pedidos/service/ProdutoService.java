package br.com.alura.exercicios.gerenciador_pedidos.service;
import br.com.alura.exercicios.gerenciador_pedidos.models.Categoria;
import br.com.alura.exercicios.gerenciador_pedidos.models.Fornecedor;
import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import br.com.alura.exercicios.gerenciador_pedidos.repository.CategoriaRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.FornecedorRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Produto cadastrarProduto(String nome,
                                  Double preco,
                                  String nomeCategoria,
                                  String nomeFornecedor){

        if(preco < 0){
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }

        Categoria categoria = repositorioCategoria
                .findByNomeContainingIgnoreCase(nomeCategoria)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Fornecedor fornecedor = repositorioFornecedor
                .findByNomeContainingIgnoreCase(nomeFornecedor).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        Produto produto = new Produto(nome, preco);

        produto.setCategorias(List.of(categoria));
        produto.setFornecedor(fornecedor);

        return repositorioProduto.save(produto);

    }
    //Busca produto pelo nome

    public Optional<Produto> buscarProduto(String produtoPesquisado){
        return
        repositorioProduto.findByNomeEqualsIgnoreCase(produtoPesquisado);

    }

    //Busca os produtos com valores maiores do que o informado

    public List<Produto> buscarValorMaior(Double valorPesquisado){

       return
               repositorioProduto.findByPrecoGreaterThanEqual(valorPesquisado);
    }

    //Busca valores menores do que o pesquisado

    public List<Produto> buscarMenoresValores(Double valorPesquisado){

        return
         repositorioProduto.findByPrecoLessThanEqual(valorPesquisado);

    }

    //Busca os três produtos mais caros

    public List<Produto>  tresProdutosMaisCaros() {

        return
                repositorioProduto.findTop3ByOrderByPrecoDesc();

    }

    // Busca os cinco produtos mais baratos de uma categoria

    public List<Produto> cincoProdutosMaisBaratosDeUmaCategoria(String categoriaPesquisada) {


        List<Produto> topCincoProdutosMaisBaratos = repositorioProduto
                .findTop5ByCategoriasNomeContainingIgnoreCaseOrderByPrecoAsc(categoriaPesquisada);

      return  topCincoProdutosMaisBaratos;
    }

    //Busca produto por parte do nome

    public List<Produto> buscarParteDoNome(String produtoPesquisado){

        List<Produto> produtoLocalizado =
                repositorioProduto.
                        findByNomeContainingIgnoreCase(produtoPesquisado);

                        produtoLocalizado.forEach(p -> System.out.println("Produtos encontrados: \n" + p));

        return produtoLocalizado;
    }

    //Busca os produtos de uma categoria e ordena do menor para o maior valor
     public List<Produto> listarPorMaiorValor(String categoriaPesquisada) {

        List<Produto> produtoOrdenadoValorMaior = repositorioProduto
                .findByCategoriasNomeContainingIgnoreCaseOrderByPrecoAsc(categoriaPesquisada);

        produtoOrdenadoValorMaior.forEach(System.out::println);

        return produtoOrdenadoValorMaior;

    }


    //Busca os produtos de uma categoria e ordena do maior para o menor valor

    public List<Produto> listarPorMenorValor(String categoria) {

        List<Produto> produtoOrdenadoValorMenor = repositorioProduto
                .findByCategoriasNomeContainingIgnoreCaseOrderByPrecoDesc(categoria);

        produtoOrdenadoValorMenor.forEach(System.out::println);

        return produtoOrdenadoValorMenor;

    }

    //Conta os produtos de uma categoria

    public Long contarProdutosDeUmaCategoria(String categoriaContada) {

        Long contaCategoria = repositorioProduto
                .countByCategoriasNomeContainingIgnoreCase(categoriaContada);

        return contaCategoria;


    }


}
