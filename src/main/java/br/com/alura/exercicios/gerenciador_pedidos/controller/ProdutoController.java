package br.com.alura.exercicios.gerenciador_pedidos.controller;


import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResumoDTO;
import br.com.alura.exercicios.gerenciador_pedidos.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    ProdutoService service;


    //Cadastra produto
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> salvar(
            @RequestBody ProdutoRequestDTO dto) {

        return ResponseEntity.ok(
                service.cadastrarProduto(dto));
    }

    //Cadastra lote de produtos

    @PostMapping("/batch")
    public List<ProdutoResponseDTO> cadastrarProdutosEmLote(
            @RequestBody List<ProdutoRequestDTO> dtos
    ) {
        return service.cadastrarEmLote(dtos);
    }

    //Busca produto por ID
    @GetMapping("/{id}")
    public ProdutoResponseDTO buscarProdutoPorId (@PathVariable Long id){

        return service.buscarProdutoPorId(id);
    }

    //Busca produto por nome
    @GetMapping("/nome-produto/{nome}")
    public ProdutoResponseDTO buscarProdutoPorNome(@PathVariable String nome){

        return service.buscarProduto(nome);
    }

    //Busca produtos com valores maiores do que o informado
    @GetMapping("/buscar-valor-maior")
    public List<ProdutoResumoDTO> buscarValorMaior(@RequestParam BigDecimal valorPesquisado){
        return service.buscarValorMaior(valorPesquisado);
    }

    //Busca produtos com o valor menor do que o informado
    @GetMapping("/buscar-valor-menor")
    public List<ProdutoResumoDTO> buscarValorMenor(@RequestParam BigDecimal valorPesquisado){
        return service.buscarMenoresValores(valorPesquisado);
    }

    //Busca os três produtos mais caros
    @GetMapping("tres-mais-caros")
    public List<ProdutoResumoDTO> tresProdutosMaisCaros(){
        return service.tresProdutosMaisCaros();
    }

    //Busca os cinco mais baratos de uma categoria
    @GetMapping("/5-mais-baratos-categoria/{categoria}")
    public List<ProdutoResumoDTO> cincoMaisBaratosCategorias(@RequestParam String categoriaPesquisada){
        return service.cincoProdutosMaisBaratosDeUmaCategoria(categoriaPesquisada);
    }

    //Busca produtos usando parte do nome
    @GetMapping("/buscar-produto")
    public List<ProdutoResumoDTO> buscarProdutoParteDoNome (@RequestParam String produtoPesquisado){
        return service.buscarParteDoNome(produtoPesquisado);
    }

    //Busca os produtos de uma categoria e ordena do menor para o maior valor
    @GetMapping("/menor-para-maior")
       public List<ProdutoResumoDTO> ordenarDoMenorParaOMaior(@RequestParam String categoria){
        return service.ordenaDoMenorParaOMaior(categoria);
    }

    //Busca os produtos de uma categoria e ordena do maior para o menor valor
    @GetMapping("/maior-para-menor")
    public List<ProdutoResumoDTO> ordenarDoMaiorParaOMenor(@RequestParam String categoria){
        return service.ordenaDoMaiorParaOMenor(categoria);
    }

    //Lista produtos por fornecedor
    @GetMapping("/produtos-por-fornecedor/{fornecedor}")
    public List<ProdutoResumoDTO> produtosPorFornecedor(@PathVariable String fornecedor){
        return service.produtosPorFornecedor(fornecedor);
    }

    //Lista produtos maiores do que determinado valor
    @GetMapping("/buscar-acima")
    public List<ProdutoResumoDTO> produtosMaioresQueUmValor (@RequestParam BigDecimal valorPesquisado){
        return service.buscaProdutoMaiorQueUmValor(valorPesquisado);
    }

    // Retorna a lista de produtos em ordem crescente
    @GetMapping("/ordem-crescente")
    public List<ProdutoResumoDTO> ordemCrescente(){
        return service.produtosEmOrdemCrescente();
    }

    // Retorna a lista de produtos em ordem decrescente
    @GetMapping("/ordem-decrescente")
    public List<ProdutoResumoDTO> ordemDecrescente(){
        return service.produtosEmOrdemDecrescente();
    }
    //Busca produtos pela letra inicial
    @GetMapping("listar-pela-inicial")
    public List<ProdutoResumoDTO> pesquisarPelaInicial(@RequestParam String letra){
        return service.buscarProdutosPelaLetraInicial(letra);
    }

    //Busca produtos por nome ou categoria
    @GetMapping("pesquisar-nome-ou-categoria")
    public List<ProdutoResumoDTO> pesquisarNomeOuCategoria(@RequestParam String pesquisa){
        return service.buscarPorProdutoOuCategoria(pesquisa);
    }

    @GetMapping("cinco-mais-caros")
    public List<ProdutoResumoDTO> cincoMaisCaros(){
        return service.buscarCincoMaisCaros();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void>excluirProduto(@PathVariable Long id){
        service.deletarProduto(id);

        return ResponseEntity.noContent().build();
    }

}
