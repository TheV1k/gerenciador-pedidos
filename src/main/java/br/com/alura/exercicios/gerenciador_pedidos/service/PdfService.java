package br.com.alura.exercicios.gerenciador_pedidos.service;

import br.com.alura.exercicios.gerenciador_pedidos.dto.Fornecedor.FornecedorResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.ItemPedidoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.PedidoResponseDTO;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] gerarPedidoPdf(PedidoResponseDTO pedido) {

        try {

            ByteArrayOutputStream baos =
                    new ByteArrayOutputStream();

            Document document = new Document();

            PdfWriter.getInstance(document, baos);

            document.open();

            adicionarCabecalho(document);

            adicionarDadosPedido(document, pedido);

            adicionarTabelaItens(document, pedido);

            adicionarRodape(document, pedido);

            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao gerar PDF",
                    e
            );
        }
    }

    private void adicionarCabecalho(Document document)
            throws DocumentException {

        Font titulo =
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD,
                        18
                );

        Paragraph cabecalho =
                new Paragraph(
                        "RELATÓRIO DE PEDIDO",
                        titulo
                );

        cabecalho.setAlignment(Element.ALIGN_CENTER);

        document.add(cabecalho);
        document.add(new Paragraph(" "));
    }

    private PdfPCell criarCelulaTitulo(String texto) {

        Font fonte = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                10
        );

        PdfPCell celula = new PdfPCell(
                new Phrase(texto, fonte)
        );

        return celula;
    }

    private void adicionarDadosPedido(
            Document document,
            PedidoResponseDTO pedido
    ) throws DocumentException {

        PdfPTable tabelaCabecalho = new PdfPTable(2);

        tabelaCabecalho.setWidthPercentage(100);
        tabelaCabecalho.setWidths(new float[]{2, 5});

        tabelaCabecalho.addCell(criarCelulaTitulo("Pedido Nº"));
        tabelaCabecalho.addCell(String.valueOf(pedido.id()));

        tabelaCabecalho.addCell(criarCelulaTitulo("Data da Solicitação"));
        tabelaCabecalho.addCell(pedido.dataPedido().toString());

        tabelaCabecalho.addCell(criarCelulaTitulo("Data da Entrega"));
        tabelaCabecalho.addCell(
                pedido.dataEntrega() != null
                        ? pedido.dataEntrega().toString()
                        : "Não informada"
        );

        tabelaCabecalho.addCell(criarCelulaTitulo("Fornecedor"));
        tabelaCabecalho.addCell(pedido.fornecedor().nome());

        tabelaCabecalho.addCell(criarCelulaTitulo("CNPJ"));
        tabelaCabecalho.addCell(pedido.fornecedor().cnpj());

        tabelaCabecalho.addCell(criarCelulaTitulo("Endereço"));
        tabelaCabecalho.addCell(pedido.fornecedor().endereco());

        tabelaCabecalho.addCell(criarCelulaTitulo("E-mail"));
        tabelaCabecalho.addCell(pedido.fornecedor().email());

        tabelaCabecalho.addCell(criarCelulaTitulo("Status"));
        tabelaCabecalho.addCell(pedido.status().toString());

        document.add(tabelaCabecalho);

        document.add(new Paragraph(" "));
    }

    private void adicionarTabelaItens(
            Document document,
            PedidoResponseDTO pedido
    ) throws DocumentException {

        PdfPTable tabela =
                new PdfPTable(4);

        tabela.setWidthPercentage(100);

        tabela.addCell(criarCelulaTitulo("Produto"));
        tabela.addCell(criarCelulaTitulo("Quantidade"));
        tabela.addCell(criarCelulaTitulo("Valor Unitário"));
        tabela.addCell(criarCelulaTitulo("Subtotal"));

        for (ItemPedidoResponseDTO item :
                pedido.itens()) {

            tabela.addCell(item.produto());

            tabela.addCell(
                    String.valueOf(
                            item.quantidade()
                    )
            );

            tabela.addCell(
                    "R$ " +
                            item.precoUnitario()
            );

            tabela.addCell(
                    "R$ " +
                            item.subtotal()
            );
        }

        document.add(tabela);
    }

    private void adicionarRodape(
            Document document,
            PedidoResponseDTO pedido
    ) throws DocumentException {

        document.add(new Paragraph(" "));

        Font totalFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD,
                        14
                );

        Paragraph total =
                new Paragraph(
                        "TOTAL: R$ "
                                + pedido.totalPedido(),
                        totalFont
                );

        total.setAlignment(
                Element.ALIGN_RIGHT
        );

        document.add(total);
    }
}
