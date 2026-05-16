package br.com.alura.exercicios.gerenciador_pedidos.repository;

import br.com.alura.exercicios.gerenciador_pedidos.models.Fornecedor;
import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    Fornecedor findByNomeEqualsIgnoreCase(String nomeFornecedor);

   List<Fornecedor> findByNomeContainingIgnoreCase(String nomeFornecedor);
    Optional<Fornecedor> findFirstByNomeContainingIgnoreCase(String nome);


    Optional<Fornecedor> findByNomeIgnoreCase(String nome);;
}
