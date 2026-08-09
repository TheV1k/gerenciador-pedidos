package br.com.alura.exercicios.gerenciador_pedidos.service;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.BusinessRuleException;
import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.ResourceNotFoundException;
import br.com.alura.exercicios.gerenciador_pedidos.controller.ProdutoController;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResumoDTO;
import br.com.alura.exercicios.gerenciador_pedidos.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private final int pagina = 0;
    private final int tamanho = 10;

    @Test
    void deveRetornar200ECadastrarProduto() throws Exception {

        // ARRANGE
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

        when(service.cadastrarProduto(requestDTO)).thenReturn(responseDto);

        // ACT
        MockHttpServletResponse response = mockMvc.perform(
                post("/produto")
                        .content(jsonDto.write(requestDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(200, response.getStatus());
    }

    @Test
    void deveRetornar400ParaProdutoEmBranco() throws Exception {
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO("",
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora");

        mockMvc.perform(
                post("/produto")
                        .content(jsonDto.write(requestDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400ParaProdutoNulo() throws Exception {
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO(null,
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora");

        mockMvc.perform(
                post("/produto")
                        .content(jsonDto.write(requestDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400ParaPrecoNegativo() throws Exception {
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO("Arroz",
                new BigDecimal("-1.00"),
                "Grãos",
                "Alfa Fornecedora");

        mockMvc.perform(
                post("/produto")
                        .content(jsonDto.write(requestDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400ParaCategoriaEmBranco() throws Exception {
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO("Arroz",
                new BigDecimal("25.00"),
                "",
                "Alfa Fornecedora");

        mockMvc.perform(
                post("/produto")
                        .content(jsonDto.write(requestDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400ParaCategoriaNulo() throws Exception {
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO("Arroz",
                new BigDecimal("25.00"),
                null,
                "Alfa Fornecedora");

        mockMvc.perform(
                post("/produto")
                        .content(jsonDto.write(requestDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400ParaFornecedorEmBranco() throws Exception {
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO("Arroz",
                new BigDecimal("25.00"),
                "Grãos",
                "");

        mockMvc.perform(
                post("/produto")
                        .content(jsonDto.write(requestDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400ParaFornecedorNulo() throws Exception {
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO("Arroz",
                new BigDecimal("25.00"),
                "Grãos",
                null);

        mockMvc.perform(
                post("/produto")
                        .content(jsonDto.write(requestDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarStatus200EListaDeProdutosAoCadastrarEmLoteComSucesso() throws Exception {
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO("Teclado", new BigDecimal("150.0"), "Eletrônicos", "TechCorp");
        ProdutoResponseDTO responseDTO = new ProdutoResponseDTO(1L, "Teclado", new BigDecimal("250.0"), "Eletrônicos", "TechCorp");

        List<ProdutoRequestDTO> dtoList = List.of(requestDTO);
        List<ProdutoResponseDTO> responseList = List.of(responseDTO);

        when(service.cadastrarEmLote(anyList())).thenReturn(responseList);

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
        when(service.cadastrarEmLote(Collections.emptyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/produto/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.emptyList())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(service, times(1)).cadastrarEmLote(Collections.emptyList());
    }

    @Test
    void deveRetornarStatus404QuandoCategoriaOuFornecedorNaoEncontrado() throws Exception {
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO("Teclado", new BigDecimal("150.0"), "Inexistente", "TechCorp");

        when(service.cadastrarEmLote(anyList()))
                .thenThrow(new ResourceNotFoundException("Categoria não encontrada"));

        mockMvc.perform(post("/produto/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(requestDTO))))
                .andExpect(status().isNotFound());

        verify(service, times(1)).cadastrarEmLote(anyList());
    }

    @Test
    void deveRetornar200EEncontrarProdutoPorID() throws Exception {
        Long id = 1L;
        ProdutoResponseDTO responseDto = new ProdutoResponseDTO(
                1L,
                "Arroz",
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora");

        when(service.buscarProdutoPorId(id)).thenReturn(responseDto);

        mockMvc.perform(get("/produto/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar422ParaIDNaoEncontrado() throws Exception {
        Long id = 99L;

        when(service.buscarProdutoPorId(id)).thenThrow(new BusinessRuleException("Produto não encontrado"));

        mockMvc.perform(get("/produto/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deveRetornar200EEncontrarCategoriaPorNome() throws Exception {
        String produto = "arroz";
        ProdutoResponseDTO responseDto = new ProdutoResponseDTO(
                1L,
                "Arroz",
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora");

        when(service.buscarProduto(produto)).thenReturn(responseDto);

        mockMvc.perform(get("/produto/nome-produto/{nome}", produto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar404ParaPesquisaProdutoEmBranco() throws Exception {
        String produto = "";

        mockMvc.perform(get("/produto/nome-produto/{nome}", produto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar200EEncontrarProdutoComMaiorValor() throws Exception {
        BigDecimal valorPesquisado = new BigDecimal("23.5");

        ProdutoResumoDTO resumoDTO = new ProdutoResumoDTO(
                "Arroz",
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora"
        );

        when(service.buscarValorMaior(valorPesquisado, PageRequest.of(pagina, tamanho))).thenReturn(List.of(resumoDTO));

        mockMvc.perform(get("/produto/buscar-valor-maior")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("valorPesquisado", String.valueOf(valorPesquisado))
                        .param("pagina", String.valueOf(pagina))
                        .param("tamanho", String.valueOf(tamanho)))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar422ParaBuscaValorMaiorNegativo() throws Exception {
        BigDecimal valorPesquisado = new BigDecimal("-23.5");

        when(service.buscarValorMaior(valorPesquisado, PageRequest.of(pagina, tamanho)))
                .thenThrow(new BusinessRuleException("Valor deve ser maior do que zero"));

        mockMvc.perform(get("/produto/buscar-valor-maior")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("valorPesquisado", String.valueOf(valorPesquisado))
                        .param("pagina", String.valueOf(pagina))
                        .param("tamanho", String.valueOf(tamanho)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deveRetornar200EEncontrarProdutoComMenorValor() throws Exception {
        BigDecimal valorPesquisado = new BigDecimal("23.5");

        ProdutoResumoDTO resumoDTO = new ProdutoResumoDTO(
                "Feijão",
                new BigDecimal("15.5"),
                "Grãos",
                "Alfa Fornecedora"
        );

        when(service.buscarMenoresValores(valorPesquisado, PageRequest.of(pagina, tamanho))).thenReturn(List.of(resumoDTO));

        mockMvc.perform(get("/produto/buscar-valor-menor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("valorPesquisado", String.valueOf(valorPesquisado))
                        .param("pagina", String.valueOf(pagina))
                        .param("tamanho", String.valueOf(tamanho)))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar422ParaBuscaValorMenorNegativo() throws Exception {
        BigDecimal valorPesquisado = new BigDecimal("-23.5");

        when(service.buscarMenoresValores(valorPesquisado, PageRequest.of(pagina, tamanho)))
                .thenThrow(new BusinessRuleException("Valor deve ser maior do que zero"));

        mockMvc.perform(get("/produto/buscar-valor-menor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("valorPesquisado", String.valueOf(valorPesquisado))
                        .param("pagina", String.valueOf(pagina))
                        .param("tamanho", String.valueOf(tamanho)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deveRetornar200ParaOsTresProdutosMaisCaros() throws Exception {
        ProdutoResumoDTO dto1 = new ProdutoResumoDTO("Televisão", new BigDecimal("2000.00"), "Eletrônicos", "Delta Eletro");
        ProdutoResumoDTO dto2 = new ProdutoResumoDTO("Celular", new BigDecimal("3500.00"), "Eletrônicos", "Delta Eletro");
        ProdutoResumoDTO dto3 = new ProdutoResumoDTO("PC Gamer", new BigDecimal("5000.00"), "Eletrônicos", "Delta Eletro");

        List<ProdutoResumoDTO> tresMaisCaros = List.of(dto1, dto2, dto3);

        when(service.tresProdutosMaisCaros()).thenReturn(tresMaisCaros);

        mockMvc.perform(get("/produto/tres-mais-caros")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar200ParaCincoMaisBaratos() throws Exception {
        String categoriaPesquisada = "Papelaria";

        ProdutoResumoDTO dto1 = new ProdutoResumoDTO("Caneta", new BigDecimal("3.5"), "Papelaria", "Papelarias Gama");
        ProdutoResumoDTO dto2 = new ProdutoResumoDTO("Lapiseira", new BigDecimal("2.5"), "Papelaria", "Papelarias Gama");
        ProdutoResumoDTO dto3 = new ProdutoResumoDTO("Caderno", new BigDecimal("10.0"), "Papelaria", "Papelarias Gama");
        ProdutoResumoDTO dto4 = new ProdutoResumoDTO("Borracha", new BigDecimal("0.5"), "Papelaria", "Papelarias Gama");
        ProdutoResumoDTO dto5 = new ProdutoResumoDTO("Apontador", new BigDecimal("4.5"), "Papelaria", "Distribuidoro Alfa");

        List<ProdutoResumoDTO> cincoMaisBaratosDeUmaCategoria = List.of(dto1, dto2, dto3, dto4, dto5);

        when(service.cincoProdutosMaisBaratosDeUmaCategoria(categoriaPesquisada))
                .thenReturn(cincoMaisBaratosDeUmaCategoria);

        mockMvc.perform(get("/produto/5-mais-baratos-categoria/{categoriaPesquisada}", categoriaPesquisada)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar200EBuscarProdutoPorParteDoNome() throws Exception {
        String produtoPesquisado = "Arroz";

        ProdutoResumoDTO resumoDTO = new ProdutoResumoDTO("Arroz Integral", new BigDecimal("30.0"), "Grãos", "Alfa Fornecedora");
        ProdutoResumoDTO resumoDTO2 = new ProdutoResumoDTO("Arroz Negro", new BigDecimal("60.0"), "Grãos", "Alfa Fornecedora");

        List<ProdutoResumoDTO> dtos = List.of(resumoDTO2, resumoDTO);

        when(service.buscarParteDoNome(produtoPesquisado, PageRequest.of(pagina, tamanho))).thenReturn(dtos);

        mockMvc.perform(get("/produto/buscar-produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("produtoPesquisado", produtoPesquisado)
                        .param("pagina", String.valueOf(pagina))
                        .param("tamanho", String.valueOf(tamanho)))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar200ParaOrdenarDoMenorParaOMaiorPorCategoriaPesquisada() throws Exception {
        String categoria = "Papelaria";

        ProdutoResumoDTO dto1 = new ProdutoResumoDTO("Borracha", new BigDecimal("0.5"), "Papelaria", "Papelarias Gama");
        ProdutoResumoDTO dto2 = new ProdutoResumoDTO("Lapiseira", new BigDecimal("2.5"), "Papelaria", "Papelarias Gama");

        List<ProdutoResumoDTO> dtos = List.of(dto1, dto2);

        when(service.ordenaDoMenorParaOMaior(categoria, PageRequest.of(pagina, tamanho))).thenReturn(dtos);

        mockMvc.perform(get("/produto/menor-para-maior")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("categoria", categoria)
                        .param("pagina", String.valueOf(pagina))
                        .param("tamanho", String.valueOf(tamanho)))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar200ParaOrdenarDoMaiorParaOMenorPorCategoriaPesquisada() throws Exception {
        String categoria = "Papelaria";

        ProdutoResumoDTO dto1 = new ProdutoResumoDTO("Caderno", new BigDecimal("10.0"), "Papelaria", "Papelarias Gama");
        ProdutoResumoDTO dto2 = new ProdutoResumoDTO("Apontador", new BigDecimal("4.5"), "Papelaria", "Distribuidora Alfa");

        List<ProdutoResumoDTO> dtos = List.of(dto1, dto2);

        when(service.ordenaDoMaiorParaOMenor(categoria, PageRequest.of(pagina, tamanho))).thenReturn(dtos);

        mockMvc.perform(get("/produto/maior-para-menor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("categoria", categoria)
                        .param("pagina", String.valueOf(pagina))
                        .param("tamanho", String.valueOf(tamanho)))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar200ParaBuscaProdutosPorFornecedor() throws Exception {
        String fornecedor = "Alfa Fornecedora";

        ProdutoResumoDTO dto1 = new ProdutoResumoDTO("Arroz", new BigDecimal("25.5"), "Grãos", "Alfa Fornecedora");
        ProdutoResumoDTO dto2 = new ProdutoResumoDTO("Feijão", new BigDecimal("15.25"), "Grãos", "Alfa Fornecedora");

        List<ProdutoResumoDTO> dtos = List.of(dto1, dto2);

        when(service.produtosPorFornecedor(fornecedor, PageRequest.of(pagina, tamanho))).thenReturn(dtos);

        mockMvc.perform(get("/produto/produtos-por-fornecedor/{fornecedor}", fornecedor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("pagina", String.valueOf(pagina))
                        .param("tamanho", String.valueOf(tamanho)))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar200ParaBuscarProdutosAcimaDeUmValor() throws Exception {
        BigDecimal valorPesquisado = new BigDecimal("150.00");

        ProdutoResumoDTO resumoDTO = new ProdutoResumoDTO("PC Gamer", new BigDecimal("5000.00"), "Eletrônicos", "Delta Computadores");

        when(service.buscaProdutoMaiorQueUmValor(valorPesquisado, PageRequest.of(pagina, tamanho))).thenReturn(List.of(resumoDTO));

        mockMvc.perform(get("/produto/buscar-acima")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("valorPesquisado", String.valueOf(valorPesquisado))
                        .param("pagina", String.valueOf(pagina))
                        .param("tamanho", String.valueOf(tamanho)))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar200ParaOrdenarDoMenorParaOMaior() throws Exception {
        ProdutoResumoDTO dto1 = new ProdutoResumoDTO("Borracha", new BigDecimal("0.5"), "Papelaria", "Papelarias Gama");

        List<ProdutoResumoDTO> dtos = List.of(dto1);

        when(service.produtosEmOrdemCrescente(PageRequest.of(pagina, tamanho))).thenReturn(dtos);

        mockMvc.perform(get("/produto/ordem-crescente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("pagina", String.valueOf(pagina))
                        .param("tamanho", String.valueOf(tamanho)))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar200ParaOrdenarDoMaiorParaOMenor() throws Exception {
        ProdutoResumoDTO dto1 = new ProdutoResumoDTO("PC Gamer", new BigDecimal("5000.00"), "Eletrônicos", "Delta Computadores");

        List<ProdutoResumoDTO> dtos = List.of(dto1);

        when(service.produtosEmOrdemDecrescente(PageRequest.of(pagina, tamanho))).thenReturn(dtos);

        mockMvc.perform(get("/produto/ordem-decrescente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("pagina", String.valueOf(pagina))
                        .param("tamanho", String.valueOf(tamanho)))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar200EEncontrarProdutosPelaInicial() throws Exception {
        String letra = "A";

        ProdutoResumoDTO dto = new ProdutoResumoDTO("Arroz", new BigDecimal("25.5"), "Grãos", "Alfa Fornecedora");

        when(service.buscarProdutosPelaLetraInicial(letra, PageRequest.of(pagina, tamanho))).thenReturn(List.of(dto));

        mockMvc.perform(get("/produto/listar-pela-inicial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("letra", letra)
                        .param("pagina", String.valueOf(pagina))
                        .param("tamanho", String.valueOf(tamanho)))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar200EEncontrarPorProdutoOuCategoria() throws Exception {
        String pesquisa = "Arroz";

        ProdutoResumoDTO dto = new ProdutoResumoDTO("Arroz", new BigDecimal("25.5"), "Grãos", "Alfa Fornecedora");

        when(service.buscarPorProdutoOuCategoria(pesquisa, PageRequest.of(pagina, tamanho))).thenReturn(List.of(dto));

        mockMvc.perform(get("/produto/pesquisar-nome-ou-categoria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("pesquisa", pesquisa)
                        .param("pagina", String.valueOf(pagina))
                        .param("tamanho", String.valueOf(tamanho)))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar200EEncontrarOsCincoProdutosMaisCaros() throws Exception {
        ProdutoResumoDTO dto1 = new ProdutoResumoDTO("PC Gamer", new BigDecimal("5000.00"), "Eletrônicos", "Delta Eletrônicos");
        ProdutoResumoDTO dto2 = new ProdutoResumoDTO("Videogame", new BigDecimal("4500.00"), "Eletrônicos", "Delta Eletrônicos");
        ProdutoResumoDTO dto3 = new ProdutoResumoDTO("Lava e seca", new BigDecimal("4300.00"), "Casa e Eletro", "Gama Eletro");

        List<ProdutoResumoDTO> dtos = List.of(dto1, dto2, dto3);

        when(service.buscarCincoMaisCaros()).thenReturn(dtos);

        mockMvc.perform(get("/produto/cinco-mais-caros")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deveExcluirProdutoComSucesso() throws Exception {
        Long idExistente = 1L;

        doNothing().when(service).deletarProduto(idExistente);

        mockMvc.perform(delete("/produto/{id}", idExistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service).deletarProduto(idExistente);
    }

    @Test
    void deveRetornarNotFoundQuandoProdutoNaoExistir() throws Exception {
        Long idInexistente = 99L;

        doThrow(new ResourceNotFoundException("Produto não encontrado"))
                .when(service).deletarProduto(idInexistente);

        mockMvc.perform(delete("/produto/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service).deletarProduto(idInexistente);
    }
}