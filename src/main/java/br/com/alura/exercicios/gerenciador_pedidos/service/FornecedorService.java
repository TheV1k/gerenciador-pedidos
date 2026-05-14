package br.com.alura.exercicios.gerenciador_pedidos.service;

import br.com.alura.exercicios.gerenciador_pedidos.models.Fornecedor;
import br.com.alura.exercicios.gerenciador_pedidos.repository.FornecedorRepository;
import org.springframework.stereotype.Service;

@Service
public class FornecedorService {

    private final FornecedorRepository repositorioFornecedor;

    public FornecedorService(FornecedorRepository repositorioFornecedor) {
        this.repositorioFornecedor = repositorioFornecedor;
    }

    public Fornecedor cadastrarFornecedor(String novoFornecedor) {

        Fornecedor fornecedorCadastrado = new Fornecedor(novoFornecedor);
        repositorioFornecedor.save(fornecedorCadastrado);

        return fornecedorCadastrado;
    }
}