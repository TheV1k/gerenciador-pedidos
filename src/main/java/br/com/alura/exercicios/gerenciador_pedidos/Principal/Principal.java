package br.com.alura.exercicios.gerenciador_pedidos.Principal;
import br.com.alura.exercicios.gerenciador_pedidos.service.CategoriaService;
import br.com.alura.exercicios.gerenciador_pedidos.service.FornecedorService;
import br.com.alura.exercicios.gerenciador_pedidos.service.PedidoService;
import br.com.alura.exercicios.gerenciador_pedidos.service.ProdutoService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class Principal {

   private final ProdutoService produtoService;
   private final CategoriaService categoriaService;
   private final PedidoService pedidoService;
   private final FornecedorService fornecedorService;
    Scanner sc = new Scanner(System.in);


    public Principal(ProdutoService produtoService, CategoriaService categoriaService, PedidoService pedidoService, FornecedorService fornecedorService) {
        this.produtoService = produtoService;
        this.categoriaService = categoriaService;
        this.pedidoService = pedidoService;
        this.fornecedorService = fornecedorService;
    }

    public void exibeMenu() {
        var busca = -1;
        while (busca != 0) {
            var menu = """
                    Selecione a opção desejada:
                    
                    1 - Cadastrar Produto
                    2 - Cadastrar Fornecedor
                    3 - Cadastrar Pedido
                    4 - Cadastrar Categoria
                    5 - Buscar produto pelo nome
                    6 - Buscar categoria
                    7 - Buscar valores maiores do que o fornecido
                    8 - Buscar valores menores do que o fornecido
                    9 - Buscar produto (Usando apenas parte do nome)
                    10 - Listar pedidos sem data de entrega
                    11 -Listar pedidos com data de entrega
                    12 -Listar produtos de uma categoria (Valor Crescente) 
                    13 -Listar produtos de uma categoria (Valor Decrescente)
                    14 - Contar produtos de uma categoria
                    15 - Buscar pedidos feitos antes de uma data
                    16 - Pedidos feitos após uma data
                    17 - Pedidos feitos entre duas datas
                    18 - Buscar os três produtos mais caros
                    19 - Cinco Produtos Mais baratos de uma categoria
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
                    break;
                case 3:
                    cadastrarPedido();
                    break;
                case 4:
                    cadastrarCategoria();
                    break;
                case 5:
                    buscarProduto();
                    break;
                case 6:
                    buscarCategoria();
                    break;
                case 7:
                    buscarValorMaior();
                    break;
                case 8:
                    buscarMenoresValores();
                    break;
                case 9:
                    buscarParteDoNome();
                    break;
                case 10:
                    buscarpedidosSemData();
                    break;
                case 11:
                    buscarpedidosComData();
                    break;

                case 12:
                    listarPorMaiorValor();
                    break;
                case 13:
                    listarPorMenorValor();
                    break;
                case 14:
                    contarProdutosDeUmaCategoria();
                    break;
                case 15:
                    pedidosFeitosAntesDeUmaData();
                    break;
                case 16:
                    pedidosFeitosDepoisDeUmaData();
                    break;
                case 17:
                    pedidosFeitosEntreDuasDatas();
                    break;
                case 18:
                    tresProdutosMaisCaros();
                    break;
                case 19:
                    cincoProdutosMaisBaratosDeUmaCategoria();
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

        produtoService.cadastrarProduto(nome,preco, nomeCategoria, nomeFornecedor);

        System.out.println(nome + " cadastrado com sucesso!");


    }

    private void cadastrarFornecedor(){

        System.out.println("Informe o nome do novo fornecedor: ");
        String novoFornecedor = sc.nextLine();

        fornecedorService.cadastrarFornecedor(novoFornecedor);
        System.out.println(novoFornecedor +" cadastrado com sucesso.");

    }

    private void cadastrarCategoria() {
        System.out.println("Digite a categoria: ");
        var nomeCategoria = sc.nextLine();

        categoriaService.cadastrarCategoria(nomeCategoria);
        System.out.println(nomeCategoria + " cadastrada com sucesso.");
    }

    private void buscarProduto(){
        System.out.println("Digite o produto desejado: ");
        var produtoPesquisado = sc.nextLine();

        produtoService.buscarProduto(produtoPesquisado);

    }



    private void cadastrarPedido() {

        System.out.println("Informe o fornecedor:");
        String informarFornecedor = sc.nextLine();

        System.out.println("Informe a data de entrega (dd/MM/yyyy):");
        String informarData = sc.nextLine();

        List<String> produtos = new ArrayList<>();

        String inserirNovo = "S";

        while (inserirNovo.equalsIgnoreCase("S")) {

            System.out.println("Adicione o produto:");
            String inserirProduto = sc.nextLine();

            produtos.add(inserirProduto);

            System.out.println("Deseja inserir um novo produto? (S/N)");
            inserirNovo = sc.nextLine();
        }

        pedidoService.cadastrarPedido(
                informarFornecedor,
                informarData,
                produtos
        );

        System.out.println("\nPedido cadastrado com sucesso!");
    }

    private void buscarCategoria(){
        System.out.println("Digite a categoria desejada: ");
        var categoriaPesquisada = sc.nextLine();

        categoriaService.buscarCategoria(categoriaPesquisada);
    }

    private void buscarValorMaior(){
        System.out.println("Informe o valor a ser pesquisado: ");
        var valorPesquisado = sc.nextDouble();
        sc.nextLine();

        produtoService.buscarValorMaior(valorPesquisado);

    }

    private void buscarMenoresValores(){
        System.out.println("Informe o valor a ser pesquisado: ");
        var valorPesquisado = sc.nextDouble();
        sc.nextLine();

        produtoService.buscarMenoresValores(valorPesquisado);

    }

    private void buscarParteDoNome(){
        System.out.println("Digite o produto desejado: ");
        var produtoPesquisado = sc.nextLine();
        produtoService.buscarParteDoNome(produtoPesquisado);
    }

    private void buscarpedidosSemData(){

       pedidoService.buscarPedidosSemData();

    }

    private void buscarpedidosComData(){

       pedidoService.buscarPedidosComData();

    }

    private void listarPorMaiorValor() {
        System.out.println("Digite a categoria desejada: ");
        var categoriaPesquisada = sc.nextLine();
        produtoService.listarPorMaiorValor(categoriaPesquisada);

    }

    private void listarPorMenorValor() {
        System.out.println("Digite a categoria desejada: ");
        var categoriaPesquisada = sc.nextLine();
        produtoService.listarPorMenorValor(categoriaPesquisada);
    }

    private void contarProdutosDeUmaCategoria() {
        System.out.println("Digite a categoria que deseja contar: ");
        var categoriaContada = sc.nextLine();
        produtoService.contarProdutosDeUmaCategoria(categoriaContada);

    }

    private void pedidosFeitosAntesDeUmaData() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("Digite a data a ser pesquisada: ");
        var pesquisaData = sc.nextLine();
        LocalDate data = LocalDate.parse(pesquisaData, formatter);

        pedidoService.pedidosFeitosAntesDeUmaData(data);
    }

    private void pedidosFeitosDepoisDeUmaData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("Digite a data a ser pesquisada: ");
        var pesquisaData = sc.nextLine();
        LocalDate data = LocalDate.parse(pesquisaData, formatter);
        pedidoService.pedidosFeitosDepoisDeUmaData(data);
    }

    private void pedidosFeitosEntreDuasDatas() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("Digite a data inicial: ");
        var pesquisaData = sc.nextLine();
        LocalDate data1 = LocalDate.parse(pesquisaData, formatter);
        System.out.println("Digite a data final: ");
        pesquisaData = sc.nextLine();
        LocalDate data2 = LocalDate.parse(pesquisaData, formatter);
        pedidoService.pedidosFeitosEntreDuasDatas(data1, data2);
    }

    private void tresProdutosMaisCaros() {
        produtoService.tresProdutosMaisCaros();
    }

    private void cincoProdutosMaisBaratosDeUmaCategoria() {

        System.out.println("Digite a categoria desejada: ");
        var categoriaPesquisada = sc.nextLine();
        produtoService.cincoProdutosMaisBaratosDeUmaCategoria(categoriaPesquisada);
    }


}


