package br.com.alura.exercicios.gerenciador_pedidos.repository;

import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.PedidoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<PedidoResponseDTO> findByDataIsNull();


    List<PedidoResponseDTO> findByDataIsNotNull();

    

   // List<Pedido> findByDataBetween(LocalDate data1, LocalDate data2);

    @Query("SELECT p FROM Pedido p WHERE p.data BETWEEN :data1 AND :data2 ")
    List<PedidoResponseDTO> pedidosFeitosEntreDuasDatas(LocalDate data1, LocalDate data2);

    List<PedidoResponseDTO> findByDataAfter(LocalDate data);

    List<PedidoResponseDTO> findByDataBefore(LocalDate data);


}
