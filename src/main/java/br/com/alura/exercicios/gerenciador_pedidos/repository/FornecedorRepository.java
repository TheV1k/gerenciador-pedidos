package br.com.alura.exercicios.gerenciador_pedidos.repository;

import br.com.alura.exercicios.gerenciador_pedidos.models.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    Fornecedor findByNomeEqualsIgnoreCase(String nomeFornecedor);

   List<Fornecedor> findByNomeContainingIgnoreCase(String nomeFornecedor);
    Fornecedor findFirstByNomeContainingIgnoreCase(String nome);


    Fornecedor findByNomeIgnoreCase(String nome);;
}
