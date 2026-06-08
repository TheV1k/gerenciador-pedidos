package br.com.alura.exercicios.gerenciador_pedidos.service;


import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.ResourceNotFoundException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria.CategoriaRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria.CategoriaResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria.CategoriaResumoDTO;
import br.com.alura.exercicios.gerenciador_pedidos.models.Categoria;
import br.com.alura.exercicios.gerenciador_pedidos.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CategoriaService {

        private final CategoriaRepository repositorioCategoria;


    public CategoriaService(CategoriaRepository repositorioCategoria) {
        this.repositorioCategoria = repositorioCategoria;
    }

    //Cadastra Categoria

    public CategoriaResponseDTO cadastrarCategoria(CategoriaRequestDTO dto){
        Categoria categoria = new Categoria(dto.nome());

        repositorioCategoria.save(categoria);

        return toResponse(categoria);

    }

    //Retorna os produtos cadastrados por categoria

    public List<CategoriaResumoDTO> buscarCategoria(String categoria){

        List<CategoriaResumoDTO> produtoPorCategoria = repositorioCategoria
                .findByNomeContainingIgnoreCase(categoria);

        return produtoPorCategoria;
    }



    private CategoriaResponseDTO toResponse(Categoria categoria){
        return new CategoriaResponseDTO(categoria.getId(),
                categoria.getNome());
    }

    public CategoriaResponseDTO buscarCategoriaPorId(Long id) {
       Categoria  categoria = repositorioCategoria.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

       return toResponse(categoria);

    }

    public void excluircategoria(Long id) {

        Categoria categoria =  repositorioCategoria.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Fornecedor não encontrado"));

        repositorioCategoria.delete(categoria);
    }
}
