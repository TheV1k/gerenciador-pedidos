package br.com.alura.exercicios.gerenciador_pedidos.controller;

import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.PedidoRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.PedidoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    PedidoService service;

    //Cadastra pedido
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> salvar(
            @RequestBody PedidoRequestDTO dto) {

        return ResponseEntity.ok(
                service.cadastrarPedido(dto));
    }

    //Buscar pedido por ID
    @GetMapping("/{id}")
    public List<PedidoResponseDTO> pedidoPorId(@PathVariable Long id){
       return service.buscarPedidoPorId(id);
    }

    //Buscar pedidos não entregues
    @GetMapping("pedidos-nao-entregues")
    public List<PedidoResponseDTO> pedidosNaoEntregues (){
        return service.buscarPedidosSemData();
    }

    //Buscar pedidos entregues
    @GetMapping("pedidos-entregues")
    public List<PedidoResponseDTO> pedidosEntregues (){
        return service.buscarPedidosEntregue();
    }

    //Buscar pedidos realizados antes de uma data
    @GetMapping("pedido-anterior")
    public List<PedidoResponseDTO> pedidoRealizadoAntesDeUmaData(@RequestParam LocalDate data){
        return service.pedidosFeitosAntesDeUmaData(data);
    }

    //Buscar pedidos entregues antes de uma data
    @GetMapping("pedido-entregue-antes")
    public List<PedidoResponseDTO> pedidoEntregueAntesDeUmaData(@RequestParam LocalDate data){
        return service.pedidosEntreguesAntesDeUmaData(data);
    }


    //Buscar pedidos realizados depois de uma data
    @GetMapping("pedido-posterior")
    public List<PedidoResponseDTO> pedidoRealizadoDepoisDeUmaData(@RequestParam LocalDate data){
        return service.pedidosFeitosDepoisDeUmaData(data);
    }

    //Buscar pedidos entregues depois de uma data
    @GetMapping("pedido-entregue-depois")
    public List<PedidoResponseDTO> pedidoEntregueDepoisDeUmaData(@RequestParam LocalDate data){
        return service.pedidosEntregueDepoisDeUmaData(data);
    }

    //Busca pedidos realizados entre duas datas
    @GetMapping("pedidos-feitos-entre")
    public List<PedidoResponseDTO>pedidosFeitosEntreDuasDatas(@RequestParam LocalDate dataInicial,
                                                             @RequestParam LocalDate dataFinal){

        return service.pedidosFeitosEntreDuasDatas(dataInicial, dataFinal);
    }

    //Busca pedidos etregues entre duas datas
    @GetMapping("pedidos-feitos-entre")
    public List<PedidoResponseDTO>pedidosEntreguesEntreDuasDatas(@RequestParam LocalDate dataInicial,
                                                             @RequestParam LocalDate dataFinal){

        return service.pedidosEntreguesEntreDuasDatas(dataInicial, dataFinal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> receberPedido(@PathVariable Long id,
                                                 @RequestParam LocalDate dataEntrega){

      PedidoResponseDTO response =  service.receberPedido(id, dataEntrega);

      return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void>excluirPedido(@PathVariable Long id){
        service.deletarPedido(id);

        return ResponseEntity.noContent().build();
    }
}
