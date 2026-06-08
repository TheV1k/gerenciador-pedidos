package br.com.alura.exercicios.gerenciador_pedidos.repository;

import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.PedidoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByDataEntregaIsNull();


    List<Pedido> findByDataEntregaIsNotNull();


    @Query("""
    SELECT p
    FROM Pedido p
    WHERE p.dataPedido BETWEEN :dataInicial AND :dataFinal
""")
    List<Pedido> pedidosFeitosEntreDuasDatas(@Param("dataInicial") LocalDate dataInicial,
                                                        @Param("dataFinal")  LocalDate dataFinal);

    @Query("""
    SELECT p
    FROM Pedido p
    WHERE p.dataEntrega BETWEEN :dataInicial AND :dataFinal
""")
    List<Pedido> pedidosEntreguesEntreDuasDatas(@Param("dataInicial") LocalDate dataInicial,
                                                        @Param("dataFinal")  LocalDate dataFinal);


    List<Pedido> findByDataPedidoAfter(LocalDate data);

    List<Pedido> findByDataEntregaBefore(LocalDate data);


    List<Pedido> findByDataPedidoBefore(LocalDate data);

    List<Pedido> findByDataEntregaAfter(LocalDate data);
}
