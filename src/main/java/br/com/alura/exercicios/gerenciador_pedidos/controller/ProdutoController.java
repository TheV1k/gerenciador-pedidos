package br.com.alura.exercicios.gerenciador_pedidos.controller;


import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResumoDTO;
import br.com.alura.exercicios.gerenciador_pedidos.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    ProdutoService service;


   @Operation(summary = "Cadastra produto")
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> salvar(
            @RequestBody @Valid ProdutoRequestDTO dto) {

        return ResponseEntity.ok(
                service.cadastrarProduto(dto));
    }


    @Operation(summary = "Cadastra lote de produtos")
    @PostMapping("/batch")
    public List<ProdutoResponseDTO> cadastrarProdutosEmLote(
            @RequestBody List<ProdutoRequestDTO> dtos
    ) {
        return service.cadastrarEmLote(dtos);
    }

   @Operation(summary = "Busca produto por ID")
    @GetMapping("/{id}")
    public ProdutoResponseDTO buscarProdutoPorId (@PathVariable Long id){

        return service.buscarProdutoPorId(id);
    }
   @Operation(summary = "")
    //Busca produto por nome
    @GetMapping("/nome-produto/{nome}")
    public ProdutoResponseDTO buscarProdutoPorNome(@PathVariable String nome){

        return service.buscarProduto(nome);
    }

   @Operation(summary = " Busca produtos com valores maiores do que o informado")
    @GetMapping("/buscar-valor-maior")
    public List<ProdutoResumoDTO> buscarValorMaior(@RequestParam BigDecimal valorPesquisado,
                                                   @RequestParam int pagina,
                                                   @RequestParam int tamanho){
        return service.buscarValorMaior(valorPesquisado, PageRequest.of(pagina,tamanho));
    }

    @Operation(summary = "Busca produtos com o valor menor do que o informado")
    @GetMapping("/buscar-valor-menor")
    public List<ProdutoResumoDTO> buscarValorMenor(@RequestParam BigDecimal valorPesquisado,
                                                   @RequestParam int pagina,
                                                   @RequestParam int tamanho){
        return service.buscarMenoresValores(valorPesquisado,PageRequest.of(pagina,tamanho));
    }

   @Operation(summary = "Busca os três produtos mais caros")
    @GetMapping("tres-mais-caros")
    public List<ProdutoResumoDTO> tresProdutosMaisCaros(){
        return service.tresProdutosMaisCaros();
    }

    @Operation(summary = "Busca os cinco mais baratos de uma categoria")
    @GetMapping("/5-mais-baratos-categoria/{categoriaPesquisada}")
    public List<ProdutoResumoDTO> cincoMaisBaratosCategorias(@PathVariable String categoriaPesquisada){
        return service.cincoProdutosMaisBaratosDeUmaCategoria(categoriaPesquisada);
    }

    @Operation(summary = "Busca produtos usando parte do nome")
    @GetMapping("/buscar-produto")
    public List<ProdutoResumoDTO> buscarProdutoParteDoNome (@RequestParam String produtoPesquisado,
                                                            @RequestParam int pagina,
                                                            @RequestParam int tamanho){
        return service.buscarParteDoNome(produtoPesquisado,
                PageRequest.of(pagina, tamanho));
    }

    @Operation(summary = "Busca os produtos de uma categoria e ordena do menor para o maior valor")
    @GetMapping("/menor-para-maior")
       public List<ProdutoResumoDTO> ordenarDoMenorParaOMaior(@RequestParam String categoria,
                                                              @RequestParam int pagina,
                                                              @RequestParam int tamanho){
        return service.ordenaDoMenorParaOMaior(categoria, PageRequest.of(pagina, tamanho));
    }

    @Operation(summary = "Busca os produtos de uma categoria e ordena do maior para o menor valor")
    @GetMapping("/maior-para-menor")
    public List<ProdutoResumoDTO> ordenarDoMaiorParaOMenor(@RequestParam String categoria,
                                                           @RequestParam int pagina,
                                                           @RequestParam int tamanho){

        return service.ordenaDoMaiorParaOMenor(categoria,
                PageRequest.of(pagina,tamanho));
    }

    @Operation(summary = "Lista produtos por fornecedor")
    @GetMapping("/produtos-por-fornecedor/{fornecedor}")
    public List<ProdutoResumoDTO> produtosPorFornecedor(@PathVariable String fornecedor,@RequestParam int pagina,
                                                        @RequestParam int tamanho){
        return service.produtosPorFornecedor(fornecedor, PageRequest.of(pagina,tamanho));
    }

   @Operation(summary = "Lista produtos maiores do que determinado valor")
    @GetMapping("/buscar-acima")
    public List<ProdutoResumoDTO> produtosMaioresQueUmValor (@RequestParam BigDecimal valorPesquisado,
                                                             @RequestParam int pagina,
                                                             @RequestParam int tamanho){

        return service.buscaProdutoMaiorQueUmValor(valorPesquisado, PageRequest.of(pagina, tamanho));
    }

    @Operation(summary = "Retorna a lista de produtos em ordem crescente")
    @GetMapping("/ordem-crescente")
    public List<ProdutoResumoDTO> ordemCrescente(@RequestParam int pagina,
                                                 @RequestParam int tamanho){

        return service.produtosEmOrdemCrescente(PageRequest.of(pagina, tamanho));
    }

    @Operation(summary = "Retorna a lista de produtos em ordem decrescente")
    @GetMapping("/ordem-decrescente")
    public List<ProdutoResumoDTO> ordemDecrescente(@RequestParam int pagina,
                                                   @RequestParam int tamanho){
        return service.produtosEmOrdemDecrescente(PageRequest.of(pagina, tamanho));
    }

    @Operation(summary = "Busca produtos pela letra inicial")
    @GetMapping("listar-pela-inicial")
    public List<ProdutoResumoDTO> pesquisarPelaInicial(@RequestParam String letra, @RequestParam int pagina,
                                                       @RequestParam int tamanho){

        return service.buscarProdutosPelaLetraInicial(letra,PageRequest.of(pagina, tamanho));
    }

    @Operation(summary = "Busca produtos por nome ou categoria")
    @GetMapping("pesquisar-nome-ou-categoria")
    public List<ProdutoResumoDTO> pesquisarNomeOuCategoria(@RequestParam String pesquisa, @RequestParam int pagina,
                                                           @RequestParam int tamanho){
        return service.buscarPorProdutoOuCategoria(pesquisa, PageRequest.of(pagina, tamanho));
    }

    @Operation(summary = "Lista os cinco produtos mais caros")
    @GetMapping("cinco-mais-caros")
    public List<ProdutoResumoDTO> cincoMaisCaros(){
        return service.buscarCincoMaisCaros();
    }

   @Operation(summary = "Deleta produto")
    @DeleteMapping("/{id}")
    ResponseEntity<Void>excluirProduto(@PathVariable Long id){
        service.deletarProduto(id);

        return ResponseEntity.noContent().build();
    }

}
