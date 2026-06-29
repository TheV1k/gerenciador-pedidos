package br.com.alura.exercicios.gerenciador_pedidos.validacoes;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.BusinessRuleException;
import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.DuplicateResourceException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.repository.ProdutoRepository;
import org.springframework.stereotype.Component;

@Component
public class ProdutoValidator {

    private final ProdutoRepository repository;

    public ProdutoValidator(ProdutoRepository repository) {
        this.repository = repository;
    }

    public void validarProduto(ProdutoRequestDTO dto) {
        validarNome(dto);
        validarUnicidade(dto);
        validarCategoria(dto);
        validarFornecedor(dto);
    }

    private void validarNome(ProdutoRequestDTO dto) {
        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new BusinessRuleException("Nome do produto é obrigatório");
        }
    }

    private void validarUnicidade(ProdutoRequestDTO dto) {
        if (repository.existsByNomeIgnoreCase(dto.nome())) {
            throw new DuplicateResourceException("Produto já cadastrado");
        }
    }

    private void validarCategoria(ProdutoRequestDTO dto) {
        if (dto.nomeCategoria() == null || dto.nomeCategoria().isBlank()) {
            throw new BusinessRuleException("Categoria é obrigatória");
        }
    }

    private void validarFornecedor(ProdutoRequestDTO dto) {
        if (dto.nomeFornecedor() == null || dto.nomeFornecedor().isBlank()) {
            throw new BusinessRuleException("Fornecedor é obrigatório");
        }
    }
}