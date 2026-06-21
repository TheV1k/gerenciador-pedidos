package br.com.alura.exercicios.gerenciador_pedidos.service;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.ResourceNotFoundException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria.CategoriaRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria.CategoriaResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria.CategoriaResumoDTO;
import br.com.alura.exercicios.gerenciador_pedidos.models.Categoria;
import br.com.alura.exercicios.gerenciador_pedidos.repository.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository repository;

    @InjectMocks
    private CategoriaService service;

    @Test

    void deveCadastrarNovaCategoria(){

        CategoriaRequestDTO requestDTO = new CategoriaRequestDTO("Bebidas");

        when(repository.save(any(Categoria.class)))
                .thenAnswer(invocation -> {
                    Categoria cat = invocation.getArgument(0);
                    cat.setId(1L);
                    return cat;
                } );

        CategoriaResponseDTO resultado = service.cadastrarCategoria(requestDTO);

        assertNotNull(resultado);
        assertEquals("Bebidas", resultado.nome());
        assertEquals(1L, resultado.id());

        verify(repository).save(any(Categoria.class));

    }

    @Test
    void deveRetornarCategoria(){

        Categoria categoria = new Categoria();
        categoria.setNome("Bebidas");

        when(repository.findByNomeContainingIgnoreCase("Bebidas")).thenReturn(List.of(categoria));

        List<CategoriaResumoDTO> resultado = service.buscarCategoria("Bebidas");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Bebidas", resultado.get(0).nome());

        verify(repository)
                .findByNomeContainingIgnoreCase("Bebidas");

    }

    @Test
    void deveRetornarCategoriaPorId(){

       Categoria categoria = new Categoria();
       categoria.setId(1L);
       categoria.setNome("Bebidas");

       when(repository.findById(1L)).thenReturn(Optional.of(categoria));

        CategoriaResponseDTO resultado = service.buscarCategoriaPorId(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.id());
        assertEquals("Bebidas", resultado.nome());

        verify(repository).findById(1L);
    }

    @Test
    void deveExcluirCategoria(){

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Bebidas");

        when(repository.findById(1L)).thenReturn(Optional.of(categoria));

        service.excluircategoria(1L);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).delete(categoria);
        verifyNoMoreInteractions(repository);

    }

    @Test
    void deveLancarExcecaoAoExcluirCategoriaInexistente() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.excluircategoria(1L)
        );

        verify(repository).findById(1L);
        verify(repository, never()).delete(any(Categoria.class));
    }


}