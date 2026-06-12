package br.com.alura.exercicios.gerenciador_pedidos.controller;

import br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria.CategoriaRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria.CategoriaResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Categoria.CategoriaResumoDTO;
import br.com.alura.exercicios.gerenciador_pedidos.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @Operation(summary = "Cadastra categoria")
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> salvar(
            @RequestBody CategoriaRequestDTO dto) {

        return ResponseEntity.ok(
                service.cadastrarCategoria(dto));
    }

    @Operation(summary = "Busca categoria por ID")
    @GetMapping("/{id}")
    public CategoriaResponseDTO buscarCategoriaPorId(@PathVariable Long id){
        return service.buscarCategoriaPorId(id);
    }

    @Operation(summary = "Lista produtos de uma categoria")
    @GetMapping("/produtos-por-categoria/{categoria}")
    public List<CategoriaResumoDTO> produtosPorCategoria(@PathVariable String categoria){
        return service.buscarCategoria(categoria);
    }

    @Operation(summary = "Apaga uma categoria")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCategoria(@PathVariable Long id) {
        service.excluircategoria(id);
        return ResponseEntity.noContent().build();

    }
}
