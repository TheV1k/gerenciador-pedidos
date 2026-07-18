package br.com.alura.exercicios.gerenciador_pedidos.service;

import br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor.FornecedorResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.ItemPedidoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.PedidoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.models.Status;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PdfServiceTest {

    private PdfService pdfService;
    private PedidoResponseDTO pedidoValido;

    @BeforeEach
    void setUp() {
        pdfService = new PdfService();

        FornecedorResponseDTO fornecedor = new FornecedorResponseDTO(
                1L,
                "Fornecedor ACME Ltda",
                "12.345.678/0001-90",
                "Rua dos Testes, 123",
                "contato@acme.com"
        );

        ItemPedidoResponseDTO item1 = new ItemPedidoResponseDTO(
                "Notebook Gamer",
                2,
                new BigDecimal("5000.00"),
                new BigDecimal("10000.00")
        );

        ItemPedidoResponseDTO item2 = new ItemPedidoResponseDTO(
                "Mouse Sem Fio",
                5,
                new BigDecimal("150.50"),
                new BigDecimal("752.50")
        );

        pedidoValido = new PedidoResponseDTO(
                12345L,
                LocalDate.of(2026, 7, 15),
                LocalDate.of(2026, 7, 20),
                fornecedor,
                Status.CRIADO,
                new BigDecimal("10752.50"),
                List.of(item1, item2)
                );
    }

    @Test
    @DisplayName("Deve gerar o arquivo PDF com sucesso e retornar array de bytes populado")
    void deveGerarPdfComSucesso() {
        // Act
        byte[] pdfBytes = pdfService.gerarPedidoPdf(pedidoValido);

        // Assert
        assertNotNull(pdfBytes, "O PDF retornado não deveria ser nulo");
        assertTrue(pdfBytes.length > 0, "O PDF retornado não deveria estar vazio");
    }

    @Test
    @DisplayName("Deve conter os textos e dados corretos formatados dentro do PDF")
    void deveConterDadosCorretosNoPdf() throws IOException {
        // Act
        byte[] pdfBytes = pdfService.gerarPedidoPdf(pedidoValido);

        // Ler o conteúdo do PDF usando Apache PDFBox
        try (PDDocument document = Loader.loadPDF(pdfBytes)) {
            PDFTextStripper textStripper = new PDFTextStripper();
            String textoOriginal = textStripper.getText(document);

            // Remove espaços em branco normais e especiais para evitar quebras por formatação invisível
            String textoDoPdf = textoOriginal.replaceAll("\\s+", "").replace("\u00a0", "");

            // Assert - Título e Metadados do Pedido
            assertTrue(textoOriginal.contains("RELATÓRIO DE PEDIDO"));
            assertTrue(textoOriginal.contains("Pedido Nº 12345"));
            assertTrue(textoOriginal.contains("Fornecedor ACME Ltda"));
            assertTrue(textoOriginal.contains("12.345.678/0001-90"));

            // Assert - Tabela de Itens
            assertTrue(textoOriginal.contains("Notebook Gamer"));
            assertTrue(textoOriginal.contains("Mouse Sem Fio"));

            // Assert - Valores numéricos formatados no padrão PT-BR
            assertTrue(textoDoPdf.contains("5.000,00"));
            assertTrue(textoDoPdf.contains("150,50"));
            assertTrue(textoDoPdf.contains("10.752,50"));

            // Assert - Paginação Resiliente (Valida o fluxo e o preenchimento do template de encerramento)
            assertTrue(textoOriginal.contains("Página 1"));
            assertTrue(textoDoPdf.contains("1"));
        }
    }

    @Test
    @DisplayName("Deve gerar PDF corretamente mesmo quando a data de entrega for nula")
    void deveGerarPdfComDataEntregaNula() throws IOException {
        // Arrange
        PedidoResponseDTO pedidoSemEntrega = new PedidoResponseDTO(
                        12345L,
                        LocalDate.of(2026, 7, 15),
                null,
                pedidoValido.fornecedor(),
                Status.CRIADO,
                pedidoValido.totalPedido(),
                pedidoValido.itens()
                );
        // Act
        byte[] pdfBytes = pdfService.gerarPedidoPdf(pedidoSemEntrega);

        // Assert
        try (PDDocument document = Loader.loadPDF(pdfBytes)) {
            PDFTextStripper textStripper = new PDFTextStripper();
            String textoDoPdf = textStripper.getText(document);

            assertTrue(textoDoPdf.contains("Não informada"));
        }
    }

    @Test
    @DisplayName("Deve lançar RuntimeException caso ocorra algum erro crítico na geração")
    void deveLancarExceptionQuandoPedidoForNulo() {
        // Assert & Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pdfService.gerarPedidoPdf(null);
        });

        assertTrue(exception.getMessage().contains("Erro ao gerar PDF"));
    }
}