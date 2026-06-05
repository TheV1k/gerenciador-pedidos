package br.com.alura.exercicios.gerenciador_pedidos.controller;

import br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor.FornecedorRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor.FornecedorResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor.FornecedorResumoDTO;
import br.com.alura.exercicios.gerenciador_pedidos.service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fornecedor")
public class FornecedorController {

    @Autowired
    private FornecedorService service;

    //Cadastra fornecedor
    @PostMapping
    public ResponseEntity<FornecedorResponseDTO> salvar(
            @RequestBody FornecedorRequestDTO dto) {

        return ResponseEntity.ok(
                service.cadastrarFornecedor(dto));
    }

    //Busca fornecedor pelo ID
    @GetMapping("/{id}")
    public FornecedorResumoDTO buscarFornecedorPorId(@PathVariable Long id){

        return service.buscarFornecedorPorId(id);
    }

    //Excluir Fornecedor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirFornecedor(@PathVariable Long id) {
        service.excluirFornecedor(id);
        return ResponseEntity.noContent().build();
    }

}
