package br.com.alura.exercicios.gerenciador_pedidos.Principal;
import br.com.alura.exercicios.gerenciador_pedidos.models.Categoria;
import br.com.alura.exercicios.gerenciador_pedidos.repository.CategoriaRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.ProdutoRepository;

import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import br.com.alura.exercicios.gerenciador_pedidos.repository.ProdutoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;



public class Principal {
    Scanner sc = new Scanner(System.in);
    private ProdutoRepository repositorioProduto;
    private CategoriaRepository repositorioCategoria;

    public Principal(ProdutoRepository repositorioProduto) {
        this.repositorioProduto = repositorioProduto;
    }

    public Principal(CategoriaRepository repositorioCategoria) {
        this.repositorioCategoria = repositorioCategoria;
    }

    public Principal(ProdutoRepository repositorioProduto, CategoriaRepository repositorioCategoria) {
        this.repositorioProduto = repositorioProduto;
        this.repositorioCategoria = repositorioCategoria;
    }

    public void exibeMenu() {
        var busca = -1;
        while (busca != 0) {
            var menu = """
                    Selecione a opção desejada:
                    
                    1 - Buscar produto pelo nome
                    2 - Buscar categoria
                    3 - Buscar valores maiores do que o fornecido
                    0 - Sair
                    """;
            System.out.println(menu);
            busca = sc.nextInt();
            sc.nextLine();
            switch (busca) {
                case 1:
                    buscarProduto();
                    break;
                case 2:
                    buscarCategoria();
                    break;

                case 3:
                    buscarValorMaior();
                    break;
                case 0:
                    System.out.println("Encerrando aplicação");
                    break;

            }
        }
    }

    private void buscarProduto(){
        System.out.println("Digite o produto desejado: ");
        var produtoPesquisado = sc.nextLine();
        Optional<Produto> produtoEncontrado = repositorioProduto.findByNomeContainingIgnoreCase(produtoPesquisado);
        System.out.println("Produto encontrado: ");
        System.out.println(produtoEncontrado);

    }

    private void buscarCategoria(){
        System.out.println("Digite a categoria desejada: ");
        var categoriaPesquisada = sc.nextLine();
        List<Categoria> produtoPorCategoria = repositorioCategoria.findByNomeContainingIgnoreCase(categoriaPesquisada);
        produtoPorCategoria.forEach(p ->
                System.out.println(produtoPorCategoria));
    }

    private void buscarValorMaior(){
        System.out.println("Informe o valor a ser pesquisado: ");
        var valorPesquisado = sc.nextDouble();
        List<Produto> produtoValorMaior = repositorioProduto.findByPrecoGreaterThanEqual(valorPesquisado);
        System.out.println("Produtos Encontrados: ");
       produtoValorMaior.forEach(System.out::println);
    }
}


