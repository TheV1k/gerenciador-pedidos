package br.com.alura.exercicios.gerenciador_pedidos.service;

import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.ItemPedidoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.PedidoResponseDTO;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

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

    private void adicionarDadosPedido(
            Document document,
            PedidoResponseDTO pedido
    ) throws DocumentException {

        document.add(
                new Paragraph(
                        "Pedido Nº: " + pedido.id()
                )
        );



        document.add(
                new Paragraph(
                        "Data: " + pedido.dataPedido()
                )
        );

        document.add(
                new Paragraph(
                        "Fornecedor: " + pedido.fornecedor()
                )
        );

        document.add(
                new Paragraph(
                        "Status: " + pedido.status()
                )
        );

        document.add(new Paragraph(" "));
    }

    private void adicionarTabelaItens(
            Document document,
            PedidoResponseDTO pedido
    ) throws DocumentException {

        PdfPTable tabela =
                new PdfPTable(4);

        tabela.setWidthPercentage(100);

        tabela.addCell("Produto");
        tabela.addCell("Quantidade");
        tabela.addCell("Valor Unitário");
        tabela.addCell("Subtotal");

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
