package br.com.alura.exercicios.gerenciador_pedidos.validacoes;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.BusinessRuleException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.ItemPedidoRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.repository.PedidoRepository;
import org.springframework.stereotype.Component;

@Component
public class ItemPedidoValidator {


    private final PedidoRepository repository;


    public ItemPedidoValidator(PedidoRepository repository) {
        this.repository = repository;
    }

    public void validarItensDoPedido(ItemPedidoRequestDTO dto) {
        validarItemPedido(dto.produto());
        validarQuantidade(dto.quantidade());
    }

    private void validarItemPedido(String produto) {

        if (produto == null || produto.isBlank()){

            throw new BusinessRuleException("Item não pode ser vazio!");
        }
    }

    private void validarQuantidade(Integer quantidade) {

        if (quantidade == null || quantidade <= 0){

            throw new BusinessRuleException("Quantidade deve ser maior do que 0!");
        }

        }
    }

