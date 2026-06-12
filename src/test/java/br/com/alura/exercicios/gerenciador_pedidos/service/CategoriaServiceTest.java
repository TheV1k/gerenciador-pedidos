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
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test

    void deveCadastrarNovaCategoria(){

        CategoriaRequestDTO requestDTO = new CategoriaRequestDTO("Bebidas");

        when(categoriaRepository.save(any(Categoria.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        CategoriaResponseDTO resultado = categoriaService.cadastrarCategoria(requestDTO);

        assertNotNull(resultado);
        assertEquals("Bebidas", resultado.nome());

        verify(categoriaRepository).save(any(Categoria.class));

    }

    @Test
    void deveRetornarCategoria(){

        Categoria categoria = new Categoria();
        categoria.setNome("Bebidas");

        when(categoriaRepository.findByNomeContainingIgnoreCase("Bebidas")).thenReturn(List.of(categoria));

        List<CategoriaResumoDTO> resultado = categoriaService.buscarCategoria("Bebidas");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Bebidas", resultado.get(0).nome());

        verify(categoriaRepository)
                .findByNomeContainingIgnoreCase("Bebidas");

    }

    @Test
    void deveRetornarCategoriaPorId(){

       Categoria categoria = new Categoria();
       categoria.setId(1L);
       categoria.setNome("Bebidas");

       when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        CategoriaResponseDTO resultado = categoriaService.buscarCategoriaPorId(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.id());
        assertEquals("Bebidas", resultado.nome());

        verify(categoriaRepository).findById(1L);
    }

    @Test
    void deveExcluirCategoria(){

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Bebidas");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        categoriaService.excluircategoria(1L);

        verify(categoriaRepository, times(1)).findById(1L);
        verify(categoriaRepository, times(1)).delete(categoria);
        verifyNoMoreInteractions(categoria);

    }

    @Test
    void deveLancarExcecaoAoExcluirCategoriaInexistente() {

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> categoriaService.excluircategoria(1L)
        );

        verify(categoriaRepository).findById(1L);
        verify(categoriaRepository, never()).delete(any(Categoria.class));
    }

}