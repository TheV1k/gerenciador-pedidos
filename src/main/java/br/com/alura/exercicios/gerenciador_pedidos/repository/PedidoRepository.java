package br.com.alura.exercicios.gerenciador_pedidos.repository;

import br.com.alura.exercicios.gerenciador_pedidos.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByDataIsNull();


    List<Pedido> findByDataIsNotNull();

    

    List<Pedido> findByDataBetween(LocalDate data1, LocalDate data2);

    List<Pedido> findByDataAfter(LocalDate data);

    List<Pedido> findByDataBefore(LocalDate data);


}
