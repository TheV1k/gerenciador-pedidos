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
import org.springframework.format.annotation.DateTimeFormat;

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

//		Produto produto1 = new Produto("Arroz Integral", 24.90);
//		repositorioProduto.save(produto1);
//
//		Produto produto2 = new Produto("Feijão Carioca", 8.50);
//		repositorioProduto.save(produto2);
//
//		Produto produto3 = new Produto("Macarrão Espaguete", 6.99);
//		repositorioProduto.save(produto3);
//
//		Produto produto4 = new Produto("Molho de Tomate", 4.75);
//		repositorioProduto.save(produto4);
//
//		Produto produto5 = new Produto("Óleo de Soja", 7.89);
//		repositorioProduto.save(produto5);
//
//		Produto produto6 = new Produto("Café Torrado", 18.40);
//		repositorioProduto.save(produto6);
//
//		Produto produto7 = new Produto("Leite Integral", 5.30);
//		repositorioProduto.save(produto7);
//
//		Produto produto8 = new Produto("Pão de Forma", 9.99);
//		repositorioProduto.save(produto8);
//
//		Produto produto9 = new Produto("Queijo Mussarela", 32.50);
//		repositorioProduto.save(produto9);
//
//		Produto produto10 = new Produto("Presunto Cozido", 27.90);
//		repositorioProduto.save(produto10);
//
//		Produto produto11 = new Produto("Refrigerante Cola", 11.49);
//		repositorioProduto.save(produto11);
//
//		Produto produto12 = new Produto("Suco de Laranja", 7.20);
//		repositorioProduto.save(produto12);
//
//		Produto produto13 = new Produto("Chocolate ao Leite", 6.80);
//		repositorioProduto.save(produto13);
//
//		Produto produto14 = new Produto("Biscoito Recheado", 4.20);
//		repositorioProduto.save(produto14);
//
//		Produto produto15 = new Produto("Sabonete", 2.99);
//		repositorioProduto.save(produto15);
//
//		Produto produto16 = new Produto("Shampoo", 15.90);
//		repositorioProduto.save(produto16);
//
//		Produto produto17 = new Produto("Condicionador", 17.50);
//		repositorioProduto.save(produto17);
//
//		Produto produto18 = new Produto("Detergente", 3.10);
//		repositorioProduto.save(produto18);
//
//		Produto produto19 = new Produto("Amaciante", 14.75);
//		repositorioProduto.save(produto19);
//
//		Produto produto20 = new Produto("Papel Higiênico", 22.90);
//		repositorioProduto.save(produto20);

//		Categoria categoria1 = new Categoria("Alimentos");
//		repositorioCategoria.save(categoria1);
//
//		Categoria categoria2 = new Categoria("Bebidas");
//		repositorioCategoria.save(categoria2);
//
//		Categoria categoria3 = new Categoria("Laticínios");
//		repositorioCategoria.save(categoria3);
//
//		Categoria categoria4 = new Categoria("Frios");
//		repositorioCategoria.save(categoria4);
//
//		Categoria categoria5 = new Categoria("Padaria");
//		repositorioCategoria.save(categoria5);
//
//		Categoria categoria6 = new Categoria("Doces");
//		repositorioCategoria.save(categoria6);
//
//		Categoria categoria7 = new Categoria("Higiene Pessoal");
//		repositorioCategoria.save(categoria7);
//
//		Categoria categoria8 = new Categoria("Limpeza");
//		repositorioCategoria.save(categoria8);
//
//		Categoria categoria9 = new Categoria("Mercearia");
//		repositorioCategoria.save(categoria9);
//
//		Categoria categoria10 = new Categoria("Utilidades Domésticas");
//		repositorioCategoria.save(categoria10);

		Pedido pedido1 = new Pedido(LocalDate.parse("2026-01-05"));
		repositorioPedido.save(pedido1);

		Pedido pedido2 = new Pedido(LocalDate.parse("2026-01-12"));
		repositorioPedido.save(pedido2);

		Pedido pedido3 = new Pedido(LocalDate.parse("2026-01-20"));
		repositorioPedido.save(pedido3);

		Pedido pedido4 = new Pedido(LocalDate.parse("2026-02-03"));
		repositorioPedido.save(pedido4);

		Pedido pedido5 = new Pedido(LocalDate.parse("2026-02-10"));
		repositorioPedido.save(pedido5);

		Pedido pedido6 = new Pedido(LocalDate.parse("2026-02-18"));
		repositorioPedido.save(pedido6);

		Pedido pedido7 = new Pedido(LocalDate.parse("2026-03-01"));
		repositorioPedido.save(pedido7);

		Pedido pedido8 = new Pedido(LocalDate.parse("2026-03-09"));
		repositorioPedido.save(pedido8);

		Pedido pedido9 = new Pedido(LocalDate.parse("2026-03-15"));
		repositorioPedido.save(pedido9);

		Pedido pedido10 = new Pedido(LocalDate.parse("2026-03-28"));
		repositorioPedido.save(pedido10);

		Pedido pedido11 = new Pedido(LocalDate.parse("2026-04-02"));
		repositorioPedido.save(pedido11);

		Pedido pedido12 = new Pedido(LocalDate.parse("2026-04-11"));
		repositorioPedido.save(pedido12);

		Pedido pedido13 = new Pedido(LocalDate.parse("2026-04-19"));
		repositorioPedido.save(pedido13);

		Pedido pedido14 = new Pedido(LocalDate.parse("2026-04-25"));
		repositorioPedido.save(pedido14);

		Pedido pedido15 = new Pedido(LocalDate.parse("2026-05-03"));
		repositorioPedido.save(pedido15);




	}
}
