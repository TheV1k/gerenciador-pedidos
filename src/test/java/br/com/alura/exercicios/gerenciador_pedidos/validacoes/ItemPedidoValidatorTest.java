package br.com.alura.exercicios.gerenciador_pedidos.validacoes;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.BusinessRuleException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.ItemPedidoRequestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class ItemPedidoValidatorTest {


    private ItemPedidoRequestDTO dto;

    @InjectMocks
    private ItemPedidoValidator validator;



    @Test
    void deveValidarOItem(){

        this.dto = new ItemPedidoRequestDTO(
                "Arroz",
                2
        );


        Assertions.assertDoesNotThrow(
                ()->validator.validarItensDoPedido(dto));

        Assertions.assertNotNull(dto);
        Assertions.assertEquals("Arroz", dto.produto());
        Assertions.assertEquals(2, dto.quantidade());
    }

    @Test
    void deveLancarExcecaoItemNulo(){

        this.dto = new ItemPedidoRequestDTO(
                null,
                2
        );


        Assertions.assertThrows(BusinessRuleException.class,
                ()->validator.validarItensDoPedido(dto));


    }

    @Test
    void deveLancarExcecaoItemVazio(){

        this.dto = new ItemPedidoRequestDTO(
                "",
                2
        );


        Assertions.assertThrows(BusinessRuleException.class,
                ()->validator.validarItensDoPedido(dto));


    }

    @Test
    void deveLancarExcecaoQuantidadeNula(){

        this.dto = new ItemPedidoRequestDTO(
                "Arroz",
                null
        );


        Assertions.assertThrows(BusinessRuleException.class,
                ()->validator.validarItensDoPedido(dto));


    }

    @Test
    void deveLancarExcecaoQuantidadeZero(){

        this.dto = new ItemPedidoRequestDTO(
                "Arroz",
                0
        );


        Assertions.assertThrows(BusinessRuleException.class,
                ()->validator.validarItensDoPedido(dto));


    }

    @Test
    void deveLancarExcecaoQuantidadeNegativa(){

        this.dto = new ItemPedidoRequestDTO(
                "Arroz",
                -1
        );


        Assertions.assertThrows(BusinessRuleException.class,
                ()->validator.validarItensDoPedido(dto));


    }


}