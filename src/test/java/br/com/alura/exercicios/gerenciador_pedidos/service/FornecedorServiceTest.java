package br.com.alura.exercicios.gerenciador_pedidos.service;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.ResourceNotFoundException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor.FornecedorRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor.FornecedorResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.models.Fornecedor;
import br.com.alura.exercicios.gerenciador_pedidos.repository.FornecedorRepository;
import br.com.alura.exercicios.gerenciador_pedidos.validacoes.FornecedorValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FornecedorServiceTest {

    @Mock
    private FornecedorRepository repository;

    @Mock
    private FornecedorValidator validator;

    @InjectMocks
    private  FornecedorService service;

    @Test
    void deveCadastrarFornecedor(){

        FornecedorRequestDTO requestDTO = new FornecedorRequestDTO(
                "Alfa Distribuidora",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br"
        );

        when(repository.save(any(Fornecedor.class)))
                .thenAnswer(invocation -> {
                    Fornecedor forn = invocation.getArgument(0);
                    forn.setId(1L);
                    return forn;
                } );

        FornecedorResponseDTO resultado = service.cadastrarFornecedor(requestDTO);

        assertNotNull(resultado);
        assertEquals("Alfa Distribuidora", resultado.nome());
        assertEquals("00000000000100", resultado.cnpj());
        assertEquals("Rua Delta -520 - São Paulo", resultado.endereco());
        assertEquals("alfadistribuidora@alfa.com.br", resultado.email());
        assertEquals(1L, resultado.id());

        verify(validator).validarFornecedor(requestDTO);
        verify(repository).save(any(Fornecedor.class));

    }

    @Test
    void deveRetornarFornecedorPorId(){

        Fornecedor fornecedor = new Fornecedor(new FornecedorRequestDTO(
                "Alfa Distribuidora",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br"));

        fornecedor.setId(1L);


        when(repository.findById(1L)).thenReturn(Optional.of(fornecedor));

        FornecedorResponseDTO resultado = service.buscarFornecedorPorId(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.id());
        assertEquals("Alfa Distribuidora", resultado.nome());
        assertEquals("00000000000100", resultado.cnpj());
        assertEquals("Rua Delta -520 - São Paulo", resultado.endereco());
        assertEquals("alfadistribuidora@alfa.com.br", resultado.email());

        verify(repository).findById(1L);
    }

    @Test
    void deveLancarExcecaoAoBuscarFornecedorPorIdInexistente() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.buscarFornecedorPorId(1L)
        );
    }

    @Test
    void deveExcluirFornecedor(){
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(1L);
        fornecedor.setNome("Alfa Distribuidora");
        fornecedor.setCnpj( "00000000000100");
        fornecedor.setEndereco("Rua Delta -520 - São Paulo");
        fornecedor.setEmail("alfadistribuidora@alfa.com.br");

        when(repository.findById(1L)).thenReturn(Optional.of(fornecedor));

        service.excluirFornecedor(1L);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).delete(fornecedor);
        verifyNoMoreInteractions(repository);


    }

    @Test
    void deveLancarExcecaoAoExcluirFornecedorInexistente() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.excluirFornecedor(1L)
        );

        verify(repository).findById(1L);
        verify(repository, never()).delete(any(Fornecedor.class));
    }

}