package br.com.alura.exercicios.gerenciador_pedidos.service;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.ResourceNotFoundException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoResumoDTO;
import br.com.alura.exercicios.gerenciador_pedidos.models.Categoria;
import br.com.alura.exercicios.gerenciador_pedidos.models.Fornecedor;
import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import br.com.alura.exercicios.gerenciador_pedidos.repository.CategoriaRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.FornecedorRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.ProdutoRepository;
import br.com.alura.exercicios.gerenciador_pedidos.validacoes.ProdutoValidator;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository repositorioProduto;
    private final CategoriaRepository repositorioCategoria;
    private final FornecedorRepository repositorioFornecedor;
    private final ProdutoValidator validator;

    public ProdutoService(ProdutoRepository repositorioProduto,
                          CategoriaRepository repositorioCategoria,
                          FornecedorRepository repositorioFornecedor,
                          ProdutoValidator validator) {
        this.repositorioProduto = repositorioProduto;
        this.repositorioCategoria = repositorioCategoria;
        this.repositorioFornecedor = repositorioFornecedor;
        this.validator = validator;
    }

    // Cadastro individual com validação e lançamento de exceção para Categoria
    public ProdutoResponseDTO cadastrarProduto(ProdutoRequestDTO dto) {
        validator.validarProduto(dto);

        Categoria categoria = repositorioCategoria
                .findAllByNomeContainingIgnoreCase(dto.nomeCategoria())
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        Fornecedor fornecedor = repositorioFornecedor
                .findByNomeContainingIgnoreCase(dto.nomeFornecedor())
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));

        Produto produto = new Produto(dto);
        produto.setCategorias(List.of(categoria));
        produto.setFornecedor(fornecedor);

        Produto produtoSalvo = repositorioProduto.save(produto);
        return toResponseDTO(produtoSalvo);
    }

    // Cadastro em lote com validação de cada item
    public List<ProdutoResponseDTO> cadastrarEmLote(List<ProdutoRequestDTO> dtos) {
        List<Produto> produtos = dtos.stream()
                .map(dto -> {
                    validator.validarProduto(dto);

                    Categoria categoria = repositorioCategoria
                            .findAllByNomeContainingIgnoreCase(dto.nomeCategoria())
                            .stream()
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

                    Fornecedor fornecedor = repositorioFornecedor
                            .findByNomeContainingIgnoreCase(dto.nomeFornecedor())
                            .stream()
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));

                    Produto produto = new Produto(dto);
                    produto.setCategorias(List.of(categoria));
                    produto.setFornecedor(fornecedor);

                    return produto;
                })
                .toList();

        repositorioProduto.saveAll(produtos);

        return produtos.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // Mapeamento seguro para ResponseDTO
    ProdutoResponseDTO toResponseDTO(Produto produto) {
        String categoria = (produto.getCategorias() != null && !produto.getCategorias().isEmpty())
                ? produto.getCategorias().get(0).getNome()
                : null;

        String fornecedor = Optional.ofNullable(produto.getFornecedor())
                .map(Fornecedor::getNome)
                .orElse(null);

        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getPreco(),
                categoria,
                fornecedor
        );
    }

    // Mapeamento seguro para ResumoDTO (evita NullPointerException e IndexOutOfBoundsException)
    private ProdutoResumoDTO toResumoDTO(Produto produto) {
        String categoria = (produto.getCategorias() != null && !produto.getCategorias().isEmpty())
                ? produto.getCategorias().get(0).getNome()
                : "Sem categoria";

        String fornecedor = Optional.ofNullable(produto.getFornecedor())
                .map(Fornecedor::getNome)
                .orElse("Sem fornecedor");

        return new ProdutoResumoDTO(
                produto.getNome(),
                produto.getPreco(),
                categoria,
                fornecedor
        );
    }

    public ProdutoResponseDTO buscarProduto(String nome) {
        Produto produto = repositorioProduto
                .findByNomeEqualsIgnoreCase(nome)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        return toResponseDTO(produto);
    }

    // Consultas paginadas repassando o Pageable para o repositório
    public List<ProdutoResumoDTO> buscarValorMaior(BigDecimal valorPesquisado, Pageable pageable) {
        return repositorioProduto.findByPrecoGreaterThanEqual(valorPesquisado, pageable)
                .stream()
                .map(this::toResumoDTO)
                .toList();
    }

    public List<ProdutoResumoDTO> buscarMenoresValores(BigDecimal valorPesquisado, Pageable pageable) {
        return repositorioProduto.findByPrecoLessThanEqual(valorPesquisado, pageable)
                .stream()
                .map(this::toResumoDTO)
                .toList();
    }

    public List<ProdutoResumoDTO> tresProdutosMaisCaros() {
        return repositorioProduto.findTop3ByOrderByPrecoDesc()
                .stream()
                .map(this::toResumoDTO)
                .toList();
    }

    public List<ProdutoResumoDTO> cincoProdutosMaisBaratosDeUmaCategoria(String categoriaPesquisada) {
        return repositorioProduto.findTop5ByCategoriasNomeContainingIgnoreCaseOrderByPrecoAsc(categoriaPesquisada)
                .stream()
                .map(this::toResumoDTO)
                .toList();
    }

    public List<ProdutoResumoDTO> buscarParteDoNome(String produtoPesquisado, Pageable pageable) {
        return repositorioProduto.findByNomeContainingIgnoreCase(produtoPesquisado, pageable)
                .stream()
                .map(this::toResumoDTO)
                .toList();
    }

    public List<ProdutoResumoDTO> ordenaDoMenorParaOMaior(String categoriaPesquisada, Pageable pageable) {
        return repositorioProduto.findByCategoriasNomeContainingIgnoreCaseOrderByPrecoAsc(categoriaPesquisada, pageable)
                .stream()
                .map(this::toResumoDTO)
                .toList();
    }

    public List<ProdutoResumoDTO> ordenaDoMaiorParaOMenor(String categoria, Pageable pageable) {
        return repositorioProduto.findByCategoriasNomeContainingIgnoreCaseOrderByPrecoDesc(categoria, pageable)
                .stream()
                .map(this::toResumoDTO)
                .toList();
    }

    public List<ProdutoResumoDTO> produtosPorFornecedor(String buscarFornecedor, Pageable pageable) {
        return repositorioProduto.findByFornecedorNomeContainingIgnoreCase(buscarFornecedor, pageable)
                .stream()
                .map(this::toResumoDTO)
                .toList();
    }

    public List<ProdutoResumoDTO> buscaProdutoMaiorQueUmValor(BigDecimal valorPesquisado, Pageable pageable) {
        return repositorioProduto.buscaProdutoMaiorValor(valorPesquisado, pageable)
                .stream()
                .map(this::toResumoDTO)
                .toList();
    }

    public List<ProdutoResumoDTO> produtosEmOrdemCrescente(Pageable pageable) {
        return repositorioProduto.produtoValorCrescente(pageable)
                .stream()
                .map(this::toResumoDTO)
                .toList();
    }

    public List<ProdutoResumoDTO> produtosEmOrdemDecrescente(Pageable pageable) {
        return repositorioProduto.produtoValorDecrescente(pageable)
                .stream()
                .map(this::toResumoDTO)
                .toList();
    }

    public List<ProdutoResumoDTO> buscarProdutosPelaLetraInicial(String letra, Pageable pageable) {
        return repositorioProduto.produtoPelaInicial(letra, pageable)
                .stream()
                .map(this::toResumoDTO)
                .toList();
    }

    public List<ProdutoResumoDTO> buscarPorProdutoOuCategoria(String pesquisa, Pageable pageable) {
        return repositorioProduto.filtraNomeOuCategoria(pesquisa, pageable)
                .stream()
                .map(this::toResumoDTO)
                .toList();
    }

    public List<ProdutoResumoDTO> buscarCincoMaisCaros() {
        return repositorioProduto.cincoProdutosMaisCaros()
                .stream()
                .map(this::toResumoDTO)
                .toList();
    }

    public ProdutoResponseDTO buscarProdutoPorId(Long id) {
        Produto produto = repositorioProduto.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado."));

        return toResponseDTO(produto);
    }

    public void deletarProduto(Long id) {
        if (!repositorioProduto.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado");
        }

        repositorioProduto.deleteById(id);
    }
}