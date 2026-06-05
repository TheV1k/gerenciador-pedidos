package br.com.alura.exercicios.gerenciador_pedidos.service;


import br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria.CategoriaRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria.CategoriaResponseDTO;
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

    public List<CategoriaResponseDTO> buscarCategoria(String categoriaPesquisada){

        List<CategoriaResponseDTO> produtoPorCategoria = repositorioCategoria
                .findByNomeContainingIgnoreCase(categoriaPesquisada);

        return produtoPorCategoria;
    }

    //Busca o valor máximo de uma categoria

    public BigDecimal valorMaximoCategoria(String categoriaCalculada) {

        return repositorioCategoria.calculaValorMaximo(categoriaCalculada);


    }

    public List<CategoriaResponseDTO> contarProdutosCategorias() {

        return repositorioCategoria.contarProdutosCategoria();


    }

    //Busca categorias com mais de dez produtos

    public List<CategoriaResponseDTO> categoriaMaisDeDezProdutos() {
        return repositorioCategoria.categoriaMaisDeDezProdutos();
    }

    private CategoriaResponseDTO toResponse(Categoria categoria){
        return new CategoriaResponseDTO(categoria.getId(),
                categoria.getNome());
    }
}
