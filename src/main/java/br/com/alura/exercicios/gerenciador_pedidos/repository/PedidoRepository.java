package br.com.alura.exercicios.gerenciador_pedidos.repository;

import br.com.alura.exercicios.gerenciador_pedidos.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByDataIsNull();


    List<Pedido> findByDataIsNotNull();

    List<Pedido> findByDataIsBefore(LocalDate pesquisaData);

    List<Pedido> findByDataIsAfter(LocalDate data);

    List<Pedido> findByDataIsBetween(LocalDate data1, LocalDate data2);
}
