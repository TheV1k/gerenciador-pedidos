package br.com.alura.exercicios.gerenciador_pedidos.controller;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.BusinessRuleException;
import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.ResourceNotFoundException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria.CategoriaRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria.CategoriaResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor.FornecedorRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor.FornecedorResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.models.Categoria;
import br.com.alura.exercicios.gerenciador_pedidos.models.Fornecedor;
import br.com.alura.exercicios.gerenciador_pedidos.repository.FornecedorRepository;
import br.com.alura.exercicios.gerenciador_pedidos.service.FornecedorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FornecedorController.class)
@AutoConfigureJsonTesters
class FornecedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FornecedorService service;

    @Autowired
    private JacksonTester<FornecedorRequestDTO> jsonDto;

    @Mock
    private FornecedorRepository repository;

    @Test
    void deveRetornar200ECadastrarFornecedor() throws Exception {

        //ARRANGE
        FornecedorRequestDTO requestDTO = new FornecedorRequestDTO("Alfa Fornecedora",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br");
        FornecedorResponseDTO responseDTO = new FornecedorResponseDTO(1L,
                "Alfa Fornecedora",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br");

        //ACT

        when(service.cadastrarFornecedor(any())).thenReturn(responseDTO);

        MockHttpServletResponse response =  mockMvc.perform(
                post("/fornecedor")
                        .content(jsonDto.write(requestDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT

        assertEquals(200,response.getStatus());

    }

    @Test
    void deveRetornar400FornecedorEmBranco() throws Exception {

        // ARRANGE
        FornecedorRequestDTO requestDTO = new FornecedorRequestDTO("",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br");

        // ACT & ASSERT
        mockMvc.perform(
                        post("/fornecedor")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonDto.write(requestDTO).getJson())
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400FornecedorNulo() throws Exception {

        // ARRANGE
        FornecedorRequestDTO requestDTO = new FornecedorRequestDTO(null,
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br");

        // ACT & ASSERT
        mockMvc.perform(
                        post("/fornecedor")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonDto.write(requestDTO).getJson())
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400CpjEmBranco() throws Exception {

        // ARRANGE
        FornecedorRequestDTO requestDTO = new FornecedorRequestDTO("Alfa Fornecedora",
                "",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br");

        // ACT & ASSERT
        mockMvc.perform(
                        post("/fornecedor")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonDto.write(requestDTO).getJson())
                )
                .andExpect(status().isBadRequest());
    }
    @Test
    void deveRetornar400CpjNulo() throws Exception {

        // ARRANGE
        FornecedorRequestDTO requestDTO = new FornecedorRequestDTO("Alfa Fornecedora",
                null,
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br");

        // ACT & ASSERT
        mockMvc.perform(
                        post("/fornecedor")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonDto.write(requestDTO).getJson())
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400EnderecoEmBranco() throws Exception {

        // ARRANGE
        FornecedorRequestDTO requestDTO = new FornecedorRequestDTO("Alfa Fornecedora",
                "00000000000100",
                "",
                "alfadistribuidora@alfa.com.br");

        // ACT & ASSERT
        mockMvc.perform(
                        post("/fornecedor")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonDto.write(requestDTO).getJson())
                )
                .andExpect(status().isBadRequest());
    }


    @Test
    void deveRetornar400EnderecoNulo() throws Exception {

        // ARRANGE
        FornecedorRequestDTO requestDTO = new FornecedorRequestDTO("Alfa Fornecedora",
                "00000000000100",
                null,
                "alfadistribuidora@alfa.com.br");

        // ACT & ASSERT
        mockMvc.perform(
                        post("/fornecedor")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonDto.write(requestDTO).getJson())
                )
                .andExpect(status().isBadRequest());
    }
    @Test
    void deveRetornar400EmailEmBranco() throws Exception {

        // ARRANGE
        FornecedorRequestDTO requestDTO = new FornecedorRequestDTO("Alfa Fornecedora",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "");

        // ACT & ASSERT
        mockMvc.perform(
                        post("/fornecedor")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonDto.write(requestDTO).getJson())
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400EmailNulo() throws Exception {

        // ARRANGE
        FornecedorRequestDTO requestDTO = new FornecedorRequestDTO("Alfa Fornecedora",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                null);

        // ACT & ASSERT
        mockMvc.perform(
                        post("/fornecedor")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonDto.write(requestDTO).getJson())
                )
                .andExpect(status().isBadRequest()); // Valida diretamente o status HTTP 400
    }

    @Test
    void deveRetornar200EEncontrarFornecedorPorID() throws Exception {

        //ARRANGE

        Long id = 1L;
        FornecedorResponseDTO responseDTO = new FornecedorResponseDTO(1L,
                "Alfa Fornecedora",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br");

        //ACT

        when(service.buscarFornecedorPorId(id)).thenReturn(responseDTO);

        MockHttpServletResponse response =  mockMvc.perform(get("/fornecedor/{id}",id)
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

        when(service.buscarFornecedorPorId(id)).thenThrow(new BusinessRuleException("Fornecedor não encontrado"));

        MockHttpServletResponse response = mockMvc.perform(get("/fornecedor/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        //ASSERT

        assertEquals(422, response.getStatus());

    }


    @Test
    void deveExcluirFornecedorComSucesso() throws Exception {

        //ARRANGE
        Long idExistente = 1L;

        //ACT
        doNothing().when(service).excluirFornecedor(idExistente);

        mockMvc.perform(delete("/fornecedor/{id}", idExistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        //ASSERT
        verify(service).excluirFornecedor(idExistente);
    }

    @Test
    void deveRetornarNotFoundQuandoCategoriaNaoExistir() throws Exception {

        //ARRANGE
        Long idInexistente = 99L;

        //ACT
        doThrow(new ResourceNotFoundException("Categoria não encontrada"))
                .when(service).excluirFornecedor(idInexistente);

        mockMvc.perform(delete("/fornecedor/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        //ASSERT
        verify(service).excluirFornecedor(idInexistente);
    }


}