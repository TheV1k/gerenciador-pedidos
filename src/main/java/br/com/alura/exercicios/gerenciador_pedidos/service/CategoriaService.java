package br.com.alura.exercicios.gerenciador_pedidos.service;


import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.ResourceNotFoundException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria.CategoriaRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria.CategoriaResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria.CategoriaResumoDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResumoDTO;
import br.com.alura.exercicios.gerenciador_pedidos.models.Categoria;
import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import br.com.alura.exercicios.gerenciador_pedidos.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

        private final CategoriaRepository repositorioCategoria;

        //Converte em ResumoDTO
        public CategoriaResumoDTO toResumoDTO(Categoria categoria) {

            List<ProdutoResumoDTO> produtos = categoria.getProdutos()
                    .stream()
                    .map(produto -> new ProdutoResumoDTO(
                            produto.getNome(),
                            produto.getPreco(),
                            produto.getCategorias().get(0).getNome(),
                            produto.getFornecedor().getNome()
                    ))
                    .toList();

            return new CategoriaResumoDTO(
                    categoria.getNome(),
                    produtos
            );
        }

    public CategoriaService(CategoriaRepository repositorioCategoria) {
        this.repositorioCategoria = repositorioCategoria;
    }

    //Cadastra Categoria

    public CategoriaResponseDTO cadastrarCategoria(CategoriaRequestDTO dto){
        Categoria categoria = new Categoria(dto);

        repositorioCategoria.save(categoria);

        return toResponse(categoria);

    }
    
    //Busca Categoria
    
    public Optional<Categoria> buscarCategoriaNome (String categoria){

        return repositorioCategoria
                .findAllByNomeContainingIgnoreCase(categoria)
                .stream()
                .findFirst();
    }

    //Retorna os produtos cadastrados por categoria

    public List<CategoriaResumoDTO> buscarCategoria(String categoria){

        return repositorioCategoria
                .findByNomeContainingIgnoreCase(categoria).stream().map(this::toResumoDTO).toList();

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
