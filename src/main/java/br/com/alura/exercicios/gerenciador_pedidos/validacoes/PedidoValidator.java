package br.com.alura.exercicios.gerenciador_pedidos.validacoes;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.BusinessRuleException;
import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.ResourceNotFoundException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.ItemPedidoRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.PedidoRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.repository.PedidoRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PedidoValidator {

    private final PedidoRepository repository;

    public PedidoValidator(PedidoRepository repository) {
        this.repository = repository;
    }

    public void validarPedido(PedidoRequestDTO dto){


        if (dto == null){

            throw new BusinessRuleException("Pedido não pode ser nulo!");

        }

        validarFornecedor(dto.fornecedor());
        validarItens(dto.itemPedido());

    }


    private void validarFornecedor(String fornecedor) {

        if (fornecedor == null || fornecedor.isBlank()){

            throw new BusinessRuleException("Fornecedor não pode ser nulo!");
        }
    }

    private void validarItens(List<ItemPedidoRequestDTO> itemPedidoRequestDTOS) {

        if (itemPedidoRequestDTOS == null || itemPedidoRequestDTOS.isEmpty()){

            throw new BusinessRuleException("Pedido deve conter pelo menos um item");
        }
    }

}
