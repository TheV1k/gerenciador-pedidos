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
import br.com.alura.exercicios.gerenciador_pedidos.validacoes.ItemPedidoValidator;
import br.com.alura.exercicios.gerenciador_pedidos.validacoes.PedidoValidator;
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
    private final PedidoValidator pedidoValidator;
    private final ItemPedidoValidator itemPedidoValidator;

    public PedidoService(ProdutoRepository repositorioProduto, FornecedorRepository repositorioFornecedor, PedidoRepository repositorioPedido, PdfService pdfService, PedidoValidator pedidoValidator, ItemPedidoValidator itemPedidoValidator) {
        this.repositorioProduto = repositorioProduto;
        this.repositorioFornecedor = repositorioFornecedor;
        this.repositorioPedido = repositorioPedido;
        this.pdfService = pdfService;
        this.pedidoValidator = pedidoValidator;
        this.itemPedidoValidator = itemPedidoValidator;
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
                .findFirstByNomeContainingIgnoreCase(dto.fornecedor());


        pedidoValidator.validarPedido(dto);
        Pedido pedido = new Pedido(dto);
        pedido.setFornecedor(fornecedor);

        List<ItemPedido> itens = new ArrayList<>();
        BigDecimal totalPedido = BigDecimal.ZERO;

        for (ItemPedidoRequestDTO dtoItem : dto.itemPedido()) {

            itemPedidoValidator.validarItensDoPedido(dtoItem);
            Produto produto = Optional.ofNullable(
                            repositorioProduto.findByNomeIgnoreCase(dtoItem.produto()))
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Produto não encontrado: " + dtoItem.produto()));

            ItemPedido item = new ItemPedido(
                    pedido,
                    produto,
                    dtoItem.quantidade());

            totalPedido = totalPedido.add(
                    produto.getPreco()
                            .multiply(BigDecimal.valueOf(dtoItem.quantidade()))
            );

            itens.add(item);
        }

        pedido.setItens(itens);
        pedido.setTotalPedido(totalPedido);

        Pedido pedidoSalvo = repositorioPedido.save(pedido);

        return toResponseDTO(pedidoSalvo);
    }

    //Busca pedidos sem data de entrega
    public List<PedidoResponseDTO> buscarPedidosNaoEntregue() {

       return
                repositorioPedido.findByDataEntregaIsNull().stream().map(this::toResponseDTO).toList();


    }

    //Busca pedidos entregue
    public List<PedidoResponseDTO> buscarPedidosEntregue() {

        return
                repositorioPedido.findByDataEntregaIsNotNull().stream().map(this::toResponseDTO).toList();


    }

    //Busca pedidos realizados antes de uma data
    public List<PedidoResponseDTO>pedidosFeitosAntesDeUmaData(LocalDate data) {

        return
                repositorioPedido.findByDataPedidoBefore(data).stream().map(this::toResponseDTO).toList();


    }

    //Busca pedidos entregues antes de uma data
    public List<PedidoResponseDTO>pedidosEntreguesAntesDeUmaData(LocalDate data) {

      return
                repositorioPedido.findByDataEntregaBefore(data).stream().map(this::toResponseDTO).toList();

    }

    //Busca pedidos feitos após uma data
    public List<PedidoResponseDTO> pedidosFeitosDepoisDeUmaData(LocalDate data) {

        return repositorioPedido.findByDataPedidoAfter(data).stream().map(this::toResponseDTO).toList();


    }

    //Busca pedidos Entregue após uma data
    public List<PedidoResponseDTO> pedidosEntregueDepoisDeUmaData(LocalDate data) {


               return repositorioPedido.findByDataEntregaAfter(data).stream().map(this::toResponseDTO).toList();

    }

    //Busca pedidos feitos entre duas datas
    public List<PedidoResponseDTO> pedidosFeitosEntreDuasDatas(LocalDate dataInicial,
                                                    LocalDate dataFinal) {

     return
                repositorioPedido.pedidosFeitosEntreDuasDatas(dataInicial, dataFinal)
                        .stream()
                        .map(this::toResponseDTO)
                        .toList();


    }

    //Busca pedidos feitos entre duas datas
    public List<PedidoResponseDTO> pedidosEntreguesEntreDuasDatas(LocalDate dataInicial,
                                                               LocalDate dataFinal) {

       return
                repositorioPedido.pedidosEntreguesEntreDuasDatas(dataInicial, dataFinal)
                        .stream()
                        .map(this::toResponseDTO)
                        .toList();


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