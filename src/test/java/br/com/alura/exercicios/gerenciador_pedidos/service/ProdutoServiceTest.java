package br.com.alura.exercicios.gerenciador_pedidos.service;

import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import br.com.alura.exercicios.gerenciador_pedidos.repository.ProdutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoService service;

    @DisplayName("Busca o produto pelo nome e retorna")
    @Test
    void deveEncontrarProduto() {

        Produto produto = new Produto();
        produto.setNome("Arroz");

        BDDMockito.given(repository.findByNomeEqualsIgnoreCase("Arroz"))
                .willReturn(Optional.of(produto));

        ProdutoResponseDTO responseDTO = service.buscarProduto("Arroz");

        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals("Arroz", responseDTO.nome());

        BDDMockito.then(repository)
                .should()
                .findByNomeEqualsIgnoreCase("Arroz");
    }
}