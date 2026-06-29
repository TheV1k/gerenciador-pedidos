package br.com.alura.exercicios.gerenciador_pedidos.validacoes;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.DuplicateResourceException;
import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.InvalidDataException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor.FornecedorRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.repository.FornecedorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FornecedorValidatorTest {

    @InjectMocks
    private FornecedorValidator validator;

    @Mock
    private FornecedorRepository repository;

    @Test
    public void deveValidarFornecedorUnico(){

        FornecedorRequestDTO dto = new FornecedorRequestDTO("Fornecedor",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br");

        BDDMockito.given(repository.existsByNomeIgnoreCase(dto.nome())).willReturn(false);
        BDDMockito.given(repository.existsByCnpjIgnoreCase(dto.cnpj())).willReturn(false);
        BDDMockito.given(repository.existsByEmailIgnoreCase(dto.email())).willReturn(false);


        Assertions.assertDoesNotThrow(() -> validator.validarFornecedor(dto));
    }

    @Test
    public void deveLancarExcecaoDeFornecedorExistente(){

        FornecedorRequestDTO dto = new FornecedorRequestDTO("Fornecedor",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br");

        BDDMockito.given(repository.existsByNomeIgnoreCase(dto.nome())).willReturn(true);


        Assertions.assertThrows(DuplicateResourceException.class, () -> validator.validarFornecedor(dto));

    }

    @Test
    public void deveLancarExcecaoDeCNPJExistente(){

        FornecedorRequestDTO dto = new FornecedorRequestDTO("Fornecedor",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br");

        BDDMockito.given(repository.existsByCnpjIgnoreCase(dto.cnpj())).willReturn(true);

        Assertions.assertThrows(DuplicateResourceException.class, () -> validator.validarFornecedor(dto));

    }

    @Test
    public void deveLancarExcecaoDeEmailExistente(){

        FornecedorRequestDTO dto = new FornecedorRequestDTO("Fornecedor",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br");

        BDDMockito.given(repository.existsByEmailIgnoreCase(dto.email())).willReturn(true);

        Assertions.assertThrows(DuplicateResourceException.class, () -> validator.validarFornecedor(dto));

    }

    @Test
    public void deveLancarExcecaoDeFornecedorNulo(){

        FornecedorRequestDTO dto = new FornecedorRequestDTO(null,
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br");


        Assertions.assertThrows(InvalidDataException.class, () -> validator.validarFornecedor(dto));

    }

    @Test
    public void deveLancarExcecaoDeFornecedorVazio(){

        FornecedorRequestDTO dto = new FornecedorRequestDTO(" ",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br");


        Assertions.assertThrows(InvalidDataException.class, () -> validator.validarFornecedor(dto));

    }

    @Test
    public void deveLancarExcecaoDeCNPJNulo(){

        FornecedorRequestDTO dto = new FornecedorRequestDTO("Fornecedor",
                null,
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br");


        Assertions.assertThrows(InvalidDataException.class, () -> validator.validarFornecedor(dto));

    }

    @Test
    public void deveLancarExcecaoDeCNPJVazio(){

        FornecedorRequestDTO dto = new FornecedorRequestDTO("Fornecedor",
                "",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br");


        Assertions.assertThrows(InvalidDataException.class, () -> validator.validarFornecedor(dto));

    }

    @Test
    public void deveLancarExcecaoDeEnderecoNulo(){

        FornecedorRequestDTO dto = new FornecedorRequestDTO("Fornecedor",
                "00000000000100",
                null,
                "alfadistribuidora@alfa.com.br");


        Assertions.assertThrows(InvalidDataException.class, () -> validator.validarFornecedor(dto));

    }

    @Test
    public void deveLancarExcecaoDeEnderecoVazio(){

        FornecedorRequestDTO dto = new FornecedorRequestDTO("Fornecedor",
                "00000000000100",
                " ",
                "alfadistribuidora@alfa.com.br");


        Assertions.assertThrows(InvalidDataException.class, () -> validator.validarFornecedor(dto));

    }

    @Test
    public void deveLancarExcecaoDeEmailNulo(){

        FornecedorRequestDTO dto = new FornecedorRequestDTO("Fornecedor",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                null);


        Assertions.assertThrows(InvalidDataException.class, () -> validator.validarFornecedor(dto));

    }

    @Test
    public void deveLancarExcecaoDeEmailVazio(){

        FornecedorRequestDTO dto = new FornecedorRequestDTO("Fornecedor",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "");


        Assertions.assertThrows(InvalidDataException.class, () -> validator.validarFornecedor(dto));

    }

    @Test
    public void deveLancarExcecaoCNPJInvalido(){

        FornecedorRequestDTO dto = new FornecedorRequestDTO("Fornecedor",
                "00A00000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidora@alfa.com.br");


        Assertions.assertThrows(InvalidDataException.class, () -> validator.validarFornecedor(dto));
    }



    @Test
    public void deveLancarExcecaoEmailInvalido(){

        FornecedorRequestDTO dto = new FornecedorRequestDTO("Fornecedor",
                "00000000000100",
                "Rua Delta -520 - São Paulo",
                "alfadistribuidoraalfa.com.br");


        Assertions.assertThrows(InvalidDataException.class, () -> validator.validarFornecedor(dto));
    }

    @Test
    void deveLancarExcecaoCNPJComQuantidadeIncorreta(){

        FornecedorRequestDTO dto = new FornecedorRequestDTO(
                "Fornecedor",
                "123",
                "Rua Delta",
                "teste@email.com"
        );

        Assertions.assertThrows(
                InvalidDataException.class,
                () -> validator.validarFornecedor(dto)
        );
    }

}