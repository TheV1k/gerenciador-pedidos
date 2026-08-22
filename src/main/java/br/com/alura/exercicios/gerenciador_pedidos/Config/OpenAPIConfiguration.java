package br.com.alura.exercicios.gerenciador_pedidos.Config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("Victor Moreira Ramos - e-mail: ");
        myContact.setEmail("victormoreiramos@gmail.com");

        Info information = new Info()
                .title("Mini ERP")
                .version("1.0")
                .description(" API Restful criada para simular o funcionamento de um mini ERP\n." +
                        "Apresenta funções de cadastro de produtos, " +
                        "fornecedores, categorias e pedidos e gerencia buscas, realiza CRUD e gera ped com resumo de pedidos realizados.")
                .contact(myContact);

        return new OpenAPI()
                .info(information)
                .servers(List.of(server));
    }
}