package br.com.alura.exercicios.gerenciador_pedidos;

import br.com.alura.exercicios.gerenciador_pedidos.Principal.Principal;
import br.com.alura.exercicios.gerenciador_pedidos.repository.CategoriaRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.FornecedorRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.PedidoRepository;
import br.com.alura.exercicios.gerenciador_pedidos.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


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
	Principal principal = new Principal(repositorioProduto, repositorioCategoria, repositorioFornecedor, repositorioPedido);
    principal.exibeMenu();


		}




	}


