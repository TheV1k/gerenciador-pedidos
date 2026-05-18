package br.com.alura.exercicios.gerenciador_pedidos.service;

import br.com.alura.exercicios.gerenciador_pedidos.models.Fornecedor;
import br.com.alura.exercicios.gerenciador_pedidos.models.Pedido;
import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import br.com.alura.exercicios.gerenciador_pedidos.repository.CategoriaRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.FornecedorRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.PedidoRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PedidoService {

    private final ProdutoRepository repositorioProduto;
    private final FornecedorRepository repositorioFornecedor;
    private final PedidoRepository repositorioPedido;

    public PedidoService(ProdutoRepository repositorioProduto,  FornecedorRepository repositorioFornecedor, PedidoRepository repositorioPedido) {
        this.repositorioProduto = repositorioProduto;
        this.repositorioFornecedor = repositorioFornecedor;
        this.repositorioPedido = repositorioPedido;
    }

    //Cadastra um novo pedido
    public Pedido cadastrarPedido(String nomeFornecedor,
                                  String informarData,
                                  List<String> nomesProdutos) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Fornecedor fornecedor = repositorioFornecedor
                .findFirstByNomeContainingIgnoreCase(nomeFornecedor)
                .orElseThrow(() ->
                        new RuntimeException("Fornecedor não encontrado"));

        LocalDate dataEntrega = null;

        if (informarData != null && !informarData.isBlank()) {
            dataEntrega = LocalDate.parse(informarData, formatter);
        }

        Set<Produto> produtosPedido = new HashSet<>();

        for (String nomeProduto : nomesProdutos) {

            Produto produto = repositorioProduto
                    .findByNomeIgnoreCase(nomeProduto);

            produtosPedido.add(produto);
        }

        Pedido pedido = new Pedido();
        pedido.setFornecedor(fornecedor);
        pedido.setData(dataEntrega);
        pedido.setProdutos(produtosPedido);

        return repositorioPedido.save(pedido);
    }

    //Busca pedidos sem data
    public List<Pedido> buscarPedidosSemData() {

        List<Pedido> pedidos =
                repositorioPedido.findByDataIsNull();

        pedidos.forEach(System.out::println);

        return pedidos;
    }

    //Busca pedidos com data
    public List<Pedido> buscarPedidosComData() {

        List<Pedido> pedidos =
                repositorioPedido.findByDataIsNotNull();

        pedidos.forEach(System.out::println);

        return pedidos;
    }

    //Busca pedidos anteriores a uma data
    public List<Pedido>pedidosFeitosAntesDeUmaData(LocalDate data) {

        List<Pedido> pedidos =
                repositorioPedido.findByDataBefore(data);

        pedidos.forEach(System.out::println);

        return pedidos;
    }

    //Busca pedidos feitos após uma data
    public List<Pedido> pedidosFeitosDepoisDeUmaData(LocalDate data) {

        List<Pedido> pedidos =
                repositorioPedido.findByDataAfter(data);

        pedidos.forEach(System.out::println);

        return pedidos;
    }

    //Busca pedidos feitos entre duas datas
    public List<Pedido> pedidosFeitosEntreDuasDatas(LocalDate data1,
                                                    LocalDate data2) {

        List<Pedido> pedidos =
                repositorioPedido.pedidosFeitosEntreDuasDatas(data1, data2);

        pedidos.forEach(System.out::println);

        return pedidos;
    }

    //Edita pedido
    public Pedido editarDataPedido(Long idPedido, LocalDate novaData){

       Optional<Pedido> pedidoOptional = repositorioPedido.findById(idPedido);

       LocalDate dataEntrega = null;

       if(pedidoOptional.isPresent()){

           Pedido pedido = pedidoOptional
                   .get();


           pedido.setData(novaData);

           return repositorioPedido.save(pedido);

       }
return null;

    }

    //Deleta pedido
    public Pedido deletarPedido (long idPedido){


        Optional<Pedido> pedidoOptional = repositorioPedido.findById(idPedido);

        if (pedidoOptional.isPresent()){
            Pedido pedido = pedidoOptional
                    .get();
            repositorioPedido.deleteById(idPedido);

            return pedido;
        }
return null;

    }
}