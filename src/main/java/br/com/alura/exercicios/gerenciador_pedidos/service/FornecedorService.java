package br.com.alura.exercicios.gerenciador_pedidos.service;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.ResourceNotFoundException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor.FornecedorRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor.FornecedorResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor.FornecedorResumoDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.models.Fornecedor;
import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import br.com.alura.exercicios.gerenciador_pedidos.repository.FornecedorRepository;
import br.com.alura.exercicios.gerenciador_pedidos.validacoes.FornecedorValidator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FornecedorService {

    private final FornecedorRepository repositorioFornecedor;
    private final FornecedorValidator validator;

    public FornecedorService(FornecedorRepository repositorioFornecedor, FornecedorValidator validator) {
        this.repositorioFornecedor = repositorioFornecedor;
        this.validator = validator;
    }

    private FornecedorResponseDTO toResponseDTO(Fornecedor fornecedor) {
        return new FornecedorResponseDTO(fornecedor.getId(),
                fornecedor.getNome(),
                fornecedor.getCnpj(),
                fornecedor.getEndereco(),
                fornecedor.getEmail());
    }

    private FornecedorResumoDTO converteResumoDTO(Fornecedor fornecedor){

        return new FornecedorResumoDTO(fornecedor.getNome(),
                fornecedor.getCnpj(),
                fornecedor.getEndereco(),
                fornecedor.getEmail());
    }
    //Cadastra fornecedor
    public FornecedorResponseDTO cadastrarFornecedor(FornecedorRequestDTO dto) {

        validator.validarFornecedor(dto);
        Fornecedor fornecedor = new Fornecedor(dto);

        Fornecedor fornecedorSalvo = repositorioFornecedor.save(fornecedor);

        return toResponseDTO(fornecedorSalvo);
    }

    //Deleta fornecedor
    public void excluirFornecedor(Long id) {

        Fornecedor fornecedor = repositorioFornecedor.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Fornecedor não encontrado"));

        repositorioFornecedor.delete(fornecedor);
    }

    //buscar Fornecedor por ID
    public FornecedorResponseDTO buscarFornecedorPorId(Long id) {
        Fornecedor fornecedor = repositorioFornecedor.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));

        return toResponseDTO(fornecedor);
    }
}