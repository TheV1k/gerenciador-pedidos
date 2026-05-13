package br.com.alura.exercicios.gerenciador_pedidos.Principal;
import br.com.alura.exercicios.gerenciador_pedidos.models.Categoria;
import br.com.alura.exercicios.gerenciador_pedidos.models.Fornecedor;
import br.com.alura.exercicios.gerenciador_pedidos.models.Pedido;
import br.com.alura.exercicios.gerenciador_pedidos.repository.*;
import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import br.com.alura.exercicios.gerenciador_pedidos.repository.ProdutoRepository;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Principal {
    Scanner sc = new Scanner(System.in);
    private ProdutoRepository repositorioProduto;
    private CategoriaRepository repositorioCategoria;
    private FornecedorRepository repositorioFornecedor;
    private PedidoRepository repositorioPedido;

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

    public Principal(ProdutoRepository repositorioProduto, CategoriaRepository repositorioCategoria, FornecedorRepository repositorioFornecedor, PedidoRepository repositorioPedido) {
        this.repositorioProduto = repositorioProduto;
        this.repositorioCategoria = repositorioCategoria;
        this.repositorioFornecedor = repositorioFornecedor;
        this.repositorioPedido = repositorioPedido;
    }

    public void exibeMenu() {
        var busca = -1;
        while (busca != 0) {
            var menu = """
                    Selecione a opção desejada:
                    
                    1 - Cadastrar Produto
                    2 - Cadastrar Fornecedor
                    3 - Cadastrar Pedido
                    4 - Buscar produto pelo nome
                    5 - Buscar categoria
                    6 - Buscar valores maiores do que o fornecido
                    7 - Buscar valores menores do que o fornecido
                    8 - Buscar produto (Usando apenas parte do nome)
                    9 - Listar pedidos sem data de entrega
                    10 -Listar pedidos com data de entrega
                    11 -Listar produtos de uma categoria (Valor Crescente) 
                    12 -Listar produtos de uma categoria (Valor Crescente)
                    13 - Contar produtos de uma categoria
                    14 - Buscar pedidos feitos antes de uma data
                    15 - Pedidos feitos após uma data
                    16 - Pedidos feitos entre duas datas
                    17 - Buscar os três produtos mais caros
                    18 - Cinco Produtos Mais baratos de uma categoria
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
                    buscarProduto();
                    break;
                case 5:
                    buscarCategoria();
                    break;

                case 6:
                    buscarValorMaior();
                    break;
                case 7:
                    buscarMenoresValores();
                    break;
                case 8:
                    buscarParteDoNome();
                    break;
                case 9:
                    buscarpedidosSemData();
                    break;
                case 10:
                    buscarpedidosComData();
                    break;

                case 11:
                    listarPorMaiorValor();
                    break;
                case 12:
                    listarPorMenorValor();
                    break;
                case 13:
                    contarProdutosDeUmaCategoria();
                    break;
                case 14:
                    pedidosFeitosAntesDeUmaData();
                    break;
                case 15:
                    pedidosFeitosDepoisDeUmaData();
                    break;
                case 16:
                    pedidosFeitosEntreDuasDatas();
                    break;
                case 17:
                    tresProdutosMaisCaros();
                    break;
                case 18:
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

    private void cadastrarPedido(){

        Pedido pedido = new Pedido();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("Informe o fornecedor: ");
        String informarFornecedor = sc.nextLine();
        Fornecedor fornecedor = repositorioFornecedor
                .findFirstByNomeContainingIgnoreCase(informarFornecedor)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        LocalDate dataEntrega = null;

        System.out.println("Informe a data de entrega: ");
        String informarData = sc.nextLine();
        if (!informarData.isBlank()){
            dataEntrega = LocalDate.parse(informarData, formatter);
        }

        Set<Produto> novoPedido = new HashSet<>();
        String inserirNovo = "S";

        while (inserirNovo.equalsIgnoreCase("S")) {
            System.out.println("Adicione o produto: ");
            String inserirProduto = sc.nextLine();

            Produto produto = repositorioProduto
                    .findByNomeIgnoreCase(inserirProduto);

            novoPedido.add(produto);

            System.out.println("Deseja inserir um novo produto? (S/N)");
            inserirNovo = sc.nextLine();

            pedido.setFornecedor(fornecedor);
            pedido.setData(dataEntrega);
            pedido.setProdutos(novoPedido);


        }
        repositorioPedido.save(pedido);
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
        List<Produto> produtoLocalizado = repositorioProduto.findByNomeContainingIgnoreCase(produtoPesquisado);
        System.out.println("Produtos encontrados: ");
        produtoLocalizado.forEach(System.out::println);
    }

    private void buscarpedidosSemData(){

        List<Pedido> pedidosSemDataEntrga = repositorioPedido.findByDataIsNull();
        System.out.println("Produtos encontrados: ");
        pedidosSemDataEntrga.forEach(System.out::println);

    }

    private void buscarpedidosComData(){

        List<Pedido> pedidosSemDataEntrga = repositorioPedido.findByDataIsNotNull();
        System.out.println("Produtos encontrados: ");
        pedidosSemDataEntrga.forEach(System.out::println);

    }

    private void listarPorMaiorValor() {
        System.out.println("Digite a categoria desejada: ");
        var categoriaPesquisada = sc.nextLine();
        List<Produto> produtoOrdenadoValorMaior = repositorioProduto.findByCategoriasNomeContainingIgnoreCaseOrderByPrecoAsc(categoriaPesquisada);
        produtoOrdenadoValorMaior.forEach(System.out::println);

    }

    private void listarPorMenorValor() {
        System.out.println("Digite a categoria desejada: ");
        var categoriaPesquisada = sc.nextLine();
        List<Produto> produtoOrdenadoValorMaior = repositorioProduto.findByCategoriasNomeContainingIgnoreCaseOrderByPrecoDesc(categoriaPesquisada);
        produtoOrdenadoValorMaior.forEach(System.out::println);
    }

    private void contarProdutosDeUmaCategoria() {
        System.out.println("Digite a categoria que deseja contar: ");
        var categoriaContada = sc.nextLine();
        Long contaCategoria = repositorioProduto.countByCategoriasNomeContainingIgnoreCase(categoriaContada);
        System.out.println("A categoria " + categoriaContada.toLowerCase() + " contém  " + contaCategoria + " produtos.");

    }

    private void pedidosFeitosAntesDeUmaData() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("Digite a data a ser pesquisada: ");
        var pesquisaData = sc.nextLine();
        LocalDate data = LocalDate.parse(pesquisaData, formatter);
        List<Pedido> pedidosAnteriores = repositorioPedido.findByDataIsBefore(data);
        System.out.println(pedidosAnteriores);
    }

    private void pedidosFeitosDepoisDeUmaData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("Digite a data a ser pesquisada: ");
        var pesquisaData = sc.nextLine();
        LocalDate data = LocalDate.parse(pesquisaData, formatter);
        List<Pedido> pedidosPosteriores = repositorioPedido.findByDataIsAfter(data);
        System.out.println(pedidosPosteriores);
    }

    private void pedidosFeitosEntreDuasDatas() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("Digite a data inicial: ");
        var pesquisaData = sc.nextLine();
        LocalDate data1 = LocalDate.parse(pesquisaData, formatter);
        System.out.println("Digite a data final: ");
        pesquisaData = sc.nextLine();
        LocalDate data2 = LocalDate.parse(pesquisaData, formatter);
        List<Pedido> pedidosEntreDatas = repositorioPedido.findByDataIsBetween(data1, data2);
        System.out.println(pedidosEntreDatas);
    }

    private void tresProdutosMaisCaros() {
        List<Produto> treProdutosMaisCaros = repositorioProduto.findTop3ByOrderByPrecoDesc();
        System.out.println(treProdutosMaisCaros);
    }

    private void cincoProdutosMaisBaratosDeUmaCategoria() {

        System.out.println("Digite a categoria desejada: ");
        var categoriaPesquisada = sc.nextLine();
        List<Produto> topCincoProdutosMaisBaratos = repositorioProduto.findTop5ByCategoriasNomeContainingIgnoreCaseOrderByPrecoAsc(categoriaPesquisada);
        topCincoProdutosMaisBaratos.forEach(System.out::println);
    }


}


