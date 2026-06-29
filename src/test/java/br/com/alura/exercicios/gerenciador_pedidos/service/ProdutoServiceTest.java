package br.com.alura.exercicios.gerenciador_pedidos.service;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.BusinessRuleException;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

}