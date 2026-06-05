package br.com.alura.exercicios.gerenciador_pedidos.repository;

import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.PedidoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<PedidoResponseDTO> findByDataEntregaIsNull();


    List<PedidoResponseDTO> findByDataEntregaIsNotNull();


    @Query("""
    SELECT p
    FROM Pedido p
    WHERE p.dataPedido BETWEEN :dataInicial AND :dataFinal
""")
    List<PedidoResponseDTO> pedidosFeitosEntreDuasDatas(@Param("dataInicial") LocalDate dataInicial,
                                                        @Param("dataFinal")  LocalDate dataFinal);

    List<PedidoResponseDTO> findByDataPedidoAfter(LocalDate data);

    List<PedidoResponseDTO> findByDataEntregaBefore(LocalDate data);


}
