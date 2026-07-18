package br.com.alura.exercicios.gerenciador_pedidos.service;

import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.ItemPedidoResponseDTO;
import br.com.alura.exercicios.gerenciador_pedidos.dto.Pedido.PedidoResponseDTO;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Service
public class PdfService {

    // Centraliza o Locale através das configurações locais da JVM da aplicação
    private final Locale localePadrao = new Locale("pt", "BR");

    private static final Color COR_PRIMARIA = Color.GRAY;
    private static final Color COR_TEXTO_TITULO = Color.WHITE;
    private static final Color COR_TEXTO_MUTED = Color.DARK_GRAY;

    public byte[] gerarPedidoPdf(PedidoResponseDTO pedido) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36, 36, 36, 54);
            PdfWriter writer = PdfWriter.getInstance(document, baos);

            NumeradorPaginaEvent evento = new NumeradorPaginaEvent();
            writer.setPageEvent(evento);

            document.open();
            adicionarCabecalho(document);
            adicionarDadosPedido(document, pedido);
            adicionarTabelaItens(document, pedido);
            adicionarRodape(document, pedido);
            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    private void adicionarCabecalho(Document document) throws DocumentException {
        Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, COR_TEXTO_MUTED);
        Paragraph cabecalho = new Paragraph("RELATÓRIO DE PEDIDO", tituloFont);
        cabecalho.setAlignment(Element.ALIGN_CENTER);

        document.add(cabecalho);
        document.add(new Paragraph(" "));
    }

    private PdfPCell criarCelulaTitulo(String texto, int alinhamento) {
        Font fonte = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, COR_TEXTO_TITULO);
        PdfPCell celula = new PdfPCell(new Phrase(texto, fonte));
        celula.setHorizontalAlignment(alinhamento);
        celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celula.setPadding(6);
        celula.setBackgroundColor(COR_PRIMARIA);
        return celula;
    }

    private PdfPCell criarCelulaDado(String texto, int alinhamento) {
        Font fonte = FontFactory.getFont(FontFactory.HELVETICA, 10);
        PdfPCell celula = new PdfPCell(new Phrase(texto, fonte));
        celula.setHorizontalAlignment(alinhamento);
        celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celula.setPadding(6);
        return celula;
    }

    private void adicionarDadosPedido(Document document, PedidoResponseDTO pedido) throws DocumentException {
        PdfPTable tabelaCabecalho = new PdfPTable(2);
        tabelaCabecalho.setWidthPercentage(100);
        tabelaCabecalho.setWidths(new float[]{2, 5});
        tabelaCabecalho.setKeepTogether(true);

        tabelaCabecalho.addCell(criarCelulaTitulo("Pedido Nº", Element.ALIGN_LEFT));
        tabelaCabecalho.addCell(criarCelulaDado(String.valueOf(pedido.id()), Element.ALIGN_LEFT));

        tabelaCabecalho.addCell(criarCelulaTitulo("Data da Solicitação", Element.ALIGN_LEFT));
        tabelaCabecalho.addCell(criarCelulaDado(pedido.dataPedido().toString(), Element.ALIGN_LEFT));

        tabelaCabecalho.addCell(criarCelulaTitulo("Data da Entrega", Element.ALIGN_LEFT));
        tabelaCabecalho.addCell(criarCelulaDado(
                pedido.dataEntrega() != null ? pedido.dataEntrega().toString() : "Não informada",
                Element.ALIGN_LEFT
        ));

        tabelaCabecalho.addCell(criarCelulaTitulo("Fornecedor", Element.ALIGN_LEFT));
        tabelaCabecalho.addCell(criarCelulaDado(pedido.fornecedor().nome(), Element.ALIGN_LEFT));

        tabelaCabecalho.addCell(criarCelulaTitulo("CNPJ", Element.ALIGN_LEFT));
        tabelaCabecalho.addCell(criarCelulaDado(pedido.fornecedor().cnpj(), Element.ALIGN_LEFT));

        tabelaCabecalho.addCell(criarCelulaTitulo("Endereço", Element.ALIGN_LEFT));
        tabelaCabecalho.addCell(criarCelulaDado(pedido.fornecedor().endereco(), Element.ALIGN_LEFT));

        tabelaCabecalho.addCell(criarCelulaTitulo("E-mail", Element.ALIGN_LEFT));
        tabelaCabecalho.addCell(criarCelulaDado(pedido.fornecedor().email(), Element.ALIGN_LEFT));

        tabelaCabecalho.addCell(criarCelulaTitulo("Status", Element.ALIGN_LEFT));
        tabelaCabecalho.addCell(criarCelulaDado(pedido.status().toString(), Element.ALIGN_LEFT));

        document.add(tabelaCabecalho);
        document.add(new Paragraph(" "));
    }

    private void adicionarTabelaItens(Document document, PedidoResponseDTO pedido) throws DocumentException {
        PdfPTable tabela = new PdfPTable(4);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[]{4, 2, 2, 2});

        tabela.addCell(criarCelulaTitulo("Produto", Element.ALIGN_LEFT));
        tabela.addCell(criarCelulaTitulo("Qtd", Element.ALIGN_CENTER));
        tabela.addCell(criarCelulaTitulo("Valor Unitário", Element.ALIGN_RIGHT));
        tabela.addCell(criarCelulaTitulo("Subtotal", Element.ALIGN_RIGHT));

        tabela.setHeaderRows(1);

        for (ItemPedidoResponseDTO item : pedido.itens()) {
            tabela.addCell(criarCelulaDado(item.produto(), Element.ALIGN_LEFT));
            tabela.addCell(criarCelulaDado(String.valueOf(item.quantidade()), Element.ALIGN_CENTER));
            tabela.addCell(criarCelulaDado(formatarMoeda(item.precoUnitario()), Element.ALIGN_RIGHT));
            tabela.addCell(criarCelulaDado(formatarMoeda(item.subtotal()), Element.ALIGN_RIGHT));
        }

        document.add(tabela);
    }

    private void adicionarRodape(Document document, PedidoResponseDTO pedido) throws DocumentException {
        document.add(new Paragraph(" "));

        Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, COR_TEXTO_MUTED);
        Paragraph total = new Paragraph("TOTAL: " + formatarMoeda(pedido.totalPedido()), totalFont);
        total.setAlignment(Element.ALIGN_RIGHT);

        document.add(total);
    }

    private String formatarMoeda(BigDecimal valor) {
        BigDecimal valorSeguro = (valor == null) ? BigDecimal.ZERO : valor;
        return NumberFormat.getCurrencyInstance(localePadrao).format(valorSeguro);
    }

    private static class NumeradorPaginaEvent extends PdfPageEventHelper {
        private PdfTemplate totalPaginasTemplate;
        private BaseFont fonteBase;
        private boolean documentoFechando = false;

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            totalPaginasTemplate = writer.getDirectContent().createTemplate(30, 16);
            try {
                fonteBase = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao inicializar fonte do rodapé", e);
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            if (documentoFechando) {
                return;
            }

            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();

            String textoPaginaAtual = String.format("Página %d de ", writer.getPageNumber());
            float larguraTexto = fonteBase.getWidthPoint(textoPaginaAtual, 9);

            float x = document.right() - larguraTexto - 20;
            float y = document.bottom() - 20;

            cb.beginText();
            cb.setFontAndSize(fonteBase, 9);
            cb.setColorFill(Color.GRAY);
            cb.setTextMatrix(x, y);
            cb.showText(textoPaginaAtual);
            cb.endText();

            cb.addTemplate(totalPaginasTemplate, document.right() - 20, y);
            cb.restoreState();
        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            documentoFechando = true;

            totalPaginasTemplate.beginText();
            totalPaginasTemplate.setFontAndSize(fonteBase, 9);
            totalPaginasTemplate.setColorFill(Color.GRAY);
            totalPaginasTemplate.showText(String.valueOf(writer.getPageNumber()));
            totalPaginasTemplate.endText();
        }
    }
}