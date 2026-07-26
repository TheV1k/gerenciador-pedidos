package br.com.alura.exercicios.gerenciador_pedidos.controller;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.BusinessRuleException;
import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.ResourceNotFoundException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor.FornecedorResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.ItemPedidoRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.ItemPedidoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.PedidoRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.PedidoResponseDTO;

import br.com.alura.exercicios.gerenciador_pedidos.models.Pedido;
import br.com.alura.exercicios.gerenciador_pedidos.models.Status;
import br.com.alura.exercicios.gerenciador_pedidos.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
@AutoConfigureJsonTesters
class PedidoControllerTest{

@Autowired
private MockMvc mockMvc;

@Autowired
private ObjectMapper objectMapper;


@MockitoBean
private PedidoService service;

@Autowired
private JacksonTester<PedidoRequestDTO> jsonDto;

    @Test
    void deveRetornar200ECadastrarPedido() throws Exception {

        // 1. ARRANGE
        ItemPedidoRequestDTO item1 = new ItemPedidoRequestDTO("Arroz", 2);
        ItemPedidoRequestDTO item2 = new ItemPedidoRequestDTO("Feijão", 3);

        PedidoRequestDTO requestDto = new PedidoRequestDTO(
                "Alfa Alimentos",
                List.of(item1, item2),
                LocalDate.now(),
                null
        );

        FornecedorResponseDTO fornecedorDTO = new FornecedorResponseDTO(
                1L,
                "Alfa Alimentos",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br"
        );

        List<ItemPedidoResponseDTO> itensResponse = List.of(
                new ItemPedidoResponseDTO("Arroz", 2, new BigDecimal("10.00"), new BigDecimal("20.00")),
                new ItemPedidoResponseDTO("Feijão", 3, new BigDecimal("15.00"), new BigDecimal("45.00"))
        );

        PedidoResponseDTO responseDTO = new PedidoResponseDTO(
                1L,
                LocalDate.now(),
                null,
                fornecedorDTO,
                Status.CRIADO,
                new BigDecimal("65.00"),
                itensResponse
        );

        // Define o comportamento do mock da Service
        when(service.cadastrarPedido(any(PedidoRequestDTO.class))).thenReturn(responseDTO);

        // 2. ACT & 3. ASSERT
        mockMvc.perform(post("/pedido")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))) // <-- O post() FECHA AQUI
                .andExpect(status().isOk()) // <-- andExpect encadeado fora do perform
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fornecedor.nome").value("Alfa Alimentos"))
                .andExpect(jsonPath("$.totalPedido").value(65.00))
                .andExpect(jsonPath("$.status").value("CRIADO"))
                .andExpect(jsonPath("$.itens.length()").value(2));

        verify(service, times(1)).cadastrarPedido(any(PedidoRequestDTO.class));
}

    void deveRetornar404QuandoProdutoNaoEncontrado() throws Exception {
        // 1. ARRANGE
        ItemPedidoRequestDTO itemInvalido = new ItemPedidoRequestDTO("Produto Inexistente", 1);
        PedidoRequestDTO requestDto = new PedidoRequestDTO(
                "Alfa Alimentos",
                List.of(itemInvalido),
                LocalDate.now(),
                null
        );

        // Simula a Service lançando ResourceNotFoundException
        when(service.cadastrarPedido(any(PedidoRequestDTO.class)))
                .thenThrow(new ResourceNotFoundException("Produto não encontrado: Produto Inexistente"));

        // 2. ACT & 3. ASSERT
        mockMvc.perform(post("/pedido")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound()); // Aguarda HTTP 404 Not Found

        verify(service, times(1)).cadastrarPedido(any(PedidoRequestDTO.class));
    }

    @Test
    @DisplayName("Deve retornar Status 404 quando o fornecedor informado não for encontrado")
    void deveRetornar404QuandoFornecedorNaoEncontrado() throws Exception {
        // 1. ARRANGE
        ItemPedidoRequestDTO item = new ItemPedidoRequestDTO("Arroz", 2);
        PedidoRequestDTO requestDto = new PedidoRequestDTO(
                "Fornecedor Fantasma",
                List.of(item),
                LocalDate.now(),
                null
        );

        when(service.cadastrarPedido(any(PedidoRequestDTO.class)))
                .thenThrow(new ResourceNotFoundException("Fornecedor não encontrado"));

        // 2. ACT & 3. ASSERT
        mockMvc.perform(post("/pedido")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());

        verify(service, times(1)).cadastrarPedido(any(PedidoRequestDTO.class));
    }


    @Test
    @DisplayName("Deve retornar Status 400 quando o payload JSON for enviado malformado ou nulo")
    void deveRetornar400QuandoBodyEstiverVazioOuInvalido() throws Exception {


        // 1. ACT & 2. ASSERT
        mockMvc.perform(post("/pedido")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // Corpo inválido
                .andExpect(status().isBadRequest());

        verify(service, never()).cadastrarPedido(any(PedidoRequestDTO.class));
    }

    @Test
    void deveRetornar200EEncontrarPedidoPeloID() throws Exception {

        //ARRANGE

        Long id = 1L;

        FornecedorResponseDTO fornecedorDTO = new FornecedorResponseDTO(
                1L,
                "Alfa Alimentos",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br"
        );

        List<ItemPedidoResponseDTO> itensResponse = List.of(
                new ItemPedidoResponseDTO("Arroz", 2, new BigDecimal("10.00"), new BigDecimal("20.00")),
                new ItemPedidoResponseDTO("Feijão", 3, new BigDecimal("15.00"), new BigDecimal("45.00"))
        );

        PedidoResponseDTO responseDTO = new PedidoResponseDTO(
                1L,
                LocalDate.now(),
                null,
                fornecedorDTO,
                Status.CRIADO,
                new BigDecimal("100.0"),
                itensResponse
        );

        //ACT & ASSERT

        when(service.buscarPedidoPorId(id)).thenReturn(responseDTO);

        mockMvc.perform(get("/pedido/{id}", id)).andExpect(status().isOk());
    }

    @Test
    void deveRetornar404ParaIdInvalido() throws Exception {

        //ARRANGE

        Long id = 99L;

        //ACT & ASSERT

        when(service.buscarPedidoPorId(id))
                .thenThrow(new ResourceNotFoundException("Id Inválido"));

        mockMvc.perform(get("/pedido/{id}", id))
                .andExpect(status()
                        .isNotFound());
    }

    @Test
    void deveRetornar200EEncontrarPedidosNaoEntregues() throws Exception {

        //ARRANGE

        FornecedorResponseDTO fornecedorDTO = new FornecedorResponseDTO(
                1L,
                "Alfa Alimentos",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br"
        );

        List<ItemPedidoResponseDTO> itensResponse = List.of(
                new ItemPedidoResponseDTO("Arroz", 2, new BigDecimal("10.00"), new BigDecimal("20.00")),
                new ItemPedidoResponseDTO("Feijão", 3, new BigDecimal("15.00"), new BigDecimal("45.00"))
        );

        PedidoResponseDTO responseDTO = new PedidoResponseDTO(
                1L,
                LocalDate.now(),
                null,
                fornecedorDTO,
                Status.CRIADO,
                new BigDecimal("100.0"),
                itensResponse
        );

        //ACT & ASSERT

        when(service.buscarPedidosNaoEntregue())
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/pedido/pedidos-nao-entregues")).andExpect(status().isOk());
    }

    @Test
    void deveRetornar200EEncontrarPedidosEntregues() throws Exception {

        //ARRANGE

        FornecedorResponseDTO fornecedorDTO = new FornecedorResponseDTO(
                1L,
                "Alfa Alimentos",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br"
        );

        List<ItemPedidoResponseDTO> itensResponse = List.of(
                new ItemPedidoResponseDTO("Arroz", 2, new BigDecimal("10.00"), new BigDecimal("20.00")),
                new ItemPedidoResponseDTO("Feijão", 3, new BigDecimal("15.00"), new BigDecimal("45.00"))
        );

        PedidoResponseDTO responseDTO = new PedidoResponseDTO(
                1L,
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                fornecedorDTO,
                Status.CRIADO,
                new BigDecimal("100.0"),
                itensResponse
        );

        //ACT & ASSERT

        when(service.buscarPedidosEntregue())
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/pedido/pedidos-entregues")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("CRIADO"))
                .andExpect(jsonPath("$[0].totalPedido").value(100.0))
                .andExpect(jsonPath("$[0].fornecedor.nome").value("Alfa Alimentos"))
                .andExpect(jsonPath("$[0].itens.length()").value(2));;
    }

    @Test
    void deveRetornar200EEncontrarPedidosRealizadosAnterioresAUmaData() throws Exception {

        //ARRANGE

        LocalDate data = LocalDate.now().plusDays(1);

        FornecedorResponseDTO fornecedorDTO = new FornecedorResponseDTO(
                1L,
                "Alfa Alimentos",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br"
        );

        List<ItemPedidoResponseDTO> itensResponse = List.of(
                new ItemPedidoResponseDTO("Arroz", 2, new BigDecimal("10.00"), new BigDecimal("20.00")),
                new ItemPedidoResponseDTO("Feijão", 3, new BigDecimal("15.00"), new BigDecimal("45.00"))
        );

        PedidoResponseDTO responseDTO = new PedidoResponseDTO(
                1L,
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                fornecedorDTO,
                Status.CRIADO,
                new BigDecimal("100.0"),
                itensResponse
        );


        when(service.pedidosFeitosAntesDeUmaData(data))
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/pedido/pedido-anterior").contentType(MediaType.APPLICATION_JSON)
                .param("data", String.valueOf(data))).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("CRIADO"))
                .andExpect(jsonPath("$[0].totalPedido").value(100.0))
                .andExpect(jsonPath("$[0].fornecedor.nome").value("Alfa Alimentos"))
                .andExpect(jsonPath("$[0].itens.length()").value(2));

    }

    @Test
    void deveRetornar200EListaVaziaQuandoNaoHouverPedidosAnteriores() throws Exception {
        LocalDate data = LocalDate.now();

        when(service.pedidosFeitosAntesDeUmaData(data))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/pedido/pedido-anterior")
                        .param("data", data.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void deveRetornar400QuandoParametroDataNaoForInformado() throws Exception {
        mockMvc.perform(get("/pedido/pedido-anterior")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400QuandoFormatoDeDataForInvalido() throws Exception {
        mockMvc.perform(get("/pedido/pedido-anterior")
                        .param("data", "2026-15-40")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400QuandoDataForInvalidaSegundoRegraDeNegocio() throws Exception {
        LocalDate dataFutura = LocalDate.now().plusYears(10);

        when(service.pedidosFeitosAntesDeUmaData(dataFutura))
                .thenThrow(new BusinessRuleException("Data informada é inválida"));

        mockMvc.perform(get("/pedido/pedido-anterior")
                        .param("data", dataFutura.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deveRetornar200EEncontrarPedidosEntreguesAntesDeUmaData() throws Exception {

        //ARRANGE

        LocalDate data = LocalDate.now().plusDays(3);

        FornecedorResponseDTO fornecedorDTO = new FornecedorResponseDTO(
                1L,
                "Alfa Alimentos",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br"
        );

        List<ItemPedidoResponseDTO> itensResponse = List.of(
                new ItemPedidoResponseDTO("Arroz", 2, new BigDecimal("10.00"), new BigDecimal("20.00")),
                new ItemPedidoResponseDTO("Feijão", 3, new BigDecimal("15.00"), new BigDecimal("45.00"))
        );

        PedidoResponseDTO responseDTO = new PedidoResponseDTO(
                1L,
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                fornecedorDTO,
                Status.CRIADO,
                new BigDecimal("100.0"),
                itensResponse
        );


        when(service.pedidosEntreguesAntesDeUmaData(data))
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/pedido/pedido-entregue-antes").contentType(MediaType.APPLICATION_JSON)
                .param("data", String.valueOf(data))).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("CRIADO"))
                .andExpect(jsonPath("$[0].totalPedido").value(100.0))
                .andExpect(jsonPath("$[0].fornecedor.nome").value("Alfa Alimentos"))
                .andExpect(jsonPath("$[0].itens.length()").value(2));

    }

    @Test
    void deveRetornar200EListaVaziaQuandoNaoHouverPedidosEntreguesAnteriores() throws Exception {

        // ARRANGE
        LocalDate dataFiltro = LocalDate.now();

        when(service.pedidosEntreguesAntesDeUmaData(dataFiltro))
                .thenReturn(Collections.emptyList());

        // ACT & ASSERT
        mockMvc.perform(get("/pedido/pedido-entregue-antes")
                        .param("data", dataFiltro.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void deveRetornar400QuandoParametroDataForOmitidoParaPedidosEntregues() throws Exception {

        // ACT & ASSERT
        mockMvc.perform(get("/pedido/pedido-entregue-antes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400QuandoDataForFormatoInvalidoParaPedidosEntregues() throws Exception {

        // ACT & ASSERT
        mockMvc.perform(get("/pedido/pedido-entregue-antes")
                        .param("data", "data-invalida")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar200EEncontrarPedidosRealizadosDepoisDeUmaData() throws Exception {

        //ARRANGE

        LocalDate data = LocalDate.now().minusDays(1);

        FornecedorResponseDTO fornecedorDTO = new FornecedorResponseDTO(
                1L,
                "Alfa Alimentos",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br"
        );

        List<ItemPedidoResponseDTO> itensResponse = List.of(
                new ItemPedidoResponseDTO("Arroz", 2, new BigDecimal("10.00"), new BigDecimal("20.00")),
                new ItemPedidoResponseDTO("Feijão", 3, new BigDecimal("15.00"), new BigDecimal("45.00"))
        );

        PedidoResponseDTO responseDTO = new PedidoResponseDTO(
                1L,
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                fornecedorDTO,
                Status.CRIADO,
                new BigDecimal("100.0"),
                itensResponse
        );


        when(service.pedidosFeitosDepoisDeUmaData(data))
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/pedido/pedido-posterior").contentType(MediaType.APPLICATION_JSON)
                .param("data", String.valueOf(data))).andExpect(status().isOk()).andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("CRIADO"))
                .andExpect(jsonPath("$[0].totalPedido").value(100.0))
                .andExpect(jsonPath("$[0].fornecedor.nome").value("Alfa Alimentos"))
                .andExpect(jsonPath("$[0].itens.length()").value(2));
    }


    @Test
    void deveRetornar200EListaVaziaQuandoNaoHouverPedidosPosteriores() throws Exception {

        // ARRANGE
        LocalDate dataFutura = LocalDate.now().plusYears(1);

        when(service.pedidosFeitosDepoisDeUmaData(dataFutura))
                .thenReturn(Collections.emptyList());

        // ACT & ASSERT
        mockMvc.perform(get("/pedido/pedido-posterior")
                        .param("data", String.valueOf(dataFutura))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void deveRetornar400QuandoParametroDataForOmitidoParaPedidosPosteriores() throws Exception {

        // ACT & ASSERT (sem chamar o service no ARRANGE, pois a requisição deve falhar na Controller)
        mockMvc.perform(get("/pedido/pedido-posterior")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400QuandoDataForFormatoInvalidoParaPedidosPosteriores() throws Exception {

        // ACT & ASSERT
        mockMvc.perform(get("/pedido/pedido-posterior")
                        .param("data", "2026-15-40") // Data com mês e dia inválidos
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void deveRetornar200EEncontrarPedidosEntreguesDepoisDeUmaData() throws Exception {

        //ARRANGE

        LocalDate data = LocalDate.now().minusDays(3);

        FornecedorResponseDTO fornecedorDTO = new FornecedorResponseDTO(
                1L,
                "Alfa Alimentos",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br"
        );

        List<ItemPedidoResponseDTO> itensResponse = List.of(
                new ItemPedidoResponseDTO("Arroz", 2, new BigDecimal("10.00"), new BigDecimal("20.00")),
                new ItemPedidoResponseDTO("Feijão", 3, new BigDecimal("15.00"), new BigDecimal("45.00"))
        );

        PedidoResponseDTO responseDTO = new PedidoResponseDTO(
                1L,
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                fornecedorDTO,
                Status.CRIADO,
                new BigDecimal("100.0"),
                itensResponse
        );


        when(service.pedidosEntregueDepoisDeUmaData(data))
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/pedido/pedido-entregue-depois").contentType(MediaType.APPLICATION_JSON)
                        .param("data", String.valueOf(data))).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("CRIADO"))
                .andExpect(jsonPath("$[0].totalPedido").value(100.0))
                .andExpect(jsonPath("$[0].fornecedor.nome").value("Alfa Alimentos"))
                .andExpect(jsonPath("$[0].itens.length()").value(2));

    }

    @Test
    void deveRetornar200EListaVaziaQuandoNaoHouverPedidosEntreguesDepoisDaData() throws Exception {

        // ARRANGE
        LocalDate dataFutura = LocalDate.now().plusYears(1);

        when(service.pedidosEntregueDepoisDeUmaData(dataFutura))
                .thenReturn(Collections.emptyList());

        // ACT & ASSERT
        mockMvc.perform(get("/pedido/pedido-entregue-depois")
                        .param("data", String.valueOf(dataFutura))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void deveRetornar400QuandoParametroDataForOmitidoParaPedidosEntreguesDepois() throws Exception {

        // ACT & ASSERT
        mockMvc.perform(get("/pedido/pedido-entregue-depois")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400QuandoDataForFormatoInvalidoParaPedidosEntreguesDepois() throws Exception {

        // ACT & ASSERT
        mockMvc.perform(get("/pedido/pedido-entregue-depois")
                        .param("data", "data-invalida")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void deveRetornar200EEncontrarPedidosRealizadosEntreDuasDatas() throws Exception {

        //ARRANGE

        LocalDate dataInicial = LocalDate.now().minusDays(3);
        LocalDate dataFinal = LocalDate.now().plusDays(3);

        FornecedorResponseDTO fornecedorDTO = new FornecedorResponseDTO(
                1L,
                "Alfa Alimentos",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br"
        );

        List<ItemPedidoResponseDTO> itensResponse = List.of(
                new ItemPedidoResponseDTO("Arroz", 2, new BigDecimal("10.00"), new BigDecimal("20.00")),
                new ItemPedidoResponseDTO("Feijão", 3, new BigDecimal("15.00"), new BigDecimal("45.00"))
        );

        PedidoResponseDTO responseDTO = new PedidoResponseDTO(
                1L,
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                fornecedorDTO,
                Status.CRIADO,
                new BigDecimal("100.0"),
                itensResponse
        );


        when(service.pedidosFeitosEntreDuasDatas(dataInicial,dataFinal))
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/pedido/pedidos-feitos-entre").contentType(MediaType.APPLICATION_JSON)
                        .param("dataInicial", String.valueOf(dataInicial))
                        .param( "dataFinal", String.valueOf(dataFinal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("CRIADO"))
                .andExpect(jsonPath("$[0].totalPedido").value(100.0))
                .andExpect(jsonPath("$[0].fornecedor.nome").value("Alfa Alimentos"))
                .andExpect(jsonPath("$[0].itens.length()").value(2));

    }

    @Test
    void deveRetornar400QuandoDataInicialForPosteriorADataFinal() throws Exception {

        // ARRANGE
        LocalDate dataInicial = LocalDate.now().plusDays(5); // Data mais recente
        LocalDate dataFinal = LocalDate.now();              // Data mais antiga

        // Simula a exceção de validação lançada pela regra de negócio
        when(service.pedidosFeitosEntreDuasDatas(dataInicial, dataFinal))
                .thenThrow(new IllegalArgumentException("A data inicial não pode ser posterior à data final."));

        // ACT & ASSERT
        mockMvc.perform(get("/pedido/pedidos-feitos-entre")
                        .param("dataInicial", String.valueOf(dataInicial))
                        .param("dataFinal", String.valueOf(dataFinal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }



    @Test
    void deveRetornar200EEncontrarPedidosEntreguesEntreDuasDatas() throws Exception {

        FornecedorResponseDTO fornecedorDTO = new FornecedorResponseDTO(
                1L,
                "Alfa Alimentos",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br"
        );

        List<ItemPedidoResponseDTO> itensResponse = List.of(
                new ItemPedidoResponseDTO("Arroz", 2, new BigDecimal("10.00"), new BigDecimal("20.00")),
                new ItemPedidoResponseDTO("Feijão", 3, new BigDecimal("15.00"), new BigDecimal("45.00"))
        );

        PedidoResponseDTO responseDTO = new PedidoResponseDTO(
                1L,
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                fornecedorDTO,
                Status.ENTREGUE,
                new BigDecimal("100.0"),
                itensResponse
        );
        List<PedidoResponseDTO> listaSimulada = List.of(responseDTO);

        when(service.pedidosEntreguesEntreDuasDatas(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(listaSimulada);

        // 3. ACT & ASSERT
        mockMvc.perform(get("/pedido/pedidos-entregues-entre")
                        .param("dataInicial", "2026-07-23")
                        .param("dataFinal", "2026-07-29")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void deveRetornar200EConteudoPdfAoGerarPdfDoPedido() throws Exception {
        // ARRANGE
        Long pedidoId = 1L;
        byte[] pdfMockado = "Conteudo do PDF em bytes de teste".getBytes(StandardCharsets.UTF_8);

        // Mock do serviço retornando os bytes simulados do PDF
        when(service.gerarPdf(pedidoId)).thenReturn(pdfMockado);

        // ACT & ASSERT
        mockMvc.perform(get("/pedido/{id}/pdf", pedidoId))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pedido-1.pdf"))
                .andExpect(content().bytes(pdfMockado)); // Valida se os bytes do body sao os mesmos do mock
    }

    @Test
    void deveRetornar404QuandoPedidoNaoExisteParaGerarPdf() throws Exception {
        // ARRANGE
        Long idInexistente = 999L;

        when(service.gerarPdf(idInexistente))
                .thenThrow(new ResourceNotFoundException("Pedido não encontrado."));

        // ACT & ASSERT
        mockMvc.perform(get("/pedido/{id}/pdf", idInexistente))
                .andExpect(status().isNotFound());
    }


    @Test
    void deveRetornar200EConfirmarAEntregaDoPedido() throws Exception {
        // 1. ARRANGE
        Long id = 1L;
        LocalDate dataEntrega = LocalDate.now();

        PedidoResponseDTO responseDTO = new PedidoResponseDTO(
                id,
                LocalDate.now().minusDays(1),
                dataEntrega,
                null,
                Status.ENTREGUE,
                new BigDecimal("100.00"),
                List.of()
        );

        // MOCK CORRIGIDO: eq(id) em vez de apenas id
        when(service.receberPedido(eq(id), any(LocalDate.class)))
                .thenReturn(responseDTO);

        // 2. ACT & ASSERT
        mockMvc.perform(put("/pedido/{id}", id)
                        .param("dataEntrega", dataEntrega.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.status").value("ENTREGUE"));
    }

    @Test
    void deveRetornar404QuandoPedidoNaoForEncontradoAoConfirmarEntrega() throws Exception {
        // 1. ARRANGE
        Long idInexistente = 99L;
        LocalDate dataEntrega = LocalDate.now();

        when(service.receberPedido(eq(idInexistente), any(LocalDate.class)))
                .thenThrow(new ResourceNotFoundException("Pedido não encontrado com o id: " + idInexistente));

        // 2. ACT & ASSERT
        mockMvc.perform(put("/pedido/{id}", idInexistente)
                        .param("dataEntrega", dataEntrega.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar400QuandoRegraDeNegocioForViolada() throws Exception {
        // 1. ARRANGE
        Long id = 1L;
        LocalDate dataEntregaInvalida = LocalDate.now().minusDays(10); // ex: data no passado

        when(service.receberPedido(eq(id), any(LocalDate.class)))
                .thenThrow(new IllegalArgumentException("A data de entrega não pode ser anterior à data da compra"));

        // 2. ACT & ASSERT
        mockMvc.perform(put("/pedido/{id}", id)
                        .param("dataEntrega", dataEntregaInvalida.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400QuandoDataEntregaNaoForInformada() throws Exception {
        // 1. ARRANGE
        Long id = 1L;

        // 2. ACT & ASSERT (O Spring lança MissingServletRequestParameterException antes de chamar o Service)
        mockMvc.perform(put("/pedido/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400QuandoFormatoDaDataForInvalido() throws Exception {
        // 1. ARRANGE
        Long id = 1L;
        String dataInvalida = "31-02-2026"; // Formato ou data inválida

        // 2. ACT & ASSERT
        mockMvc.perform(put("/pedido/{id}", id)
                        .param("dataEntrega", dataInvalida)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveExcluirPedidoComSucesso() throws Exception {

        // ARRANGE
        Long id = 1L;
        Pedido pedidoDeletado = new Pedido();
        pedidoDeletado.setId(id);

        // Como o método deletarPedido retorna um Pedido, passamos a instância no thenReturn:
        when(service.deletarPedido(id)).thenReturn(pedidoDeletado);

        // ACT & ASSERT
        mockMvc.perform(delete("/pedido/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // ASSERT
        verify(service).deletarPedido(id);
    }


    @Test
    void deveRetornarNotFoundQuandoCategoriaNaoExistir() throws Exception {

        //ARRANGE
        Long idInexistente = 99L;

        //ACT
        doThrow(new ResourceNotFoundException("Pedido não encontrada"))
                .when(service).deletarPedido(idInexistente);

        mockMvc.perform(delete("/pedido/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        //ASSERT
        verify(service).deletarPedido(idInexistente);
    }


}


