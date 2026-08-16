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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repositorioProduto;

    @Mock
    private CategoriaRepository repositorioCategoria;

    @Mock
    private FornecedorRepository repositorioFornecedor;

    @Mock
    private ProdutoValidator validator;

    @InjectMocks
    private ProdutoService produtoService;

    private Categoria categoria;
    private Fornecedor fornecedor;
    private Produto produto;
    private ProdutoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Eletrônicos");

        fornecedor = new Fornecedor();
        fornecedor.setId(1L);
        fornecedor.setNome("Tech Supply");

        requestDTO = new ProdutoRequestDTO("Teclado", new BigDecimal("150.00"), "Eletrônicos", "Tech Supply");

        produto = new Produto(requestDTO);
        produto.setId(10L);
        produto.setCategorias(List.of(categoria));
        produto.setFornecedor(fornecedor);
    }

    @Nested
    @DisplayName("Testes de Cadastro")
    class Cadastro {

        @Test
        @DisplayName("Deve cadastrar produto com sucesso")
        void deveCadastrarProdutoComSucesso() {
            doNothing().when(validator).validarProduto(requestDTO);
            when(repositorioCategoria.findAllByNomeContainingIgnoreCase("Eletrônicos")).thenReturn(List.of(categoria));
            when(repositorioFornecedor.findByNomeContainingIgnoreCase("Tech Supply")).thenReturn(List.of(fornecedor));
            when(repositorioProduto.save(any(Produto.class))).thenReturn(produto);

            ProdutoResponseDTO response = produtoService.cadastrarProduto(requestDTO);

            assertThat(response).isNotNull();
            assertThat(response.id()).isEqualTo(10L);
            assertThat(response.nome()).isEqualTo("Teclado");
            assertThat(response.nomeCategoria()).isEqualTo("Eletrônicos");
            assertThat(response.nomeFornecedor()).isEqualTo("Tech Supply");

            verify(validator).validarProduto(requestDTO);
            verify(repositorioProduto).save(any(Produto.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando categoria não for encontrada ao cadastrar")
        void deveLancarExcecaoQuandoCategoriaNaoEncontrada() {
            doNothing().when(validator).validarProduto(requestDTO);
            when(repositorioCategoria.findAllByNomeContainingIgnoreCase("Eletrônicos")).thenReturn(List.of());

            assertThatThrownBy(() -> produtoService.cadastrarProduto(requestDTO))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Categoria não encontrada");

            verify(repositorioProduto, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando fornecedor não for encontrado ao cadastrar")
        void deveLancarExcecaoQuandoFornecedorNaoEncontrado() {
            doNothing().when(validator).validarProduto(requestDTO);
            when(repositorioCategoria.findAllByNomeContainingIgnoreCase("Eletrônicos")).thenReturn(List.of(categoria));
            when(repositorioFornecedor.findByNomeContainingIgnoreCase("Tech Supply")).thenReturn(List.of());

            assertThatThrownBy(() -> produtoService.cadastrarProduto(requestDTO))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Fornecedor não encontrado");

            verify(repositorioProduto, never()).save(any());
        }

        @Test
        @DisplayName("Deve cadastrar produtos em lote com sucesso")
        void deveCadastrarEmLoteComSucesso() {
            List<ProdutoRequestDTO> dtos = List.of(requestDTO);

            doNothing().when(validator).validarProduto(any());
            when(repositorioCategoria.findAllByNomeContainingIgnoreCase("Eletrônicos")).thenReturn(List.of(categoria));
            when(repositorioFornecedor.findByNomeContainingIgnoreCase("Tech Supply")).thenReturn(List.of(fornecedor));
            when(repositorioProduto.saveAll(anyList())).thenReturn(List.of(produto));

            List<ProdutoResponseDTO> respostas = produtoService.cadastrarEmLote(dtos);

            assertThat(respostas).hasSize(1);
            verify(repositorioProduto).saveAll(anyList());
        }
    }

    @Nested
    @DisplayName("Testes de Busca e Consulta")
    class Busca {

        @Test
        @DisplayName("Deve buscar produto por ID com sucesso")
        void deveBuscarProdutoPorIdComSucesso() {
            when(repositorioProduto.findById(10L)).thenReturn(Optional.of(produto));

            ProdutoResponseDTO response = produtoService.buscarProdutoPorId(10L);

            assertThat(response).isNotNull();
            assertThat(response.id()).isEqualTo(10L);
            verify(repositorioProduto).findById(10L);
        }

        @Test
        @DisplayName("Deve lançar exceção ao buscar ID inexistente")
        void deveLancarExcecaoAoBuscarIdInexistente() {
            when(repositorioProduto.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> produtoService.buscarProdutoPorId(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Produto não encontrado.");
        }

        @Test
        @DisplayName("Deve buscar produtos com valor maior que o informado paginado")
        void deveBuscarValorMaiorPaginado() {
            Pageable pageable = PageRequest.of(0, 10);
            BigDecimal valor = new BigDecimal("100.00");

            when(repositorioProduto.findByPrecoGreaterThanEqual(valor, pageable)).thenReturn(List.of(produto));

            List<ProdutoResumoDTO> resultado = produtoService.buscarValorMaior(valor, pageable);

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).nome()).isEqualTo("Teclado");
            verify(repositorioProduto).findByPrecoGreaterThanEqual(valor, pageable);
        }

        @Test
        @DisplayName("Deve buscar os 3 produtos mais caros")
        void deveBuscarTresProdutosMaisCaros() {
            when(repositorioProduto.findTop3ByOrderByPrecoDesc()).thenReturn(List.of(produto));

            List<ProdutoResumoDTO> resultado = produtoService.tresProdutosMaisCaros();

            assertThat(resultado).hasSize(1);
            verify(repositorioProduto).findTop3ByOrderByPrecoDesc();
        }
    }

    @Nested
    @DisplayName("Testes de Exclusão")
    class Exclusao {

        @Test
        @DisplayName("Deve deletar produto com sucesso quando ID existir")
        void deveDeletarProdutoComSucesso() {
            when(repositorioProduto.existsById(10L)).thenReturn(true);

            produtoService.deletarProduto(10L);

            verify(repositorioProduto).deleteById(10L);
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar deletar ID inexistente")
        void deveLancarExcecaoAoDeletarIdInexistente() {
            when(repositorioProduto.existsById(99L)).thenReturn(false);

            assertThatThrownBy(() -> produtoService.deletarProduto(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Produto não encontrado");

            verify(repositorioProduto, never()).deleteById(anyLong());
        }
    }
}