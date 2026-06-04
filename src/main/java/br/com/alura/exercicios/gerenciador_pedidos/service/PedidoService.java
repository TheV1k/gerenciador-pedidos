package br.com.alura.exercicios.gerenciador_pedidos.service;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.ResourceNotFoundException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.ItemPedidoRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.ItemPedidoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.PedidoRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.PedidoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.models.*;
import br.com.alura.exercicios.gerenciador_pedidos.repository.FornecedorRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.PedidoRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

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


    private PedidoResponseDTO toResponseDTO(Pedido pedido) {

        List<ItemPedidoResponseDTO> itens = pedido.getItens()
                .stream()
                .map(item -> new ItemPedidoResponseDTO(
                        item.getProduto().getNome(),
                        item.getQuantidade(),
                        item.getPrecoUnitario()
                ))
                .toList();

        return new PedidoResponseDTO(
                pedido.getDataPedido(),
                pedido.getDataEntrega(),
                pedido.getStatusPedido(),
                pedido.getTotalPedido(),
                itens
        );
    }

    //Cadastra um novo pedido
    public PedidoResponseDTO cadastrarPedido(PedidoRequestDTO dto) {

        Fornecedor fornecedor = repositorioFornecedor
                .findFirstByNomeContainingIgnoreCase(dto.nomeFornecedor());

        Pedido pedido = new Pedido();
        pedido.setFornecedor(fornecedor);
        pedido.setDataPedido(dto.dataPedido());
        pedido.setDataEntrega(dto.dataEntrega());
        pedido.setStatusPedido(Status.ENVIADO);

        List<ItemPedido> itens = new ArrayList<>();
        BigDecimal totalPedido = BigDecimal.ZERO;

        for (ItemPedidoRequestDTO dtoItem : dto.itemPedido()) {

            Produto produto = Optional.ofNullable(
                            repositorioProduto.findByNomeIgnoreCase(dtoItem.produto()))
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Produto não encontrado: "
                                            + dtoItem.produto()));

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(dtoItem.quantidade());
            item.setPrecoUnitario(produto.getPreco());

            BigDecimal subtotal = produto.getPreco()
                    .multiply(BigDecimal.valueOf(dtoItem.quantidade()));

            totalPedido = totalPedido.add(subtotal);

            itens.add(item);
        }

        pedido.setItens(itens);
        pedido.setTotalPedido(totalPedido);

        Pedido pedidoSalvo = repositorioPedido.save(pedido);

        return toResponseDTO(pedidoSalvo);
    }

    //Busca pedidos sem data
    public List<PedidoResponseDTO> buscarPedidosSemData() {

        List<PedidoResponseDTO> pedidos =
                repositorioPedido.findByDataIsNull();

        return pedidos;
    }

    //Busca pedidos com data
    public List<PedidoResponseDTO> buscarPedidosComData() {

        List<PedidoResponseDTO> pedidos =
                repositorioPedido.findByDataIsNotNull();

        return pedidos;
    }

    //Busca pedidos anteriores a uma data
    public List<PedidoResponseDTO>pedidosFeitosAntesDeUmaData(LocalDate data) {

        List<PedidoResponseDTO> pedidos =
                repositorioPedido.findByDataBefore(data);

        return pedidos;
    }

    //Busca pedidos feitos após uma data
    public List<PedidoResponseDTO> pedidosFeitosDepoisDeUmaData(LocalDate data) {

        List<PedidoResponseDTO> pedidos =
                repositorioPedido.findByDataAfter(data);

        return pedidos;
    }

    //Busca pedidos feitos entre duas datas
    public List<PedidoResponseDTO> pedidosFeitosEntreDuasDatas(LocalDate data1,
                                                    LocalDate data2) {

        List<PedidoResponseDTO> pedidos =
                repositorioPedido.pedidosFeitosEntreDuasDatas(data1, data2);

        return pedidos;
    }

    //Edita pedido
    public PedidoResponseDTO editarDataEntrega(Long idPedido,
                                               LocalDate novaData) {

        Pedido pedido = repositorioPedido.findById(idPedido)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pedido não encontrado"));

        pedido.setDataEntrega(novaData);

        Pedido pedidoAtualizado = repositorioPedido.save(pedido);

        return toResponseDTO(pedidoAtualizado);
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