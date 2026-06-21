package br.com.alura.exercicios.gerenciador_pedidos.validacoes;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.ResourceNotFoundException;
import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;

public class ProdutoValidator {

    public void validarProduto(Produto produto){

        validarFornecedor(produto);
        validarCategoria(produto);
    }

    private void validarCategoria(Produto produto) {

        if (produto.getCategorias()== null){
            throw new ResourceNotFoundException("Categoria é obrigatória!");
        }
    }

    private void validarFornecedor(Produto produto) {

        if (produto.getFornecedor() == null){

            throw new ResourceNotFoundException("Fornecedor é obrigatório!");
        }
    }
}
