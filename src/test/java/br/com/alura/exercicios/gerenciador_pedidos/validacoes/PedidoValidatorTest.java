package br.com.alura.exercicios.gerenciador_pedidos.validacoes;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.BusinessRuleException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.ItemPedidoRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.PedidoRequestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class PedidoValidatorTest {

    @InjectMocks
    private PedidoValidator validator;

    private PedidoRequestDTO dto;

    @Test
    void deveValidarOPedido(){

        this.dto = new PedidoRequestDTO("Alfa Fornecedora",
                List.of(new ItemPedidoRequestDTO(
                        "Arroz",
                        3)),
                LocalDate.now(),
                null);

        Assertions.assertDoesNotThrow(()-> validator.validarPedido(dto));
        Assertions.assertNotNull(dto);
    }

    @Test
    void deveLancarExcecaoFornecedorNulo(){

        this.dto = new PedidoRequestDTO(null,
                List.of(new ItemPedidoRequestDTO(
                        "Arroz",
                        3)),
                LocalDate.now(),
                null);

        Assertions.assertThrows(BusinessRuleException.class, ()-> validator.validarPedido(dto));

    }

    @Test
    void deveLancarExcecaoFornecedorVazio(){

        this.dto = new PedidoRequestDTO(" ",
                List.of(new ItemPedidoRequestDTO(
                        "Arroz",
                        3)),
                LocalDate.now(),
                null);

        Assertions.assertThrows(BusinessRuleException.class, ()-> validator.validarPedido(dto));

    }

    @Test
    void deveLancarExcecaoPedidoVazio(){

        this.dto = new PedidoRequestDTO("Alfa Distribuidora ",
                null,
                LocalDate.now(),
                null);

        Assertions.assertThrows(BusinessRuleException.class, ()-> validator.validarPedido(dto));

    }
}