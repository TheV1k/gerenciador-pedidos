package br.com.alura.exercicios.gerenciador_pedidos;

import br.com.alura.exercicios.gerenciador_pedidos.models.Categoria;
import br.com.alura.exercicios.gerenciador_pedidos.models.Pedido;
import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import br.com.alura.exercicios.gerenciador_pedidos.repository.CategoriaRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.PedidoRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class GerenciadorPedidosApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(GerenciadorPedidosApplication.class, args);
	}

	@Autowired
	private PedidoRepository repositorioPedido;
	@Autowired
	private CategoriaRepository repositorioCategoria;
	@Autowired
	private ProdutoRepository repositorioProduto;

	@Override
	public void run(String... args) throws Exception {

		Produto produto1 = new Produto("Feijão", 10.95);
		repositorioProduto.save(produto1);
		Pedido pedido = new Pedido(LocalDate.parse("2026-05-08"));
		repositorioPedido.save(pedido);
		Categoria categoria = new Categoria("Grãos");
		repositorioCategoria.save(categoria);
		Produto produto2 = new Produto("Copa Lombo", 23.80);
		repositorioProduto.save(produto2);
		Pedido pedido2 = new Pedido(LocalDate.parse("2026-05-02"));
		repositorioPedido.save(pedido2);
		Categoria categoria2 = new Categoria("Açougue");
		repositorioCategoria.save(categoria2);



	}
}
