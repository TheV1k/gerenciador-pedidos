package br.com.alura.exercicios.gerenciador_pedidos.controller;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.BusinessRuleException;
import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.ResourceNotFoundException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria.CategoriaRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria.CategoriaResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria.CategoriaResumoDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResumoDTO;
import br.com.alura.exercicios.gerenciador_pedidos.service.CategoriaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoriaController.class)
@AutoConfigureJsonTesters
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaService service;

    @Autowired
    private JacksonTester<CategoriaRequestDTO> jsonDto;


    @Test
    void deveRetornar200ECadastrarCategoria() throws Exception {

        //ARRANGE
        CategoriaRequestDTO requestDTO = new CategoriaRequestDTO("Grãos");
        CategoriaResponseDTO responseDTO = new CategoriaResponseDTO(1L, "Grãos");

        //ACT

        when(service.cadastrarCategoria(any())).thenReturn(responseDTO);

        MockHttpServletResponse response =  mockMvc.perform(
                post("/categoria")
                        .content(jsonDto.write(requestDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT

        assertEquals(200,response.getStatus());

    }

    @Test
    void deveRetornar400CategoriaEmBranco() throws Exception {

        //ARRANGE
        CategoriaRequestDTO requestDTO = new CategoriaRequestDTO("");

        //ACT

        MockHttpServletResponse response =  mockMvc.perform(
                post("/categoria")
                        .content(jsonDto.write(requestDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();


        //ASSERT

        assertEquals(400,response.getStatus());

    }

    @Test
    void deveRetornar400CategoriaNulo() throws Exception {

        //ARRANGE
        CategoriaRequestDTO requestDTO = new CategoriaRequestDTO(null);

        //ACT

        MockHttpServletResponse response =  mockMvc.perform(
                post("/categoria")
                        .content(jsonDto.write(requestDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT

        //ASSERT

        assertEquals(400,response.getStatus());

    }


    @Test
    void deveRetornar200EEncontrarCategoriaPorID() throws Exception {

        //ARRANGE

        Long id = 1L;
        CategoriaResponseDTO responseDTO = new CategoriaResponseDTO(1L, "Grãos");

        //ACT

        when(service.buscarCategoriaPorId(id)).thenReturn(responseDTO);

        MockHttpServletResponse response = (MockHttpServletResponse) mockMvc.perform(get("/categoria/{id}",id)
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

        when(service.buscarCategoriaPorId(id)).thenThrow(new BusinessRuleException("Categoria não encontrada"));

        MockHttpServletResponse response = (MockHttpServletResponse) mockMvc.perform(get("/categoria/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        //ASSERT

        assertEquals(422, response.getStatus());

    }

    @Test
    void deveRetornar200EListarProdutosPorCategoria() throws Exception {

        // ARRANGE
        String categoria = "Grãos";
        CategoriaResumoDTO resumoDTO = new CategoriaResumoDTO("Grãos", List.of(new ProdutoResumoDTO("Arroz",
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora")));

        when(service.buscarCategoria(categoria))
                .thenReturn(List.of(resumoDTO));

        // ACT (Correção: remoção da chave extra na URL e remoção do cast)
        MockHttpServletResponse response = mockMvc.perform(
                        get("/categoria/produtos-por-categoria/{categoria}", categoria)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // ASSERT
        assertEquals(200, response.getStatus());
    }

    @Test
    void deveExcluirCategoriaComSucesso() throws Exception {

        //ARRANGE
        Long idExistente = 1L;

        //ACT
        doNothing().when(service).excluirCategoria(idExistente);

        mockMvc.perform(delete("/categoria/{id}", idExistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        //ASSERT
        verify(service).excluirCategoria(idExistente);
    }

    @Test
    void deveRetornarNotFoundQuandoCategoriaNaoExistir() throws Exception {

        //ARRANGE
        Long idInexistente = 99L;

        //ACT
        doThrow(new ResourceNotFoundException("Categoria não encontrada"))
                .when(service).excluirCategoria(idInexistente);

        mockMvc.perform(delete("/categoria/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

       //ASSERT
        verify(service).excluirCategoria(idInexistente);
    }



}



