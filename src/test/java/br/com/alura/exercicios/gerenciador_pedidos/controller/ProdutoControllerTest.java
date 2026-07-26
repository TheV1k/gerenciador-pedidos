package br.com.alura.exercicios.gerenciador_pedidos.controller;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.BusinessRuleException;
import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.ResourceNotFoundException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResumoDTO;
import br.com.alura.exercicios.gerenciador_pedidos.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(ProdutoController.class)
@AutoConfigureJsonTesters
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService service;


    @Autowired
    private JacksonTester<ProdutoRequestDTO> jsonDto;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deveRetornar200ECadastrarProduto() throws Exception {

        //ARRANGE
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO(
                "Arroz",
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora");

        ProdutoResponseDTO responseDto = new ProdutoResponseDTO(
                1L,
                "Arroz",
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora");

        //ACT

        when(service.cadastrarProduto(requestDTO)).thenReturn(responseDto);

        MockHttpServletResponse response =  mockMvc.perform(
                post("/produto")
                        .content(jsonDto.write(requestDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT

        assertEquals(200,response.getStatus());


    }


    @Test
    void deveRetornar400ParaProdutoEmBranco() throws Exception {

        //ARRANGE
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO("",
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora");


        //ACT & ASSERT


        ResultActions response =  mockMvc.perform(
                post("/produto")
                        .content(jsonDto.write(requestDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

    }

    @Test
    void deveRetornar400ParaProdutoNulo() throws Exception {

        //ARRANGE
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO(null,
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora");


        //ACT & ASSERT


        ResultActions response =  mockMvc.perform(
                post("/produto")
                        .content(jsonDto.write(requestDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

    }

    @Test
    void deveRetornar400ParaPrecoNegativo() throws Exception {

        //ARRANGE
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO(null,
                new BigDecimal("-1.00"),
                "Grãos",
                "Alfa Fornecedora");


        //ACT & ASSERT


        ResultActions response =  mockMvc.perform(
                post("/produto")
                        .content(jsonDto.write(requestDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

    }


    @Test
    void deveRetornar400ParaCategoriaEmBranco() throws Exception {

        //ARRANGE
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO(null,
                new BigDecimal("25.00"),
                "",
                "Alfa Fornecedora");


        //ACT & ASSERT


        ResultActions response =  mockMvc.perform(
                post("/produto")
                        .content(jsonDto.write(requestDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

    }


    @Test
    void deveRetornar400ParaCategoriaNulo() throws Exception {

        //ARRANGE
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO(null,
                new BigDecimal("25.00"),
                null,
                "Alfa Fornecedora");


        //ACT & ASSERT


        ResultActions response =  mockMvc.perform(
                post("/produto")
                        .content(jsonDto.write(requestDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

    }

    @Test
    void deveRetornar400ParaFornecedorEmBranco() throws Exception {

        //ARRANGE
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO(null,
                new BigDecimal("25.00"),
                "Grãos",
                "");


        //ACT & ASSERT


        ResultActions response =  mockMvc.perform(
                post("/produto")
                        .content(jsonDto.write(requestDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

    }

    @Test
    void deveRetornar400ParaFornecedorNulo() throws Exception {

        //ARRANGE
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO(null,
                new BigDecimal("25.00"),
                "Grãos",
                null);


        //ACT & ASSERT


        ResultActions response =  mockMvc.perform(
                post("/produto")
                        .content(jsonDto.write(requestDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

    }

    @Test
    void deveRetornarStatus200EListaDeProdutosAoCadastrarEmLoteComSucesso() throws Exception {
        // ARRANGE
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO("Teclado", new BigDecimal("150.0"),"Eletrônicos", "TechCorp");
        ProdutoResponseDTO responseDTO = new ProdutoResponseDTO(1L, "Teclado", new BigDecimal("250.0"), "Eletrônicos", "TechCorp");

        List<ProdutoRequestDTO> dtoList = List.of(requestDTO);
        List<ProdutoResponseDTO> responseList = List.of(responseDTO);

        when(service.cadastrarEmLote(anyList())).thenReturn(responseList);

        // ACT & ASSERT
        mockMvc.perform(post("/produto/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("Teclado"))
                .andExpect(jsonPath("$[0].nomeCategoria").value("Eletrônicos"))
                .andExpect(jsonPath("$[0].nomeFornecedor").value("TechCorp"));
    }

    @Test
    void deveRetornarStatus200EListaVaziaQuandoReceberPayloadVazio() throws Exception {
        // ARRANGE
        when(service.cadastrarEmLote(Collections.emptyList())).thenReturn(Collections.emptyList());

        // ACT & ASSERT
        mockMvc.perform(post("/produto/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.emptyList())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(service, times(1)).cadastrarEmLote(Collections.emptyList());
    }

    @Test
    void deveRetornarStatus404QuandoCategoriaOuFornecedorNaoEncontrado() throws Exception {
        // ARRANGE
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO("Teclado", new BigDecimal("150.0"),"Inexistente", "TechCorp");

        when(service.cadastrarEmLote(anyList()))
                .thenThrow(new ResourceNotFoundException("Categoria não encontrada"));

        // ACT & ASSERT
        mockMvc.perform(post("/produto/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(requestDTO))))
                .andExpect(status().isNotFound());

        verify(service, times(1)).cadastrarEmLote(anyList());
    }

    @Test
    void deveRetornar200EEncontrarProdutoPorID() throws Exception {

        //ARRANGE

        Long id = 1L;
        ProdutoResponseDTO responseDto = new ProdutoResponseDTO(
                1L,
                "Arroz",
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora");

        //ACT

        when(service.buscarProdutoPorId(id)).thenReturn(responseDto);

        MockHttpServletResponse response = (MockHttpServletResponse) mockMvc.perform(get("/produto/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        //ASSERT

        assertEquals(200, response.getStatus());

    }

    @Test
    void deveRetornar422ParaIDNaoEncontrado() throws Exception {

        //ARRANGE

        Long id = 99L;

        //ACT

        when(service.buscarProdutoPorId(id)).thenThrow(new BusinessRuleException("Produto não encontrado"));

        MockHttpServletResponse response = mockMvc.perform(get("/produto/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        //ASSERT

        assertEquals(422, response.getStatus());

    }

    @Test
    void deveRetornar200EEncontrarCategoriaPorNome() throws Exception {

        //ARRANGE

        String produto = "arroz";
        ProdutoResponseDTO responseDto = new ProdutoResponseDTO(
                1L,
                "Arroz",
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora");

        //ACT

        when(service.buscarProduto(produto)).thenReturn(responseDto);

        ResultActions response = mockMvc.perform(get("/produto/nome-produto/{nome}", produto)
                        .contentType(MediaType.APPLICATION_JSON)
                .param("nome", "arroz")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());



    }

    @Test
    void deveRetornar404ParaPesquisaProdutoEmBranco() throws Exception {

        //ARRANGE

        String produto = "";

        //ACT & ASSERT
        ResultActions response = mockMvc.perform(get("/produto/nome-produto/{nome}", produto)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isNotFound());

    }

    @Test
    void deveRetornar200EEncontrarProdutoComMaiorValor() throws Exception {

        //ARRANGE

       BigDecimal valorPesquisado = new BigDecimal("23.5");

        ProdutoResumoDTO resumoDTO = new ProdutoResumoDTO(
                "Arroz",
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora"
        );



        //ACT

        when(service.buscarValorMaior(valorPesquisado)).thenReturn(List.of(resumoDTO));

        MockHttpServletResponse response = mockMvc.perform(get("/produto/buscar-valor-maior")
                .contentType(MediaType.APPLICATION_JSON)
                .param("valorPesquisado", String.valueOf(valorPesquisado))
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        //ASSERT
        assertEquals(200, response.getStatus());

    }


    @Test
    void deveRetornar422ParaBuscaValorMaiorNegativo() throws Exception {

        //ARRANGE

        BigDecimal valorPesquisado = new BigDecimal("-23.5");


        //ACT & ASSERT

        when(service.buscarValorMaior(valorPesquisado)).thenThrow(new BusinessRuleException("Valor deve ser maior do que zero"));

        ResultActions response = mockMvc.perform(get("/produto/buscar-valor-maior")
                .contentType(MediaType.APPLICATION_JSON)
                .param("valorPesquisado", String.valueOf(valorPesquisado))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());

    }


    @Test
    void deveRetornar200EEncontrarProdutoComMenorValor() throws Exception {

        //ARRANGE

        BigDecimal valorPesquisado = new BigDecimal("23.5");

        ProdutoResumoDTO resumoDTO = new ProdutoResumoDTO(
                "Feijão",
                new BigDecimal("15.5"),
                "Grãos",
                "Alfa Fornecedora"
        );



        //ACT

        when(service.buscarMenoresValores(valorPesquisado)).thenReturn(List.of(resumoDTO));

        MockHttpServletResponse response = mockMvc.perform(get("/produto/buscar-valor-menor")
                .contentType(MediaType.APPLICATION_JSON)
                .param("valorPesquisado", String.valueOf(valorPesquisado))
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        //ASSERT
        assertEquals(200, response.getStatus());

    }


    @Test
    void deveRetornar422ParaBuscaValorMenorNegativo() throws Exception {

        //ARRANGE

        BigDecimal valorPesquisado = new BigDecimal("-23.5");


        //ACT & ASSERT

        when(service.buscarMenoresValores(valorPesquisado)).thenThrow(new BusinessRuleException("Valor deve ser maior do que zero"));

        ResultActions response = mockMvc.perform(get("/produto/buscar-valor-menor")
                .contentType(MediaType.APPLICATION_JSON)
                .param("valorPesquisado", String.valueOf(valorPesquisado))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());

    }

    @Test
    void deveRetornar200ParaOsTresProdutosMaisCaros() throws Exception {

        //ARRANGE
        ProdutoResumoDTO dto1 = new ProdutoResumoDTO("Televisão",
                new BigDecimal("2000.00"),
                "Eletrônicos",
                "Delta Eletro");

        ProdutoResumoDTO dto2 = new ProdutoResumoDTO("Celular",
                new BigDecimal("3500.00"),
                "Eletrônicos",
                "Delta Eletro");

        ProdutoResumoDTO dto3 = new ProdutoResumoDTO("PC Gamer",
                new BigDecimal("5000.00"),
                "Eletrônicos",
                "Delta Eletro");

        List<ProdutoResumoDTO> tresMaisCaros = List.of(dto1, dto2, dto3);

        when(service.tresProdutosMaisCaros()).thenReturn(tresMaisCaros);

        ResultActions response = mockMvc
                .perform(get("/produto/tres-mais-caros")
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    void deveRetornar200ParaCincoMaisBaratos() throws Exception {


        String categoriaPesquisada = "Papelaria";

        ProdutoResumoDTO dto1 = new ProdutoResumoDTO("Caneta",
                new BigDecimal("3.5"),
                "Papelaria",
                "Papelarias Gama");

        ProdutoResumoDTO dto2 = new ProdutoResumoDTO("Lapiseira",
                new BigDecimal("2.5"),
                "Papelaria",
                "Papelarias Gama");

        ProdutoResumoDTO dto3 = new ProdutoResumoDTO("Caderno",
                new BigDecimal("10.0"),
                "Papelaria",
                "Papelarias Gama");

        ProdutoResumoDTO dto4 = new ProdutoResumoDTO("Borracha",
                new BigDecimal("0.5"),
                "Papelaria",
                "Papelarias Gama");

        ProdutoResumoDTO dto5 = new ProdutoResumoDTO("Apontador",
                new BigDecimal("4.5"),
                "Papelaria",
                "Distribuidoro Alfa");

        List<ProdutoResumoDTO> cincoMaisBaratosDeUmaCategoria = List.of(dto1, dto2, dto3, dto4, dto5);

        when(service.cincoProdutosMaisBaratosDeUmaCategoria(categoriaPesquisada))
                .thenReturn(cincoMaisBaratosDeUmaCategoria);

        ResultActions response = mockMvc.perform(
                        get("/produto/5-mais-baratos-categoria/{categoriaPesquisada}", categoriaPesquisada.toLowerCase())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar200EBuscarProdutoPorParteDoNome() throws Exception {

        String produtoPesquisado = "Arroz";

        ProdutoResumoDTO resumoDTO = new ProdutoResumoDTO("Arroz Integral",
                new BigDecimal("30.0"),
                "Grãos",
                "Alfa Fornecedora");

        ProdutoResumoDTO resumoDTO2 = new ProdutoResumoDTO("Arroz Negro",
                new BigDecimal("60.0"),
                "Grãos",
                "Alfa Fornecedora");

        List<ProdutoResumoDTO> dtos = List.of(resumoDTO2, resumoDTO);

        when(service.buscarParteDoNome(produtoPesquisado)).thenReturn(dtos);

        ResultActions response = mockMvc.perform(
                        get("/produto/buscar-produto", produtoPesquisado.toLowerCase())
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("produtoPesquisado",produtoPesquisado)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }


    @Test
    void deveRetornar200ParaOrdenarDoMenorParaOMaiorPorCategoriaPesquisada() throws Exception {


        String categoria = "Papelaria";

        ProdutoResumoDTO dto1 = new ProdutoResumoDTO("Caneta",
                new BigDecimal("3.5"),
                "Papelaria",
                "Papelarias Gama");

        ProdutoResumoDTO dto2 = new ProdutoResumoDTO("Lapiseira",
                new BigDecimal("2.5"),
                "Papelaria",
                "Papelarias Gama");

        ProdutoResumoDTO dto3 = new ProdutoResumoDTO("Caderno",
                new BigDecimal("10.0"),
                "Papelaria",
                "Papelarias Gama");

        ProdutoResumoDTO dto4 = new ProdutoResumoDTO("Borracha",
                new BigDecimal("0.5"),
                "Papelaria",
                "Papelarias Gama");

        ProdutoResumoDTO dto5 = new ProdutoResumoDTO("Apontador",
                new BigDecimal("4.5"),
                "Papelaria",
                "Distribuidora Alfa");

        List<ProdutoResumoDTO> dtos = List.of(dto1, dto2, dto3, dto4, dto5);

        when(service.ordenaDoMenorParaOMaior(categoria))
                .thenReturn(dtos);

        ResultActions response = mockMvc.perform(
                        get("/produto/menor-para-maior")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("categoria", categoria)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }


    @Test
    void deveRetornar200ParaOrdenarDoMaiorParaOMenorPorCategoriaPesquisada() throws Exception {


        String categoria = "Papelaria";

        ProdutoResumoDTO dto1 = new ProdutoResumoDTO("Caneta",
                new BigDecimal("3.5"),
                "Papelaria",
                "Papelarias Gama");

        ProdutoResumoDTO dto2 = new ProdutoResumoDTO("Lapiseira",
                new BigDecimal("2.5"),
                "Papelaria",
                "Papelarias Gama");

        ProdutoResumoDTO dto3 = new ProdutoResumoDTO("Caderno",
                new BigDecimal("10.0"),
                "Papelaria",
                "Papelarias Gama");

        ProdutoResumoDTO dto4 = new ProdutoResumoDTO("Borracha",
                new BigDecimal("0.5"),
                "Papelaria",
                "Papelarias Gama");

        ProdutoResumoDTO dto5 = new ProdutoResumoDTO("Apontador",
                new BigDecimal("4.5"),
                "Papelaria",
                "Distribuidora Alfa");

        List<ProdutoResumoDTO> dtos = List.of(dto1, dto2, dto3, dto4, dto5);

        when(service.ordenaDoMaiorParaOMenor(categoria))
                .thenReturn(dtos);

        ResultActions response = mockMvc.perform(
                        get("/produto/maior-para-menor")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("categoria", categoria)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar200ParaBuscaProdutosPorFornecedor() throws Exception {

        String fornecedor = "Alfa Fornecedora";

        ProdutoResumoDTO dto1 = new ProdutoResumoDTO("Arroz",
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora");
        ProdutoResumoDTO dto2 = new ProdutoResumoDTO("Feijão", new BigDecimal("15.25"), "Grãos", "Alfa Fornecedora");

        List<ProdutoResumoDTO> dtos = List.of(dto1, dto2);

        when(service.produtosPorFornecedor(fornecedor)).thenReturn(dtos);

        ResultActions response = mockMvc.perform(get("/produto/produtos-por-fornecedor/{fornecedor}", fornecedor.toLowerCase())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());



    }

    @Test
    void deveRetornar200ParaBuscarProdutosAcimaDeUmValor() throws Exception {

        BigDecimal valorPesquisado = new BigDecimal("150.00");

        ProdutoResumoDTO resumoDTO = new ProdutoResumoDTO("PC Gamer",
                new BigDecimal("5000.00"),
                "Eletrônicos", "Delta Computadores");


        when(service.buscaProdutoMaiorQueUmValor(valorPesquisado)).thenReturn(List.of(resumoDTO));

        ResultActions response = mockMvc.perform(
                        get("/produto/buscar-acima")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("valorPesquisado", String.valueOf(valorPesquisado))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar200ParaOrdenarDoMaiorParaOMenor() throws Exception {


        ProdutoResumoDTO dto1 = new ProdutoResumoDTO("Caneta",
                new BigDecimal("3.5"),
                "Papelaria",
                "Papelarias Gama");

        ProdutoResumoDTO dto2 = new ProdutoResumoDTO("Lapiseira",
                new BigDecimal("2.5"),
                "Papelaria",
                "Papelarias Gama");

        ProdutoResumoDTO dto3 = new ProdutoResumoDTO("Arroz",
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora");

        ProdutoResumoDTO dto4 = new ProdutoResumoDTO("Borracha",
                new BigDecimal("0.5"),
                "Papelaria",
                "Papelarias Gama");

        ProdutoResumoDTO dto5 = new ProdutoResumoDTO("PC Gamer",
                new BigDecimal("5000.00"),
                "Eletrônicos",
                "Delta Computadores");



        List<ProdutoResumoDTO> dtos = List.of(dto1, dto2, dto3, dto4, dto5);

        when(service.produtosEmOrdemCrescente())
                .thenReturn(dtos);

        ResultActions response = mockMvc.perform(
                        get("/produto/ordem-crescente")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar200ParaOrdenarDoMenorParaOMaior() throws Exception {


        ProdutoResumoDTO dto1 = new ProdutoResumoDTO("Caneta",
                new BigDecimal("3.5"),
                "Papelaria",
                "Papelarias Gama");

        ProdutoResumoDTO dto2 = new ProdutoResumoDTO("Lapiseira",
                new BigDecimal("2.5"),
                "Papelaria",
                "Papelarias Gama");

        ProdutoResumoDTO dto3 = new ProdutoResumoDTO("Arroz",
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora");

        ProdutoResumoDTO dto4 = new ProdutoResumoDTO("Borracha",
                new BigDecimal("0.5"),
                "Papelaria",
                "Papelarias Gama");

        ProdutoResumoDTO dto5 = new ProdutoResumoDTO("PC Gamer",
                new BigDecimal("5000.00"),
                "Eletrônicos",
                "Delta Computadores");



        List<ProdutoResumoDTO> dtos = List.of(dto1, dto2, dto3, dto4, dto5);

        when(service.produtosEmOrdemCrescente())
                .thenReturn(dtos);

        ResultActions response = mockMvc.perform(
                        get("/produto/ordem-decrescente")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void DeveRetornar200EEncontrarProdutosPelaInicial() throws Exception {

        String letra = "A";

        ProdutoResumoDTO dto = new ProdutoResumoDTO("Arroz",
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora");

        when(service.buscarProdutosPelaLetraInicial(letra))
                .thenReturn(List.of(dto));


        ResultActions response = mockMvc.perform(
                        get("/produto/listar-pela-inicial")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("letra", letra)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    @Operation(summary = "Testa pesquisa por produto ou categoria, utilizando o produto como parâmetro.")
    void deveRetornar200EEncontrarPorProduto() throws Exception {

        String pesquisa = "Arroz";

        ProdutoResumoDTO dto = new ProdutoResumoDTO("Arroz",
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora");

        when(service.buscarProdutosPelaLetraInicial(pesquisa))
                .thenReturn(List.of(dto));


        ResultActions response = mockMvc.perform(
                        get("/produto/pesquisar-nome-ou-categoria")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pesquisa", pesquisa)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    @Operation(summary = "Testa pesquisa por produto ou categoria, utilizando o produto como parâmetro.")
    void deveRetornar200EEncontrarPorCategoria() throws Exception {

        String pesquisa = "Grãos";

        ProdutoResumoDTO dto = new ProdutoResumoDTO("Arroz",
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora");

        when(service.buscarProdutosPelaLetraInicial(pesquisa))
                .thenReturn(List.of(dto));


        ResultActions response = mockMvc.perform(
                        get("/produto/pesquisar-nome-ou-categoria")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pesquisa", pesquisa)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk());
    }

    @Test
    void deveRetornar200EEncontrarOsCincoProdutosMaisCaros() throws Exception {

        ProdutoResumoDTO dto1 = new ProdutoResumoDTO("PC Gamer",
                new BigDecimal("5000.00"),
                "Eletrônicos",
                "Delta Eletrônicos");

        ProdutoResumoDTO dto2 = new ProdutoResumoDTO("Celular",
                new BigDecimal("3000.00"),
                "Eletrônicos",
                "Delta Eletrônicos");

        ProdutoResumoDTO dto3 = new ProdutoResumoDTO("Videogame",
                new BigDecimal("4500.00"),
                "Eletrônicos",
                "Delta Eletrônicos");

        ProdutoResumoDTO dto4 = new ProdutoResumoDTO("Fogão",
                new BigDecimal("2000.00"),
                "Casa e Eletro",
                "Gama Eletro");

        ProdutoResumoDTO dto5 = new ProdutoResumoDTO("Lava e seca",
                new BigDecimal("4300.00"),
                "Casa e Eletro",
                "Gama Eletro");

        List<ProdutoResumoDTO> dtos = List.of(dto1,dto2,dto3,dto4,dto5);

        when(service.produtosEmOrdemCrescente())
                .thenReturn(dtos);

        ResultActions response = mockMvc.perform(
                        get("/produto/ordem-decrescente")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void deveExcluirProdutoComSucesso() throws Exception {

        //ARRANGE
        Long idExistente = 1L;

        //ACT
        doNothing().when(service).deletarProduto(idExistente);

        mockMvc.perform(delete("/produto/{id}", idExistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        //ASSERT
        verify(service).deletarProduto(idExistente);
    }

    @Test
    void deveRetornarNotFoundQuandoProdutoNaoExistir() throws Exception {

        //ARRANGE
        Long idInexistente = 99L;

        //ACT
        doThrow(new ResourceNotFoundException("Produto não encontrada"))
                .when(service).deletarProduto(idInexistente);

        mockMvc.perform(delete("/produto/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        //ASSERT
        verify(service).deletarProduto(idInexistente);
    }

}
