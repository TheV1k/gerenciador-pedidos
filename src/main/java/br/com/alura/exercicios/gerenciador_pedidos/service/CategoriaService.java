package br.com.alura.exercicios.gerenciador_pedidos.service;


import br.com.alura.exercicios.gerenciador_pedidos.models.Categoria;
import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import br.com.alura.exercicios.gerenciador_pedidos.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

        private final CategoriaRepository repositorioCategoria;


    public CategoriaService(CategoriaRepository repositorioCategoria) {
        this.repositorioCategoria = repositorioCategoria;
    }

    //Cadastra Categoria

    public void cadastrarCategoria(String nomeCategoria){
        Categoria categoria = new Categoria();
        categoria.setNome(nomeCategoria);

        repositorioCategoria.save(categoria);
    }

    //Retorna os produtos cadastrados por categoria

    public List<Categoria> buscarCategoria(String categoriaPesquisada){

        List<Categoria> produtoPorCategoria = repositorioCategoria
                .findByNomeContainingIgnoreCase(categoriaPesquisada);

        return produtoPorCategoria;
    }

    //Busca o valor máximo de uma categoria

    public Double valorMaximoCategoria(String categoriaCalculada) {

        return repositorioCategoria.calculaValorMaximo(categoriaCalculada);


    }

    public List<Object[]> contarProdutosCategorias() {

        return repositorioCategoria.contarProdutosCategoria();


    }

    //Busca categorias com mais de dez produtos

    public List<Object[]> categoriaMaisDeDezProdutos() {
        return repositorioCategoria.categoriaMaisDeDezProdutos();
    }
}
