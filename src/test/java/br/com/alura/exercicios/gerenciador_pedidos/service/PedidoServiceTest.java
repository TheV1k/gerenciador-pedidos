package br.com.alura.exercicios.gerenciador_pedidos.service;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.BusinessRuleException;
import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.ResourceNotFoundException;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {



    @InjectMocks
    private PedidoService service;

    @Mock
    private FornecedorRepository repositorioFornecedor;
    @Mock
    private ProdutoRepository repositorioProduto;
    @Mock
    private PedidoRepository repositorioPedido;
    @Mock
    private PedidoValidator pedidoValidator;
    @Mock
    private ItemPedidoValidator itemPedidoValidator;

    @Mock
    private PdfService pdfService;


    @Test
    void deveCriarPedido(){

        //ARRANGE

        ItemPedidoRequestDTO dtoItem = new ItemPedidoRequestDTO("Arroz", 2);

        List<String> nomeEsperado = List.of("Arroz");

        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO("Alfa Fornecedora", List.of(dtoItem), LocalDate.now(), null);

        Fornecedor mockFornecedor = new Fornecedor();
        mockFornecedor.setNome("Alfa Fornecedora");

        Produto mockProduto = new Produto();
        mockProduto.setNome("Arroz");
        mockProduto.setPreco(new BigDecimal("25.5"));

        Pedido mockPedidoSalvo = new Pedido(pedidoRequestDTO);
        mockPedidoSalvo.setId(1L);
        mockPedidoSalvo.setFornecedor(mockFornecedor);

        ItemPedido itemPedido = new ItemPedido(mockPedidoSalvo, mockProduto, 2);
        itemPedido.setPrecoUnitario(mockProduto.getPreco());

        mockPedidoSalvo.setItens(List.of(itemPedido));
        mockPedidoSalvo.setTotalPedido(new BigDecimal("51.0"));

        BDDMockito.when(repositorioFornecedor.findFirstByNomeContainingIgnoreCase("Alfa Fornecedora")).thenReturn(mockFornecedor);
        BDDMockito.when(repositorioProduto.findByNomeIgnoreCaseIn(nomeEsperado)).thenReturn(List.of(mockProduto));
        BDDMockito.when(repositorioPedido.save(any(Pedido.class))).thenReturn(mockPedidoSalvo);

        //ACT

        PedidoResponseDTO resultado = service.cadastrarPedido(pedidoRequestDTO);

        //ASSERT
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Alfa Fornecedora", resultado.fornecedor().nome());
        Assertions.assertEquals(new BigDecimal("51.0"), resultado.totalPedido());
        Assertions.assertEquals(LocalDate.now(), resultado.dataPedido());
        Assertions.assertEquals(1, resultado.itens().size());

        ItemPedidoResponseDTO itemResultado = resultado.itens().get(0);
        assertEquals("Arroz", itemResultado.produto());
        assertEquals(2, itemResultado.quantidade());
        assertEquals(new BigDecimal("25.5"), itemResultado.precoUnitario());
        assertEquals(new BigDecimal("51.0"), itemResultado.subtotal());

        BDDMockito.verify(pedidoValidator, Mockito.times(1)).validarPedido(pedidoRequestDTO);
        BDDMockito.verify(itemPedidoValidator, Mockito.times(1)).validarItensDoPedido(dtoItem);
        BDDMockito.verify(repositorioPedido, Mockito.times(1)).save(any(Pedido.class));


    }

    @Test
    void deveLancarExcecaoProdutoNaoEncontrado(){

        //ARRANGE

        ItemPedidoRequestDTO dtoItem = new ItemPedidoRequestDTO("Arroz", 2);

        List<String> nomeEsperado = List.of("Arroz");

        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO("Alfa Fornecedora", List.of(dtoItem), LocalDate.now(), null);

        Fornecedor mockFornecedor = new Fornecedor();
        mockFornecedor.setNome("Alfa Fornecedora");
        

        BDDMockito.when(repositorioFornecedor.findFirstByNomeContainingIgnoreCase("Alfa Fornecedora")).thenReturn(mockFornecedor);
        BDDMockito.when(repositorioProduto.findByNomeIgnoreCaseIn(nomeEsperado)).thenReturn(List.of());


        //ACT

        assertThrows(ResourceNotFoundException.class, () -> service.cadastrarPedido(pedidoRequestDTO));

        //ASSERT

        BDDMockito.verify(pedidoValidator, Mockito.times(1)).validarPedido(pedidoRequestDTO);
        BDDMockito.verify(itemPedidoValidator, Mockito.times(1)).validarItensDoPedido(dtoItem);
        BDDMockito.verify(repositorioPedido, Mockito.never()).save(any(Pedido.class));


    }

    @Test
    void deverEncontrarPedidoSemDataDeEntrega(){

        //ARRANGE

        ItemPedidoRequestDTO dtoItem = new ItemPedidoRequestDTO("Arroz", 2);

        List<String> nomeEsperado = List.of("Arroz");

        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO("Alfa Fornecedora", List.of(dtoItem), LocalDate.now(), null);

        Fornecedor mockFornecedor = new Fornecedor();
        mockFornecedor.setNome("Alfa Fornecedora");

        Produto mockProduto = new Produto();
        mockProduto.setNome("Arroz");
        mockProduto.setPreco(new BigDecimal("25.5"));

        Pedido mockPedidoSalvo = new Pedido(pedidoRequestDTO);
        mockPedidoSalvo.setId(1L);
        mockPedidoSalvo.setFornecedor(mockFornecedor);

        ItemPedido itemPedido = new ItemPedido(mockPedidoSalvo, mockProduto, 2);
        itemPedido.setPrecoUnitario(mockProduto.getPreco());

        mockPedidoSalvo.setItens(List.of(itemPedido));
        mockPedidoSalvo.setTotalPedido(new BigDecimal("51.0"));

        BDDMockito.given(repositorioPedido.findByDataEntregaIsNull()).willReturn(List.of(mockPedidoSalvo));

        //ACT

       List <PedidoResponseDTO> resultado = service.buscarPedidosNaoEntregue();

       //ASSERT

        Assertions.assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L,resultado.get(0).id());
        assertEquals("Alfa Fornecedora", resultado.get(0).fornecedor().nome());
        BDDMockito.verify(repositorioPedido, BDDMockito.times(1)).findByDataEntregaIsNull();

    }

    @Test
    void deverEncontrarPedidoComDataDeEntrega(){

        //ARRANGE

        ItemPedidoRequestDTO dtoItem = new ItemPedidoRequestDTO("Arroz", 2);

        List<String> nomeEsperado = List.of("Arroz");

        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO("Alfa Fornecedora", List.of(dtoItem), LocalDate.now(), LocalDate.of(2026,07,31));

        Fornecedor mockFornecedor = new Fornecedor();
        mockFornecedor.setNome("Alfa Fornecedora");

        Produto mockProduto = new Produto();
        mockProduto.setNome("Arroz");
        mockProduto.setPreco(new BigDecimal("25.5"));

        Pedido mockPedidoSalvo = new Pedido(pedidoRequestDTO);
        mockPedidoSalvo.setId(1L);
        mockPedidoSalvo.setFornecedor(mockFornecedor);

        ItemPedido itemPedido = new ItemPedido(mockPedidoSalvo, mockProduto, 2);
        itemPedido.setPrecoUnitario(mockProduto.getPreco());

        mockPedidoSalvo.setItens(List.of(itemPedido));
        mockPedidoSalvo.setTotalPedido(new BigDecimal("51.0"));

        BDDMockito.given(repositorioPedido.findByDataEntregaIsNotNull()).willReturn(List.of(mockPedidoSalvo));

        //ACT

        List <PedidoResponseDTO> resultado = service.buscarPedidosEntregue();

        //ASSERT

        Assertions.assertNotNull(resultado);
        assertEquals(1L,resultado.get(0).id());
        assertEquals("Alfa Fornecedora", resultado.get(0).fornecedor().nome());
        BDDMockito.verify(repositorioPedido, BDDMockito.times(1)).findByDataEntregaIsNotNull();

    }

    @Test
    void deverEncontrarPedidoFeitoAntesDeUmaData(){

        //ARRANGE

        ItemPedidoRequestDTO dtoItem = new ItemPedidoRequestDTO("Arroz", 2);

        List<String> nomeEsperado = List.of("Arroz");

        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO("Alfa Fornecedora", List.of(dtoItem), LocalDate.now(), LocalDate.of(2026,07,31));

        Fornecedor mockFornecedor = new Fornecedor();
        mockFornecedor.setNome("Alfa Fornecedora");

        Produto mockProduto = new Produto();
        mockProduto.setNome("Arroz");
        mockProduto.setPreco(new BigDecimal("25.5"));

        Pedido mockPedidoSalvo = new Pedido(pedidoRequestDTO);
        mockPedidoSalvo.setId(1L);
        mockPedidoSalvo.setFornecedor(mockFornecedor);

        ItemPedido itemPedido = new ItemPedido(mockPedidoSalvo, mockProduto, 2);
        itemPedido.setPrecoUnitario(mockProduto.getPreco());

        mockPedidoSalvo.setItens(List.of(itemPedido));
        mockPedidoSalvo.setTotalPedido(new BigDecimal("51.0"));

        LocalDate dataPesquisa = LocalDate.of(2026,12,31);

        BDDMockito.given(repositorioPedido.findByDataPedidoBefore(dataPesquisa))
                .willReturn(List.of(mockPedidoSalvo));

        //ACT

        List <PedidoResponseDTO> resultado = service.pedidosFeitosAntesDeUmaData(dataPesquisa);

        //ASSERT

        Assertions.assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L,resultado.get(0).id());
        assertEquals("Alfa Fornecedora", resultado.get(0).fornecedor().nome());
        BDDMockito.verify(repositorioPedido, BDDMockito.times(1)).findByDataPedidoBefore(dataPesquisa);

    }

    @Test
    void deverEncontrarPedidoEntregueAntesDeUmaData(){

        //ARRANGE

        ItemPedidoRequestDTO dtoItem = new ItemPedidoRequestDTO("Arroz", 2);

        List<String> nomeEsperado = List.of("Arroz");

        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO("Alfa Fornecedora", List.of(dtoItem), LocalDate.now(), LocalDate.of(2026,07,31));

        Fornecedor mockFornecedor = new Fornecedor();
        mockFornecedor.setNome("Alfa Fornecedora");

        Produto mockProduto = new Produto();
        mockProduto.setNome("Arroz");
        mockProduto.setPreco(new BigDecimal("25.5"));

        Pedido mockPedidoSalvo = new Pedido(pedidoRequestDTO);
        mockPedidoSalvo.setId(1L);
        mockPedidoSalvo.setFornecedor(mockFornecedor);

        ItemPedido itemPedido = new ItemPedido(mockPedidoSalvo, mockProduto, 2);
        itemPedido.setPrecoUnitario(mockProduto.getPreco());

        mockPedidoSalvo.setItens(List.of(itemPedido));
        mockPedidoSalvo.setTotalPedido(new BigDecimal("51.0"));

        LocalDate dataPesquisa = LocalDate.of(2026,12,31);

        BDDMockito.given(repositorioPedido.findByDataEntregaBefore(dataPesquisa))
                .willReturn(List.of(mockPedidoSalvo));

        //ACT

        List <PedidoResponseDTO> resultado = service.pedidosEntreguesAntesDeUmaData(dataPesquisa);

        //ASSERT

        Assertions.assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L,resultado.get(0).id());
        assertEquals("Alfa Fornecedora", resultado.get(0).fornecedor().nome());
        BDDMockito.verify(repositorioPedido, BDDMockito.times(1)).findByDataEntregaBefore(dataPesquisa);

    }

    @Test
    void deverEncontrarPedidoFeitoDepoisDeUmaData(){

        //ARRANGE

        ItemPedidoRequestDTO dtoItem = new ItemPedidoRequestDTO("Arroz", 2);

        List<String> nomeEsperado = List.of("Arroz");

        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO("Alfa Fornecedora", List.of(dtoItem), LocalDate.now(), LocalDate.of(2026,07,31));

        Fornecedor mockFornecedor = new Fornecedor();
        mockFornecedor.setNome("Alfa Fornecedora");

        Produto mockProduto = new Produto();
        mockProduto.setNome("Arroz");
        mockProduto.setPreco(new BigDecimal("25.5"));

        Pedido mockPedidoSalvo = new Pedido(pedidoRequestDTO);
        mockPedidoSalvo.setId(1L);
        mockPedidoSalvo.setFornecedor(mockFornecedor);

        ItemPedido itemPedido = new ItemPedido(mockPedidoSalvo, mockProduto, 2);
        itemPedido.setPrecoUnitario(mockProduto.getPreco());

        mockPedidoSalvo.setItens(List.of(itemPedido));
        mockPedidoSalvo.setTotalPedido(new BigDecimal("51.0"));

        LocalDate dataPesquisa = LocalDate.of(2026,12,31);

        BDDMockito.given(repositorioPedido.findByDataPedidoAfter(dataPesquisa))
                .willReturn(List.of(mockPedidoSalvo));

        //ACT

        List <PedidoResponseDTO> resultado = service.pedidosFeitosDepoisDeUmaData(dataPesquisa);

        //ASSERT

        Assertions.assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L,resultado.get(0).id());
        assertEquals("Alfa Fornecedora", resultado.get(0).fornecedor().nome());
        BDDMockito.verify(repositorioPedido, BDDMockito.times(1)).findByDataPedidoAfter(dataPesquisa);

    }

    @Test
    void deverEncontrarPedidoEntregueDepoisDeUmaData(){

        //ARRANGE

        ItemPedidoRequestDTO dtoItem = new ItemPedidoRequestDTO("Arroz", 2);

        List<String> nomeEsperado = List.of("Arroz");

        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO("Alfa Fornecedora", List.of(dtoItem), LocalDate.now(), LocalDate.of(2026,07,31));

        Fornecedor mockFornecedor = new Fornecedor();
        mockFornecedor.setNome("Alfa Fornecedora");

        Produto mockProduto = new Produto();
        mockProduto.setNome("Arroz");
        mockProduto.setPreco(new BigDecimal("25.5"));

        Pedido mockPedidoSalvo = new Pedido(pedidoRequestDTO);
        mockPedidoSalvo.setId(1L);
        mockPedidoSalvo.setFornecedor(mockFornecedor);

        ItemPedido itemPedido = new ItemPedido(mockPedidoSalvo, mockProduto, 2);
        itemPedido.setPrecoUnitario(mockProduto.getPreco());

        mockPedidoSalvo.setItens(List.of(itemPedido));
        mockPedidoSalvo.setTotalPedido(new BigDecimal("51.0"));

        LocalDate dataPesquisa = LocalDate.of(2026,12,31);

        BDDMockito.given(repositorioPedido.findByDataEntregaAfter(dataPesquisa))
                .willReturn(List.of(mockPedidoSalvo));

        //ACT

        List <PedidoResponseDTO> resultado = service.pedidosEntregueDepoisDeUmaData(dataPesquisa);

        //ASSERT

        Assertions.assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L,resultado.get(0).id());
        assertEquals("Alfa Fornecedora", resultado.get(0).fornecedor().nome());
        BDDMockito.verify(repositorioPedido, BDDMockito.times(1)).findByDataEntregaAfter(dataPesquisa);

    }

    @Test
    void deverEncontrarPedidoFeitoEntreDuasDatas(){

        //ARRANGE

        ItemPedidoRequestDTO dtoItem = new ItemPedidoRequestDTO("Arroz", 2);

        List<String> nomeEsperado = List.of("Arroz");

        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO("Alfa Fornecedora", List.of(dtoItem), LocalDate.now(), LocalDate.of(2026,07,31));

        Fornecedor mockFornecedor = new Fornecedor();
        mockFornecedor.setNome("Alfa Fornecedora");

        Produto mockProduto = new Produto();
        mockProduto.setNome("Arroz");
        mockProduto.setPreco(new BigDecimal("25.5"));

        Pedido mockPedidoSalvo = new Pedido(pedidoRequestDTO);
        mockPedidoSalvo.setId(1L);
        mockPedidoSalvo.setFornecedor(mockFornecedor);

        ItemPedido itemPedido = new ItemPedido(mockPedidoSalvo, mockProduto, 2);
        itemPedido.setPrecoUnitario(mockProduto.getPreco());

        mockPedidoSalvo.setItens(List.of(itemPedido));
        mockPedidoSalvo.setTotalPedido(new BigDecimal("51.0"));

        LocalDate dataPesquisaInicial = LocalDate.of(2026,07,01);

        LocalDate dataPesquisaFinal = LocalDate.of(2026,07,31);

        BDDMockito.given(repositorioPedido.pedidosFeitosEntreDuasDatas(dataPesquisaInicial, dataPesquisaFinal))
                .willReturn(List.of(mockPedidoSalvo));

        //ACT

        List <PedidoResponseDTO> resultado = service.pedidosFeitosEntreDuasDatas(dataPesquisaInicial, dataPesquisaFinal);

        //ASSERT

        Assertions.assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L,resultado.get(0).id());
        assertEquals("Alfa Fornecedora", resultado.get(0).fornecedor().nome());
        BDDMockito.verify(repositorioPedido, BDDMockito.times(1)).pedidosFeitosEntreDuasDatas(dataPesquisaInicial, dataPesquisaFinal);

    }

    @Test
    void deverEncontrarPedidoEntregueEntreDuasDatas(){

        //ARRANGE

        ItemPedidoRequestDTO dtoItem = new ItemPedidoRequestDTO("Arroz", 2);

        List<String> nomeEsperado = List.of("Arroz");

        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO("Alfa Fornecedora", List.of(dtoItem), LocalDate.now(), LocalDate.of(2026,07,31));

        Fornecedor mockFornecedor = new Fornecedor();
        mockFornecedor.setNome("Alfa Fornecedora");

        Produto mockProduto = new Produto();
        mockProduto.setNome("Arroz");
        mockProduto.setPreco(new BigDecimal("25.5"));

        Pedido mockPedidoSalvo = new Pedido(pedidoRequestDTO);
        mockPedidoSalvo.setId(1L);
        mockPedidoSalvo.setFornecedor(mockFornecedor);

        ItemPedido itemPedido = new ItemPedido(mockPedidoSalvo, mockProduto, 2);
        itemPedido.setPrecoUnitario(mockProduto.getPreco());

        mockPedidoSalvo.setItens(List.of(itemPedido));
        mockPedidoSalvo.setTotalPedido(new BigDecimal("51.0"));

        LocalDate dataPesquisaInicial = LocalDate.of(2026,07,01);

        LocalDate dataPesquisaFinal = LocalDate.of(2026,07,31);

        BDDMockito.given(repositorioPedido.pedidosEntreguesEntreDuasDatas(dataPesquisaInicial, dataPesquisaFinal))
                .willReturn(List.of(mockPedidoSalvo));

        //ACT

        List <PedidoResponseDTO> resultado = service.pedidosEntreguesEntreDuasDatas(dataPesquisaInicial, dataPesquisaFinal);

        //ASSERT

        Assertions.assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L,resultado.get(0).id());
        assertEquals("Alfa Fornecedora", resultado.get(0).fornecedor().nome());
        BDDMockito.verify(repositorioPedido, BDDMockito.times(1)).pedidosEntreguesEntreDuasDatas(dataPesquisaInicial, dataPesquisaFinal);

    }

    @Test
    void deveReceberPedido(){

        //ARRANGE

        ItemPedidoRequestDTO dtoItem = new ItemPedidoRequestDTO("Arroz", 2);

        List<String> nomeEsperado = List.of("Arroz");

        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO("Alfa Fornecedora", List.of(dtoItem), LocalDate.now(), null);

        Fornecedor mockFornecedor = new Fornecedor();
        mockFornecedor.setNome("Alfa Fornecedora");

        Produto mockProduto = new Produto();
        mockProduto.setNome("Arroz");
        mockProduto.setPreco(new BigDecimal("25.5"));

        Pedido mockPedidoSalvo = new Pedido(pedidoRequestDTO);
        mockPedidoSalvo.setId(1L);
        mockPedidoSalvo.setFornecedor(mockFornecedor);
        mockPedidoSalvo.setStatusPedido(Status.CRIADO);

        ItemPedido itemPedido = new ItemPedido(mockPedidoSalvo, mockProduto, 2);
        itemPedido.setPrecoUnitario(mockProduto.getPreco());


        mockPedidoSalvo.setItens(List.of(itemPedido));
        mockPedidoSalvo.setTotalPedido(new BigDecimal("51.0"));


        LocalDate dataEntrega = LocalDate.now();

        BDDMockito.given(repositorioPedido.findById(1L))
                .willReturn(Optional.of(mockPedidoSalvo));

        BDDMockito.given(repositorioPedido.save(ArgumentMatchers.any(Pedido.class)))
                .willReturn(mockPedidoSalvo);

        //ACT

        PedidoResponseDTO resultado = service.receberPedido(1L, dataEntrega);

        //ASSERT
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Alfa Fornecedora", resultado.fornecedor().nome());
        Assertions.assertEquals(new BigDecimal("51.0"), resultado.totalPedido());
        Assertions.assertEquals(LocalDate.now(), resultado.dataPedido());
        Assertions.assertEquals(1, resultado.itens().size());

        BDDMockito.verify(repositorioPedido, BDDMockito.times(1)).findById(1L);
        BDDMockito.verify(repositorioPedido, BDDMockito.times(1)).save(ArgumentMatchers.any(Pedido.class));
    }

    @Test
    void deveRetornarPedidoNaoEncontrado(){

        //ARRANGE

        Long idInexistente = 1L;
        LocalDate dataEntrega = LocalDate.now();

        BDDMockito.given(repositorioPedido.findById(1L))
                .willReturn(Optional.empty());

        //ACT

        ResourceNotFoundException excecao = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> service.receberPedido(idInexistente, dataEntrega)
        );

        //ASSERT

        assertEquals("Pedido não encontrado", excecao.getMessage());
        BDDMockito.verify(repositorioPedido, BDDMockito.never()).save(ArgumentMatchers.any(Pedido.class));
    }

    @Test
    @DisplayName("Testa exceção de pedido entregue com a data de entrega posterior a data atual")
    void deveRetornarExcecaoEntregaPosteriorDataAtual(){

        //ARRANGE

        ItemPedidoRequestDTO dtoItem = new ItemPedidoRequestDTO("Arroz", 2);

        List<String> nomeEsperado = List.of("Arroz");

        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO("Alfa Fornecedora", List.of(dtoItem), LocalDate.now(), null);

        Fornecedor mockFornecedor = new Fornecedor();
        mockFornecedor.setNome("Alfa Fornecedora");

        Produto mockProduto = new Produto();
        mockProduto.setNome("Arroz");
        mockProduto.setPreco(new BigDecimal("25.5"));

        Pedido mockPedidoSalvo = new Pedido(pedidoRequestDTO);
        mockPedidoSalvo.setId(1L);
        mockPedidoSalvo.setFornecedor(mockFornecedor);
        mockPedidoSalvo.setStatusPedido(Status.CRIADO);

        ItemPedido itemPedido = new ItemPedido(mockPedidoSalvo, mockProduto, 2);
        itemPedido.setPrecoUnitario(mockProduto.getPreco());


        mockPedidoSalvo.setItens(List.of(itemPedido));
        mockPedidoSalvo.setTotalPedido(new BigDecimal("51.0"));


        LocalDate dataEntrega = LocalDate.now().plusDays(1);

        BDDMockito.given(repositorioPedido.findById(1L))
                .willReturn(Optional.of(mockPedidoSalvo));


        //ACT

        BusinessRuleException excecao = Assertions.assertThrows(
                BusinessRuleException.class,
                () -> service.receberPedido(1L, dataEntrega)
        );

        //ASSERT
        Assertions.assertEquals("Data de entrega não pode ser posterior a data atual!", excecao.getMessage());
        assertThrows(BusinessRuleException.class, () -> service.receberPedido(1L, dataEntrega));
        BDDMockito.verify(repositorioPedido, BDDMockito.never()).save(ArgumentMatchers.any(Pedido.class));
    }

    @Test
    void deveRetornarExcecaoPedidoEntregue(){

        //ARRANGE

        ItemPedidoRequestDTO dtoItem = new ItemPedidoRequestDTO("Arroz", 2);

        List<String> nomeEsperado = List.of("Arroz");

        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO("Alfa Fornecedora", List.of(dtoItem), LocalDate.now(), LocalDate.now());

        Fornecedor mockFornecedor = new Fornecedor();
        mockFornecedor.setNome("Alfa Fornecedora");

        Produto mockProduto = new Produto();
        mockProduto.setNome("Arroz");
        mockProduto.setPreco(new BigDecimal("25.5"));

        Pedido mockPedidoSalvo = new Pedido(pedidoRequestDTO);
        mockPedidoSalvo.setId(1L);
        mockPedidoSalvo.setFornecedor(mockFornecedor);
        mockPedidoSalvo.setStatusPedido(Status.ENTREGUE);

        ItemPedido itemPedido = new ItemPedido(mockPedidoSalvo, mockProduto, 2);
        itemPedido.setPrecoUnitario(mockProduto.getPreco());


        mockPedidoSalvo.setItens(List.of(itemPedido));
        mockPedidoSalvo.setTotalPedido(new BigDecimal("51.0"));


        LocalDate dataEntrega = LocalDate.now();

        BDDMockito.given(repositorioPedido.findById(1L))
                .willReturn(Optional.of(mockPedidoSalvo));


        //ACT

        BusinessRuleException excecao = Assertions.assertThrows(
                BusinessRuleException.class,
                () -> service.receberPedido(1L, dataEntrega)
        );

        //ASSERT
        Assertions.assertEquals("Este pedido já foi recebido.", excecao.getMessage());
        assertThrows(BusinessRuleException.class, () -> service.receberPedido(1L, dataEntrega));
        BDDMockito.verify(repositorioPedido, BDDMockito.never()).save(ArgumentMatchers.any(Pedido.class));
    }

    @Test
    void deveDeletarPedido(){

        //ARRANGE
        Long idPedido = 1L;
        Pedido mockPedido = new Pedido();
        mockPedido.setId(idPedido);
        mockPedido.setTotalPedido(new BigDecimal("100.00"));

        BDDMockito.given(repositorioPedido.findById(idPedido)).willReturn(Optional.of(mockPedido));

        //ACT
        Pedido resultado = service.deletarPedido(idPedido);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(idPedido, resultado.getId());
        BDDMockito.verify(repositorioPedido, BDDMockito.times(1)).delete(mockPedido);

    }

    @Test
    void deveRetornarIDNaoEncontrado(){

        //ARRANGE
        Long idPedido = 1L;
        Pedido mockPedido = new Pedido();
        mockPedido.setId(idPedido);
        mockPedido.setTotalPedido(new BigDecimal("100.00"));

        BDDMockito.given(repositorioPedido.findById(idPedido)).willReturn(Optional.empty());

        //ACT
        ResourceNotFoundException excecao = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> service.deletarPedido(idPedido));

                Assertions.assertEquals("Pedido não encontrado", excecao.getMessage());
        BDDMockito.verify(repositorioPedido, BDDMockito.never()).delete(ArgumentMatchers.any(Pedido.class));
    }

    @Test
    @DisplayName("Deve gerar o array de bytes do PDF com sucesso quando o pedido existir")
    void deveGerarPdfComSucesso() {
        // ARRANGE
        Long idPedido = 1L;
        Pedido mockPedido = new Pedido();

        Fornecedor mockFornecedor = new Fornecedor();
        mockFornecedor.setId(10L);
        mockFornecedor.setNome("Fornecedor Teste");

        mockPedido.setId(idPedido);
        mockPedido.setItens(List.of());
        mockPedido.setFornecedor(mockFornecedor);

        mockPedido.setTotalPedido(BigDecimal.ZERO);
        mockPedido.setDataPedido(LocalDate.now());



        byte[] pdfEsperado = "PDF_MOCK_CONTEUDO".getBytes();

        BDDMockito.given(repositorioPedido.findById(idPedido))
                .willReturn(Optional.of(mockPedido));

        BDDMockito.given(pdfService.gerarPedidoPdf(ArgumentMatchers.any(PedidoResponseDTO.class)))
                .willReturn(pdfEsperado);

        // ACT
        byte[] resultado = service.gerarPdf(idPedido);

        // ASSERT
        Assertions.assertNotNull(resultado);
        Assertions.assertArrayEquals(pdfEsperado, resultado);

        BDDMockito.verify(repositorioPedido, BDDMockito.times(1)).findById(idPedido);
        BDDMockito.verify(pdfService, BDDMockito.times(1)).gerarPedidoPdf(ArgumentMatchers.any(PedidoResponseDTO.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar gerar PDF de um pedido inexistente")
    void deveLancarExcecaoAoGerarPdfDePedidoInexistente() {
        // ARRANGE
        Long idInexistente = 99L;

        BDDMockito.given(repositorioPedido.findById(idInexistente))
                .willReturn(Optional.empty());

        // ACT & ASSERT
        ResourceNotFoundException excecao = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> service.gerarPdf(idInexistente)
        );

        Assertions.assertEquals("Pedido não encontrado", excecao.getMessage());
        BDDMockito.verify(pdfService, BDDMockito.never()).gerarPedidoPdf(ArgumentMatchers.any());
    }

    @Test
    void deveEncontrarPedidoPorID(){

       //ARRANGE
        Long idPedido = 1L;
        Pedido mockPedido = new Pedido();
        mockPedido.setId(idPedido);

        BDDMockito.given(repositorioPedido.findById(1L)).willReturn(Optional.of(mockPedido));

        //ACT

        Pedido resultado = service.buscarPedidoPorId(1L);

        //ASSERT
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(idPedido, resultado.getId());
        BDDMockito.verify(repositorioPedido, BDDMockito.times(1)).findById(idPedido);


    }

    @Test
    void deveRetornarExcecaoPedidoNaoEncontrado(){

        //ARRANGE
        Long idPedido = 1L;


        BDDMockito.given(repositorioPedido.findById(1L)).willReturn(Optional.empty());

        //ACT
        ResourceNotFoundException excecao = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> service.buscarPedidoPorId(idPedido));

        //ASSERT
        Assertions.assertEquals("Pedido não encontrado", excecao.getMessage());

    }

}