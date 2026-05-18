package br.com.alura.exercicios.gerenciador_pedidos.service;

import br.com.alura.exercicios.gerenciador_pedidos.models.Fornecedor;
import br.com.alura.exercicios.gerenciador_pedidos.models.Pedido;
import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import br.com.alura.exercicios.gerenciador_pedidos.repository.FornecedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FornecedorService {

    private final FornecedorRepository repositorioFornecedor;

    public FornecedorService(FornecedorRepository repositorioFornecedor) {
        this.repositorioFornecedor = repositorioFornecedor;
    }

    //Cadastra fornecedor
    public Fornecedor cadastrarFornecedor(String novoFornecedor) {

        Fornecedor fornecedorCadastrado = new Fornecedor(novoFornecedor);
        repositorioFornecedor.save(fornecedorCadastrado);

        return fornecedorCadastrado;
    }

    //Deleta fornecedor
public Fornecedor excluiFornecedor(String excluirFornecedor){


    Optional<Fornecedor> fornecedorOptional = repositorioFornecedor
            .findByNomeIgnoreCase(excluirFornecedor);

    if (fornecedorOptional.isPresent()){

        Fornecedor fornecedor = fornecedorOptional.get();
        repositorioFornecedor.delete(fornecedor);

        return fornecedor;
    }
    return null;
}

}