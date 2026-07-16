package br.com.alura.exercicios.gerenciador_pedidos.validacoes;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.BusinessRuleException;
import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.DuplicateResourceException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Produto.ProdutoRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.repository.ProdutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ProdutoValidatorTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoValidator validator;

    private ProdutoRequestDTO dto;

    @Test
    void deveValidarProduto(){

        this.dto = new ProdutoRequestDTO(
                "Arroz",
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora"
        );

        Assertions.assertDoesNotThrow(()-> validator.validarProduto(dto));
    }

    @Test
    void deveLançarExcecaoNomeNulo(){

        this.dto = new ProdutoRequestDTO(
                null,
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora"
        );

      Assertions.assertThrows(BusinessRuleException.class,
              ()-> validator.validarProduto(dto));
    }

    @Test
    void deveLançarExcecaoNomeEmBranco(){

        this.dto = new ProdutoRequestDTO(
                "",
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora"
        );

        Assertions.assertThrows(BusinessRuleException.class,
                ()-> validator.validarProduto(dto));
    }

    @Test
    void deveLançarExcecaoCategoriaNulo(){

        this.dto = new ProdutoRequestDTO(
                "Arroz",
                new BigDecimal("25.5"),
                null,
                "Alfa Fornecedora"
        );

        Assertions.assertThrows(BusinessRuleException.class,
                ()-> validator.validarProduto(dto));
    }

    @Test
    void deveLançarExcecaoCategoriaEmBranco(){

        this.dto = new ProdutoRequestDTO(
                "Arroz",
                new BigDecimal("25.5"),
                "",
                "Alfa Fornecedora"
        );

        Assertions.assertThrows(BusinessRuleException.class,
                ()-> validator.validarProduto(dto));
    }

    @Test
    void deveLançarFornecedorNulo(){

        this.dto = new ProdutoRequestDTO(
                "Arroz",
                new BigDecimal("25.5"),
                "Grãos",
                null
        );

        Assertions.assertThrows(BusinessRuleException.class,
                ()-> validator.validarProduto(dto));
    }

    @Test
    void deveLançarFornecedorEmBranco(){

        this.dto = new ProdutoRequestDTO(
                "Arroz",
                new BigDecimal("25.5"),
                "Grãos",
                ""
        );

        Assertions.assertThrows(BusinessRuleException.class,
                ()-> validator.validarProduto(dto));
    }

    @Test
    void deveLançarExcecaoDuplicidade(){

        this.dto = new ProdutoRequestDTO(
                "Arroz",
                new BigDecimal("25.5"),
                "Grãos",
                "Alfa Fornecedora"
        );

        BDDMockito.when(repository.existsByNomeIgnoreCase(dto.nome())).thenReturn(true);

        Assertions.assertThrows(DuplicateResourceException.class,
                ()-> validator.validarProduto(dto),
                "Deveria lançar DuplicateResourceException.class, mas não lançou!");

    }

}