package br.com.alura.exercicios.gerenciador_pedidos.repository;

import br.com.alura.exercicios.gerenciador_pedidos.models.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    Fornecedor findByNomeEqualsIgnoreCase(String nomeFornecedor);

   List<Fornecedor> findByNomeContainingIgnoreCase(String nomeFornecedor);
    Fornecedor findFirstByNomeContainingIgnoreCase(String nome);


    Fornecedor findByNomeIgnoreCase(String nome);;

    boolean existsByCnpjIgnoreCase(String cnpj);

    boolean existsByEnderecoIgnoreCase(String endereco);

    boolean existsByEmailIgnoreCase(String email);


    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByCnpjIgnoreCaseAndIdNot(String cnpj, Long id);
}
