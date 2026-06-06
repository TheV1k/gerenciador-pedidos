package br.com.alura.exercicios.gerenciador_pedidos.service;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.BusinessRuleException;
import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.ResourceNotFoundException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor.FornecedorResponseDTO;
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
    private final PdfService pdfService;

    public PedidoService(ProdutoRepository repositorioProduto, FornecedorRepository repositorioFornecedor, PedidoRepository repositorioPedido, PdfService pdfService) {
        this.repositorioProduto = repositorioProduto;
        this.repositorioFornecedor = repositorioFornecedor;
        this.repositorioPedido = repositorioPedido;
        this.pdfService = pdfService;
    }


    private PedidoResponseDTO toResponseDTO(Pedido pedido) {

        List<ItemPedidoResponseDTO> itens = pedido.getItens()
                .stream()
                .map(this::toDTO)
                .toList();

        FornecedorResponseDTO fornecedorDTO = new FornecedorResponseDTO(
                pedido.getFornecedor().getId(),
                pedido.getFornecedor().getNome(),
                pedido.getFornecedor().getCnpj(),
                pedido.getFornecedor().getEndereco(),
                pedido.getFornecedor().getEmail()
        );

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getDataPedido(),
                pedido.getDataEntrega(),
                fornecedorDTO,
                pedido.getStatusPedido(),
                pedido.getTotalPedido(),
                itens
        );
    }

    private ItemPedidoResponseDTO toDTO(ItemPedido item) {

        BigDecimal subtotal =
                item.getPrecoUnitario()
                        .multiply(BigDecimal.valueOf(item.getQuantidade()));

        return new ItemPedidoResponseDTO(
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                subtotal
        );
    }
    //Cadastra um novo pedido
    public PedidoResponseDTO cadastrarPedido(PedidoRequestDTO dto) {

        Fornecedor fornecedor = repositorioFornecedor
                .findFirstByNomeContainingIgnoreCase(dto.nomeFornecedor());

        Pedido pedido = new Pedido();
        pedido.setFornecedor(fornecedor);
        pedido.setDataPedido(dto.dataPedido());
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

    //Busca pedidos sem data de entrega
    public List<PedidoResponseDTO> buscarPedidosSemData() {

        List<PedidoResponseDTO> pedidos =
                repositorioPedido.findByDataEntregaIsNull();

        return pedidos;
    }

    //Busca pedidos entregue
    public List<PedidoResponseDTO> buscarPedidosEntregue() {

        List<PedidoResponseDTO> pedidos =
                repositorioPedido.findByDataEntregaIsNotNull();

        return pedidos;
    }

    //Busca pedidos realizados antes de uma data
    public List<PedidoResponseDTO>pedidosFeitosAntesDeUmaData(LocalDate data) {

        List<PedidoResponseDTO> pedidos =
                repositorioPedido.findByDataPedidoBefore(data);

        return pedidos;
    }

    //Busca pedidos entregues antes de uma data
    public List<PedidoResponseDTO>pedidosEntreguesAntesDeUmaData(LocalDate data) {

        List<PedidoResponseDTO> pedidos =
                repositorioPedido.findByDataEntregaBefore(data);

        return pedidos;
    }

    //Busca pedidos feitos após uma data
    public List<PedidoResponseDTO> pedidosFeitosDepoisDeUmaData(LocalDate data) {

        List<PedidoResponseDTO> pedidos =
                repositorioPedido.findByDataPedidoAfter(data);

        return pedidos;
    }

    //Busca pedidos Entregue após uma data
    public List<PedidoResponseDTO> pedidosEntregueDepoisDeUmaData(LocalDate data) {

        List<PedidoResponseDTO> pedidos =
                repositorioPedido.findByDataEntregaAfter(data);

        return pedidos;
    }

    //Busca pedidos feitos entre duas datas
    public List<PedidoResponseDTO> pedidosFeitosEntreDuasDatas(LocalDate dataInicial,
                                                    LocalDate dataFinal) {

        List<PedidoResponseDTO> pedidos =
                repositorioPedido.pedidosFeitosEntreDuasDatas(dataInicial, dataFinal);

        return pedidos;
    }

    //Busca pedidos feitos entre duas datas
    public List<PedidoResponseDTO> pedidosEntreguesEntreDuasDatas(LocalDate dataInicial,
                                                               LocalDate dataFinal) {

        List<PedidoResponseDTO> pedidos =
                repositorioPedido.pedidosEntreguesEntreDuasDatas(dataInicial, dataFinal);

        return pedidos;
    }

    //Receber o pedido
    public PedidoResponseDTO receberPedido(Long idPedido,
                                               LocalDate dataEntrega) {

        Pedido pedido = repositorioPedido.findById(idPedido)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pedido não encontrado"));

        if(dataEntrega.isAfter(LocalDate.now())){
            throw new BusinessRuleException("Data de entrega não pode ser posterior a data atual!");
        }

        if (pedido.getStatusPedido() == Status.ENTREGUE) {
            throw new BusinessRuleException(
                    "Este pedido já foi recebido.");
        }

        pedido.setDataEntrega(dataEntrega);
        pedido.setStatusPedido(Status.ENTREGUE);

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

    public byte[] gerarPdf(Long pedidoId) {

        Pedido pedido =
                repositorioPedido.findById(pedidoId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Pedido não encontrado"
                                )
                        );

        PedidoResponseDTO dto =
                toResponseDTO(pedido);

        return pdfService
                .gerarPedidoPdf(dto);
    }

    public List<PedidoResponseDTO> buscarPedidoPorId(Long id){

        Pedido pedido = repositorioPedido.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        return List.of(toResponseDTO(pedido));
    }
}