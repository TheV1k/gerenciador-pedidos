package br.com.alura.exercicios.gerenciador_pedidos.Principal;
import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import br.com.alura.exercicios.gerenciador_pedidos.service.CategoriaService;
import br.com.alura.exercicios.gerenciador_pedidos.service.FornecedorService;
import br.com.alura.exercicios.gerenciador_pedidos.service.PedidoService;
import br.com.alura.exercicios.gerenciador_pedidos.service.ProdutoService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.LongFunction;

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
                    20 - Lista produtos por Fornecedor
                    21 - Listar produtos maiores que um valor 
                    22 - Lista os produtos por ordem de valor crescente
                    23 - Lista os produtos por ordem de valor decrescente
                    24 - Listar Produtos que iniciem com uma determinada letra
                    25 - Calcular a média dos preços dos produtos
                    26 - Buscar o preço máximo de uma categoria
                    27 - Contar produtos das categorias
                    28 - Filtra categoria com mais de dez produtos
                    29 - Filtra produtos por nome ou categoria
                    30 - Busca os cinco produtos mais caros
                    31 - Editar data de entrega do pedido
                    32 - Excluir pedido
                    33 - Excluir fornecedor
                    34 - Excluir Produto
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
                case 20:
                    listarProdutosPorFornecedor();
                    break;
                case 21:
                    listarProdutoMaiorQueUmValor();
                    break;
                case 22:
                    listarProdutosEmOrdemCrescente();
                    break;
                case 23:
                    listarProdutosEmOrdemDecrescente();
                    break;
                case 24:
                    buscarProdutosQueIniciamComALetra();
                    break;
                case 25:
                    calcularMediaDosPrecos();
                    break;
                case 26:
                    buscaPrecoMaximoDaCategoria();
                    break;
                case 27:
                    contarProdutosDeCadaCategoria();
                    break;
                case 28:
                    categoriaComMaisDe10Produtos();
                    break;
                case 29:
                    buscarPorProdutoOuCategoria();
                    break;
                case 30:
                    buscar5MaisCaros();
                    break;
                case 31:
                    editarDataDoPedido();
                    break;
                case 32:
                    deletarPedido();
                    break;
                case 33:
                    deletarFornecedor();
                    break;
                case 34:
                    deletarProduto();
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

        var produtoEncontrado = produtoService.buscarProduto(produtoPesquisado);

        System.out.println(produtoEncontrado);



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

        categoriaService.buscarCategoria(categoriaPesquisada)
                .forEach(System.out::println);
    }

    private void buscarValorMaior(){
        System.out.println("Informe o valor a ser pesquisado: ");
        var valorPesquisado = sc.nextDouble();
        sc.nextLine();

        produtoService
                .buscarValorMaior(valorPesquisado)
                .forEach(System.out::println);

    }

    private void buscarMenoresValores(){
        System.out.println("Informe o valor a ser pesquisado: ");
        var valorPesquisado = sc.nextDouble();
        sc.nextLine();

        produtoService
                .buscarMenoresValores(valorPesquisado)
                .forEach(System.out::println);

    }

    private void buscarParteDoNome(){
        System.out.println("Digite o produto desejado: ");
        var produtoPesquisado = sc.nextLine();
       List<Produto> produtosEncontrados = produtoService
                .buscarParteDoNome(produtoPesquisado);

        System.out.println("Produtos encontrados: ");
        produtosEncontrados.forEach(System.out::println);
    }



    private void buscarpedidosSemData(){

       pedidoService.buscarPedidosSemData().forEach(System.out::println);

    }

    private void buscarpedidosComData(){

       pedidoService.buscarPedidosComData()
               .forEach(System.out::println);

    }

    private void listarPorMaiorValor() {
        System.out.println("Digite a categoria desejada: ");
        var categoriaPesquisada = sc.nextLine();
        produtoService.listarPorMaiorValor(categoriaPesquisada)
                .forEach(System.out::println);

    }

    private void listarPorMenorValor() {
        System.out.println("Digite a categoria desejada: ");
        var categoriaPesquisada = sc.nextLine();
        produtoService.listarPorMenorValor(categoriaPesquisada)
                .forEach(System.out::println);
    }

    private void contarProdutosDeUmaCategoria() {
        System.out.println("Digite a categoria que deseja contar: ");
        var categoriaContada = sc.nextLine();
       Long contagem = produtoService.contarProdutosDeUmaCategoria(categoriaContada);

        System.out.printf("A categoria %s contém %d produtos.\n", categoriaContada, contagem);

    }

    private void pedidosFeitosAntesDeUmaData() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("Digite a data a ser pesquisada: ");
        var pesquisaData = sc.nextLine();
        LocalDate data = LocalDate.parse(pesquisaData, formatter);

        pedidoService.pedidosFeitosAntesDeUmaData(data).forEach(System.out::println);
    }

    private void pedidosFeitosDepoisDeUmaData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("Digite a data a ser pesquisada: ");
        var pesquisaData = sc.nextLine();
        LocalDate data = LocalDate.parse(pesquisaData, formatter);
        pedidoService.pedidosFeitosDepoisDeUmaData(data).forEach(System.out::println);
    }

    private void pedidosFeitosEntreDuasDatas() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("Digite a data inicial: ");
        var pesquisaData = sc.nextLine();
        LocalDate data1 = LocalDate.parse(pesquisaData, formatter);
        System.out.println("Digite a data final: ");
        pesquisaData = sc.nextLine();
        LocalDate data2 = LocalDate.parse(pesquisaData, formatter);
        pedidoService.pedidosFeitosEntreDuasDatas(data1, data2).forEach(System.out::println);
    }

    private void tresProdutosMaisCaros() {
        produtoService.tresProdutosMaisCaros().forEach(System.out::println);
    }

    private void cincoProdutosMaisBaratosDeUmaCategoria() {

        System.out.println("Digite a categoria desejada: ");
        var categoriaPesquisada = sc.nextLine();
        produtoService.cincoProdutosMaisBaratosDeUmaCategoria(categoriaPesquisada).forEach(System.out::println);
    }

    private void listarProdutosPorFornecedor() {

        System.out.println("Digite qual fornecedor você deseja pesquisar: ");
        var buscarFornecedor = sc.nextLine();
        System.out.println("Segue produtos do fornecedor: " + buscarFornecedor);
       List<Produto> produtos = produtoService
                .produtosPorFornecedor(buscarFornecedor);

       produtos.forEach(System.out::println);

    }

    private void listarProdutoMaiorQueUmValor() {
        System.out.println("Informe o valor a ser pesquisado: ");
        var valorPesquisado = sc.nextDouble();
        sc.nextLine();
        produtoService.buscaProdutoMaiorQueUmValor(valorPesquisado)
                .forEach(System.out::println);

    }

    private void listarProdutosEmOrdemCrescente(){
       produtoService
               .produtosEmOrdemCrescente().forEach(System.out::println);
    }

    private void  listarProdutosEmOrdemDecrescente(){
        produtoService
                .produtosEmOrdemDerescente().forEach(System.out::println);
    }

    private void buscarProdutosQueIniciamComALetra(){
        System.out.println("Digite a letra para buscar os produtos: ");
        var letra = sc.nextLine();
        produtoService.buscarProdutosPelaLetraInicial(letra)
                .forEach(System.out::println);
    }

    private void calcularMediaDosPrecos(){

        double mediaDosValores = produtoService
                .calculaMediaDosProdutos();

        System.out.printf("A média do preços dos produtos é: R$%.2f\n", mediaDosValores);

    }

    private void buscaPrecoMaximoDaCategoria(){
        System.out.println("Informe a categoria a ser pesquisada: ");
        var categoriaCalculada = sc.nextLine();

               Double valorMaximo = categoriaService
                        .valorMaximoCategoria(categoriaCalculada);

                System.out.println("O valor máximo na categoria " + categoriaCalculada + " é R$" + valorMaximo);

    }

    private void contarProdutosDeCadaCategoria(){

     List<Object[]> contagemCategoria =  categoriaService.contarProdutosCategorias();

     contagemCategoria
             .stream()
             .toList()
             .forEach(c -> System.out.println("Categoria: " + c[0] + " | " + "Quantidade: " + c[1]));

    }

    private void categoriaComMaisDe10Produtos(){
        List<Object[]> filtraCategoria =  categoriaService
                .categoriaMaisDeDezProdutos();

        filtraCategoria.forEach(c ->{
                    String nomeCategoria = (String) c[0];
                    Long quantidade = (Long) c[1];;

                    System.out.println("Categoria: " + nomeCategoria + " | Quantidade de produtos: " + quantidade);
                });
    }

    private void  buscarPorProdutoOuCategoria(){
        System.out.println("Informe o produto ou categoria a ser pesquisado: ");
        var pesquisa = sc.nextLine();
        List<Produto> produtosEncontrados = produtoService.buscarPorProdutoOuCategoria(pesquisa);

        produtosEncontrados.forEach(System.out::println);
    }

    private void buscar5MaisCaros(){

        List<Produto> cincoMaisCaros = produtoService
                .buscarCincoMaisCaros();

        cincoMaisCaros.forEach(System.out::println);
    }

    private void editarDataDoPedido() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("Informe o ID do pedido: ");
        Long idPedido = sc.nextLong();
        sc.nextLine();
        System.out.println("Informe a de entrega: ");

        String editaData = sc.nextLine();
        LocalDate novaData = null;

        if (!editaData.isBlank()){

            novaData = LocalDate.parse(editaData, formatter);
        }

        pedidoService.editarDataPedido(idPedido, novaData);
        System.out.println("Data de entrega do pedido " +  idPedido +" atualizada para: " + novaData);

    }

    private void deletarPedido() {

        System.out.println("Informe o ID que deseja excluir");
        Long idPedido = sc.nextLong();
        sc.nextLine();
        var confirmacaoExclusao = "N";

        System.out.println("Confirma a exclusão do pedido?");
        confirmacaoExclusao = sc.nextLine().toUpperCase().trim();

            switch (confirmacaoExclusao){

                case "N":
                System.out.println("Solicitação cancelada. O pedido não será cancelado");
                break;
            case "S":
                pedidoService.deletarPedido(idPedido);
                System.out.println(idPedido + " Excluído com sucesso!");
                break;
            default:
                System.out.println("Opção inválida!");
                break;
        }


        }
    private void deletarFornecedor() {

        System.out.println("Informe o Fornecedor que deseja excluir");
        String excluirFornecedor = sc.nextLine();
        var confirmacaoExclusao = "N";

        System.out.println("Confirma a exclusão do fornecedor?");
        confirmacaoExclusao = sc.nextLine().toUpperCase().trim();

        switch (confirmacaoExclusao) {

            case "N":
                System.out.println("Solicitação cancelada.");
                break;
            case "S":
                fornecedorService.excluiFornecedor(excluirFornecedor);
                System.out.println(excluirFornecedor + " Excluído com sucesso!");
                break;
            default:
                System.out.println("Opção inválida!");
                break;
        }
    }
    private void deletarProduto() {

        System.out.println("Informe o Produto que deseja excluir");
        String excluirProduto = sc.nextLine();
        var confirmacaoExclusao = "N";

        System.out.println("Confirma a exclusão de " + excluirProduto + "?");
        confirmacaoExclusao = sc.nextLine().toUpperCase().trim();

        switch (confirmacaoExclusao) {

            case "N":
                System.out.println("Solicitação cancelada.");
                break;
            case "S":
                produtoService.deletaProduto(excluirProduto);
                System.out.println(excluirProduto + " Excluído com sucesso!");
                break;
            default:
                System.out.println("Opção inválida!");
                break;
        }
    }
}




