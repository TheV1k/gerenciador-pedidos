package br.com.alura.exercicios.gerenciador_pedidos.controller;

import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.PedidoRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.PedidoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    PedidoService service;
    @Operation(summary = "Cadastra pedido" )
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> salvar(
            @RequestBody PedidoRequestDTO dto) {

        return ResponseEntity.ok(
                service.cadastrarPedido(dto));
    }


    @Operation(summary = "Buscar pedido por ID")
    @GetMapping("/{id}")
    public List<PedidoResponseDTO> pedidoPorId(@PathVariable Long id){
       return service.buscarPedidoPorId(id);
    }


    @Operation(summary = "Buscar pedidos não entregues" )
    @GetMapping("pedidos-nao-entregues")
    public List<PedidoResponseDTO> pedidosNaoEntregues (){
        return service.buscarPedidosNaoEntregue();
    }

    @Operation(summary = "Buscar pedidos entregues")
    @GetMapping("pedidos-entregues")
    public List<PedidoResponseDTO> pedidosEntregues (){
        return service.buscarPedidosEntregue();
    }


   @Operation(summary = "Buscar pedidos realizados antes de uma data")
    @GetMapping("pedido-anterior")
    public List<PedidoResponseDTO> pedidoRealizadoAntesDeUmaData(@RequestParam LocalDate data){
        return service.pedidosFeitosAntesDeUmaData(data);
    }


    @Operation(summary = "Buscar pedidos entregues antes de uma data")
    @GetMapping("pedido-entregue-antes")
    public List<PedidoResponseDTO> pedidoEntregueAntesDeUmaData(@RequestParam LocalDate data){
        return service.pedidosEntreguesAntesDeUmaData(data);
    }



    @Operation(summary = "Buscar pedidos realizados depois de uma data")
    @GetMapping("pedido-posterior")
    public List<PedidoResponseDTO> pedidoRealizadoDepoisDeUmaData(@RequestParam LocalDate data){
        return service.pedidosFeitosDepoisDeUmaData(data);
    }


    @Operation(summary = "Buscar pedidos entregues depois de uma data")
    @GetMapping("pedido-entregue-depois")
    public List<PedidoResponseDTO> pedidoEntregueDepoisDeUmaData(@RequestParam LocalDate data){
        return service.pedidosEntregueDepoisDeUmaData(data);
    }


    @Operation(summary = "Busca pedidos realizados entre duas datas")
    @GetMapping("pedidos-feitos-entre")
    public List<PedidoResponseDTO>pedidosFeitosEntreDuasDatas(@RequestParam LocalDate dataInicial,
                                                             @RequestParam LocalDate dataFinal){

        return service.pedidosFeitosEntreDuasDatas(dataInicial, dataFinal);
    }


   @Operation(summary = "Busca pedidos entregues entre duas datas")
    @GetMapping("pedidos-entregues-entre")
    public List<PedidoResponseDTO>pedidosEntreguesEntreDuasDatas(@RequestParam LocalDate dataInicial,
                                                             @RequestParam LocalDate dataFinal){

        return service.pedidosEntreguesEntreDuasDatas(dataInicial, dataFinal);
    }

@Operation(summary = "Gera pedido em pdf")
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> gerarPdf(
            @PathVariable Long id
    ) {

        byte[] pdf = service.gerarPdf(id);

        return ResponseEntity.ok().header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=pedido-" +
                                id +
                                ".pdf"
                ).contentType(
                        MediaType.APPLICATION_PDF
                )
                .body(pdf);
    }

@Operation(summary = "Confirma a entrega de um pedido" )
    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> receberPedido(@PathVariable Long id,
                                                 @RequestParam LocalDate dataEntrega){

      PedidoResponseDTO response =  service.receberPedido(id, dataEntrega);

      return ResponseEntity.ok(response);

    }
    @Operation(summary = "Apaga pedido" )
    @DeleteMapping("/{id}")
    ResponseEntity<Void>excluirPedido(@PathVariable Long id){
        service.deletarPedido(id);

        return ResponseEntity.noContent().build();
    }
}
