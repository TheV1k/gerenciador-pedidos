package br.com.alura.exercicios.gerenciador_pedidos.service;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.ResourceNotFoundException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.models.Categoria;
import br.com.alura.exercicios.gerenciador_pedidos.models.Fornecedor;
import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import br.com.alura.exercicios.gerenciador_pedidos.repository.CategoriaRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.FornecedorRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.ProdutoRepository;
import br.com.alura.exercicios.gerenciador_pedidos.validacoes.ProdutoValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.ExpectedCount.never;


@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @Mock
    private CategoriaRepository repositorioCategoria;

    @Mock

    private FornecedorRepository repositorioFornecedor;

    @Mock
    private ProdutoValidator validator;

    @Mock
    private ProdutoRequestDTO dto;

    @Mock
    private ProdutoResponseDTO ResponseDto;

    @Spy
    @InjectMocks
    private ProdutoService service;


    @Test
    void deveCadastrarProduto(){

        Categoria categoria = new Categoria("Grãos");

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(2L);
        fornecedor.setNome("Alfa Fornecedora");
        fornecedor.setCnpj("00000000000100");
        fornecedor.setEndereco("Rua Delta -520 - São Paulo");
        fornecedor.setEmail("alfadistribuidora@alfa.com.br");

        this.dto = new ProdutoRequestDTO("Arroz",
                new BigDecimal("45.50"),
                "Grãos",
                "Alfa Fornecedora");

        when(repositorioCategoria.findAllByNomeContainingIgnoreCase(anyString()))
                .thenReturn(List.of(categoria));

        when(repositorioFornecedor.findByNomeContainingIgnoreCase(anyString()))
                .thenReturn(List.of(fornecedor));


        when(repository.save(any(Produto.class)))
                .thenAnswer(invocation -> {
                    Produto produto = invocation.getArgument(0);
                    produto.setId(1L);
                    return produto;
                } );

        ProdutoResponseDTO responseDTO = service.cadastrarProduto(dto);

        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals("Arroz", responseDTO.nome());
        Assertions.assertEquals(new BigDecimal("45.50"), responseDTO.preco());
        Assertions.assertEquals("Grãos", responseDTO.nomeCategoria());
        Assertions.assertEquals("Alfa Fornecedora", responseDTO.nomeFornecedor());
        Assertions.assertEquals(1L,responseDTO.id());


        verify(validator).validarProduto(dto);
        verify(repository).save(any(Produto.class));

    }


    @Test
    void deveLancarExcecaoProdutoNulo() {

        ProdutoRequestDTO dto = new ProdutoRequestDTO(null, null, null, null);

        when(repositorioCategoria.findAllByNomeContainingIgnoreCase(null))
                .thenReturn(Collections.emptyList());

        when(repositorioFornecedor.findByNomeContainingIgnoreCase(null))
                .thenReturn(Collections.emptyList());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.cadastrarProduto(dto);
        });

        verify(validator).validarProduto(dto);
        verify(repository, BDDMockito.never()).save(any());
    }

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
    @Test
    void deveCadastrarLoteComSucesso() {
        // ARRANGE
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO("Teclado", new BigDecimal("150.0"), "Eletrônicos", "TechCorp");
        Categoria categoria = new Categoria("Eletrônicos");
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Tech Corp");

        ProdutoResponseDTO responseDTO = new ProdutoResponseDTO(1L,"Teclado", new BigDecimal("150.0"),"Eletrônicos", "TechCorp");

        when(repositorioCategoria.findAllByNomeContainingIgnoreCase("Eletrônicos"))
                .thenReturn(List.of(categoria));

        when(repositorioFornecedor.findByNomeContainingIgnoreCase("TechCorp"))
                .thenReturn(List.of(fornecedor));

        doReturn(responseDTO).when(service).toResponseDTO(any(Produto.class));

        // ACT
        List<ProdutoResponseDTO> resultado = service.cadastrarEmLote(List.of(requestDTO));

        // ASSERT
        assertThat(resultado).isNotNull().hasSize(1);
        assertThat(resultado.get(0)).isEqualTo(responseDTO);

        verify(repositorioCategoria, times(1)).findAllByNomeContainingIgnoreCase("Eletrônicos");
        verify(repositorioFornecedor, times(1)).findByNomeContainingIgnoreCase("TechCorp");
        verify(repository, times(1)).saveAll(anyList());
    }

    @Test
    void deveLancarExcecaoQuandoCategoriaNaoEncontrada() {
        // ARRANGE
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO("Teclado", new BigDecimal("150.00"),"Inexistente" ,"TechCorp");

        when(repositorioCategoria.findAllByNomeContainingIgnoreCase("Inexistente"))
                .thenReturn(Collections.emptyList());

        // ACT & ASSERT
        assertThatThrownBy(() -> service.cadastrarEmLote(List.of(requestDTO)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Categoria não encontrada");

        verify(repositorioCategoria, times(1)).findAllByNomeContainingIgnoreCase("Inexistente");
        verifyNoInteractions(repositorioFornecedor);
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoFornecedorNaoEncontrado() {
        // ARRANGE
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO("Teclado", new BigDecimal("150.0"),"Eletrônicos", "Inexistente");
        Categoria categoria = new Categoria("Eletrônicos");

        when(repositorioCategoria.findAllByNomeContainingIgnoreCase("Eletrônicos"))
                .thenReturn(List.of(categoria));

        when(repositorioFornecedor.findByNomeContainingIgnoreCase("Inexistente"))
                .thenReturn(Collections.emptyList());

        // ACT & ASSERT
        assertThatThrownBy(() -> service.cadastrarEmLote(List.of(requestDTO)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Fornecedor não encontrado");

        verify(repositorioCategoria, times(1)).findAllByNomeContainingIgnoreCase("Eletrônicos");
        verify(repositorioFornecedor, times(1)).findByNomeContainingIgnoreCase("Inexistente");
        verifyNoInteractions(repository);
    }

    @Test
    void deveRetornarListaVaziaQuandoEntradaForVazia() {
        // ACT
        List<ProdutoResponseDTO> resultado = service.cadastrarEmLote(Collections.emptyList());

        // ASSERT
        assertThat(resultado).isEmpty();
        verify(repository, times(1)).saveAll(Collections.emptyList());
        verifyNoInteractions(repositorioCategoria);
        verifyNoInteractions(repositorioFornecedor);
    }
}