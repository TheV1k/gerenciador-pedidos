package br.com.alura.exercicios.gerenciador_pedidos.validacoes;

import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.DuplicateResourceException;
import br.com.alura.exercicios.gerenciador_pedidos.Exceptions.InvalidDataException;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor.FornecedorRequestDTO;
import br.com.alura.exercicios.gerenciador_pedidos.repository.FornecedorRepository;
import org.springframework.stereotype.Component;

@Component
public class FornecedorValidator {

    private final FornecedorRepository repository;

    public FornecedorValidator(FornecedorRepository repository) {
        this.repository = repository;
    }


    public void validarFornecedor(FornecedorRequestDTO dto){

        validarDadosObrigatorios(dto);
        validarFornecedorNovo(dto);

    }


    public void validarAtualizacao(Long id, FornecedorRequestDTO dto){

        validarDadosObrigatorios(dto);
        validarFornecedorAtualizacao(id, dto);

    }


    private void validarDadosObrigatorios(FornecedorRequestDTO dto) {

        if(dto.nome() == null || dto.nome().isBlank()){
            throw new InvalidDataException("Fornecedor não pode ficar em branco!");
        }


        if(dto.cnpj() == null || dto.cnpj().isBlank()){
            throw new InvalidDataException("CNPJ não pode ficar em branco!");
        }


        if(!dto.cnpj().matches("\\d{14}")){
            throw new InvalidDataException("CNPJ inválido!");
        }


        if(dto.email() == null || dto.email().isBlank()){
            throw new InvalidDataException("E-mail não pode ficar em branco");
        }


        if(!dto.email()
                .matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){

            throw new InvalidDataException("E-mail inválido!");
        }


        if(dto.endereco() == null || dto.endereco().isBlank()){
            throw new InvalidDataException("Endereço não pode ficar em branco!");
        }

    }



    private void validarFornecedorNovo(FornecedorRequestDTO dto) {


       if(repository.existsByNomeIgnoreCase(dto.nome())){
            throw new DuplicateResourceException(
                    "Fornecedor já cadastrado!"
            );
        }


        if(repository.existsByCnpjIgnoreCase(dto.cnpj())){
            throw new DuplicateResourceException(
                    "CNPJ já cadastrado!"
            );
        }


        if(repository.existsByEmailIgnoreCase(dto.email())){
            throw new DuplicateResourceException(
                    "E-mail já cadastrado!"
            );
        }

    }



    private void validarFornecedorAtualizacao(Long id, FornecedorRequestDTO dto){


        if(repository.existsByNomeIgnoreCaseAndIdNot(dto.nome(), id)){
            throw new DuplicateResourceException(
                    "Fornecedor já cadastrado!"
            );
        }


        if(repository.existsByCnpjIgnoreCaseAndIdNot(dto.cnpj(), id)){
            throw new DuplicateResourceException(
                    "CNPJ já cadastrado!"
            );
        }


        if(repository.existsByEmailIgnoreCaseAndIdNot(dto.email(), id)){
            throw new DuplicateResourceException(
                    "E-mail já cadastrado!"
            );
        }

    }

}