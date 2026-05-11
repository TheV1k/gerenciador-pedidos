package br.com.alura.exercicios.gerenciador_pedidos;

import br.com.alura.exercicios.gerenciador_pedidos.Principal.Principal;
import br.com.alura.exercicios.gerenciador_pedidos.models.Categoria;
import br.com.alura.exercicios.gerenciador_pedidos.models.Fornecedor;
import br.com.alura.exercicios.gerenciador_pedidos.models.Pedido;
import br.com.alura.exercicios.gerenciador_pedidos.models.Produto;
import br.com.alura.exercicios.gerenciador_pedidos.repository.CategoriaRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.FornecedorRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.PedidoRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Scanner;

import static java.awt.SystemColor.menu;




@SpringBootApplication
public class GerenciadorPedidosApplication implements CommandLineRunner {

	@Autowired
	private PedidoRepository repositorioPedido;
	@Autowired
	private CategoriaRepository repositorioCategoria;
	@Autowired
	private ProdutoRepository repositorioProduto;
	@Autowired
	private FornecedorRepository repositorioFornecedor;

	public static void main(String[] args) {
		SpringApplication.run(GerenciadorPedidosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	Principal principal = new Principal(repositorioProduto, repositorioCategoria);
    principal.exibeMenu();


		}




	}


