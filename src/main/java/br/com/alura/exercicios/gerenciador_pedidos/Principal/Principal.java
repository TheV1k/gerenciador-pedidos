package br.com.alura.exercicios.gerenciador_pedidos.Principal;
import br.com.alura.exercicios.gerenciador_pedidos.models.Categoria;
import br.com.alura.exercicios.gerenciador_pedidos.models.Fornecedor;
import br.com.alura.exercicios.gerenciador_pedidos.repository.CategoriaRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.FornecedorRepository;
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
    private FornecedorRepository repositorioFornecedor;

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

    public Principal(FornecedorRepository repositorioFornecedor) {
        this.repositorioFornecedor = repositorioFornecedor;
    }

    public Principal(ProdutoRepository repositorioProduto, CategoriaRepository repositorioCategoria, FornecedorRepository repositorioFornecedor) {
        this.repositorioProduto = repositorioProduto;
        this.repositorioCategoria = repositorioCategoria;
        this.repositorioFornecedor = repositorioFornecedor;
    }

    public void exibeMenu() {
        var busca = -1;
        while (busca != 0) {
            var menu = """
                    Selecione a opção desejada:
                    
                    1 - Cadastrar Produto
                    2 - Cadastrar Fornecedor
                    3 - Buscar produto pelo nome
                    4 - Buscar categoria
                    5 - Buscar valores maiores do que o fornecido
                    6 - Buscar valores menores do que o fornecido
                    7 - Buscar produto (Usando apenas parte do nome)
                    0 - Sair
                    """;
            System.out.println(menu);
            busca = sc.nextInt();
            sc.nextLine();
            switch (busca) {
                case 1:
                    cadastrarProduto();
                    break;
                case 2:
                    cadastrarFornecedor();
                case 3:
                    buscarProduto();
                    break;
                case 4:
                    buscarCategoria();
                    break;

                case 5:
                    buscarValorMaior();
                    break;
                case 6:
                    buscarMenoresValores();
                    break;
                case 7:
                    buscarParteDoNome();
                    break;
                case 0:
                    System.out.println("Encerrando aplicação");
                    break;
                default:
                    System.out.println("Opção inválida");

            }
        }
    }

    private void cadastrarProduto(){

        System.out.println("Digite o produto");
        String nome = sc.nextLine();
        System.out.println("Digite o valor do produto: ");
        double preco = sc.nextDouble();
        sc.nextLine();
        System.out.println("Informe a categoria do produto: ");
        String nomeCategoria = sc.nextLine();
        System.out.println("Informe o fornecedor do produto: ");
        String nomeFornecedor = sc.nextLine();
        Categoria categoria = repositorioCategoria
                .findByNomeContainingIgnoreCase(nomeCategoria).get(0);
        Fornecedor fornecedor = repositorioFornecedor
                .findByNomeContainingIgnoreCase(nomeFornecedor).get(0);
        Produto produto = new Produto(nome, preco);
        produto.setCategorias(List.of(categoria));
        produto.setFornecedor(fornecedor);
        repositorioProduto.save(produto);

        System.out.println(produto + "\n Cadastrado com sucesso!");


    }

    private void cadastrarFornecedor(){

        System.out.println("Informe o nome do novo fornecedor: ");
        String novoFornecedor = sc.nextLine();
        Fornecedor fornecedorCadastrado = new Fornecedor(novoFornecedor);
        repositorioFornecedor.save(fornecedorCadastrado);
        System.out.println(novoFornecedor + "Cadastrado com sucesso!");
    }

    private void buscarProduto(){
        System.out.println("Digite o produto desejado: ");
        var produtoPesquisado = sc.nextLine();
        Optional<Produto> produtoEncontrado = repositorioProduto.findByNomeEqualsIgnoreCase(produtoPesquisado);
        System.out.println("Produto encontrado: ");
        System.out.println(produtoEncontrado);

    }

    private void buscarCategoria(){
        System.out.println("Digite a categoria desejada: ");
        var categoriaPesquisada = sc.nextLine();
        List<Categoria> produtoPorCategoria = repositorioCategoria.findByNomeContainingIgnoreCase(categoriaPesquisada);
        produtoPorCategoria.forEach(System.out::println);
    }

    private void buscarValorMaior(){
        System.out.println("Informe o valor a ser pesquisado: ");
        var valorPesquisado = sc.nextDouble();
        List<Produto> produtoValorMaior = repositorioProduto.findByPrecoGreaterThanEqual(valorPesquisado);
        System.out.println("Produtos Encontrados: ");
       produtoValorMaior.forEach(System.out::println);
    }

    private void buscarMenoresValores(){
        System.out.println("Informe o valor a ser pesquisado: ");
        var valorPesquisado = sc.nextDouble();
        List<Produto> produtoValorMaior = repositorioProduto.findByPrecoLessThanEqual(valorPesquisado);
        System.out.println("Produtos Encontrados: ");
        produtoValorMaior.forEach(System.out::println);
    }

    private void buscarParteDoNome(){
        System.out.println("Digite o produto desejado: ");
        var produtoPesquisado = sc.nextLine();
        List<Produto> produtoLocalizado = repositorioProduto.findByNomeContainsIgnoreCase(produtoPesquisado);
        System.out.println("Produtos encontrados: ");
        produtoLocalizado.forEach(System.out::println);
    }
}


