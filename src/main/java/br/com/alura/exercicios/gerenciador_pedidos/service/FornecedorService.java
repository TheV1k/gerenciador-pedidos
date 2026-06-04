package br.com.alura.exercicios.gerenciador_pedidos.service;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.ResourceNotFoundException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor.FornecedorRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor.FornecedorResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.models.Fornecedor;
import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import br.com.alura.exercicios.gerenciador_pedidos.repository.FornecedorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FornecedorService {

    private final FornecedorRepository repositorioFornecedor;

    public FornecedorService(FornecedorRepository repositorioFornecedor) {
        this.repositorioFornecedor = repositorioFornecedor;
    }

    private FornecedorResponseDTO toResponseDTO(Fornecedor fornecedor) {
        return new FornecedorResponseDTO(fornecedor.getId(),
                fornecedor.getNome(),
                fornecedor.getCnpj(),
                fornecedor.getEndereco(),
                fornecedor.getEmail());
    }
    //Cadastra fornecedor
    public FornecedorResponseDTO cadastrarFornecedor(FornecedorRequestDTO dto) {

        Fornecedor fornecedor = new Fornecedor(dto);

        Fornecedor fornecedorSalvo = repositorioFornecedor.save(fornecedor);

        return toResponseDTO(fornecedorSalvo);
    }

    //Deleta fornecedor
    public FornecedorResponseDTO excluirFornecedor(String nomeFornecedor) {

        Fornecedor fornecedor = Optional.ofNullable(
                        repositorioFornecedor.findByNomeIgnoreCase(nomeFornecedor))
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Fornecedor não encontrado"));

        repositorioFornecedor.delete(fornecedor);

        return toResponseDTO(fornecedor);
    }

}