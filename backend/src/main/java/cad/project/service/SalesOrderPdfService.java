package cad.project.service;

import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.SalesOrder;
import cad.project.model.SalesOrderItems;
import cad.project.repositries.SaleOrderRepositry;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class SalesOrderPdfService {

    @Autowired
    private SaleOrderRepositry saleOrderRepositry;

    private static final Color SLATE_900 = new Color(15, 23, 42);
    private static final Color BLUE_600 = new Color(37, 99, 235);
    private static final Color SLATE_50 = new Color(248, 250, 252);
    private static final Color SLATE_500 = new Color(100, 116, 139);
    private static final Color SLATE_200 = new Color(226, 232, 240);
    private static final Color WHITE = Color.WHITE;

    public byte[] generateSalesOrderPdf(Long salesOrderId) {
        SalesOrder salesOrder = saleOrderRepositry.findById(salesOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("SalesOrder", "salesOrderId", salesOrderId));

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 0, 0, 0, 40);
            PdfWriter.getInstance(document, out);
            document.open();

            Font brandFont = new Font(Font.HELVETICA, 22, Font.BOLD, WHITE);
            Font brandSubFont = new Font(Font.HELVETICA, 9, Font.NORMAL, new Color(203, 213, 225));
            Font h1Font = new Font(Font.HELVETICA, 16, Font.BOLD, SLATE_900);
            Font labelFont = new Font(Font.HELVETICA, 8, Font.BOLD, SLATE_500);
            Font valueFont = new Font(Font.HELVETICA, 11, Font.NORMAL, SLATE_900);
            Font valueBoldFont = new Font(Font.HELVETICA, 11, Font.BOLD, SLATE_900);
            Font tableHeaderFont = new Font(Font.HELVETICA, 9, Font.BOLD, WHITE);
            Font tableCellFont = new Font(Font.HELVETICA, 10, Font.NORMAL, SLATE_900);
            Font totalLabelFont = new Font(Font.HELVETICA, 11, Font.NORMAL, WHITE);
            Font totalValueFont = new Font(Font.HELVETICA, 20, Font.BOLD, WHITE);
            Font statusFont = new Font(Font.HELVETICA, 9, Font.BOLD, WHITE);

            // ---- Bandeau haut (slate-900) ----
            PdfPTable headerBand = new PdfPTable(2);
            headerBand.setWidthPercentage(100);
            headerBand.setWidths(new float[]{3, 2});

            PdfPCell brandCell = new PdfPCell();
            brandCell.setBackgroundColor(SLATE_900);
            brandCell.setBorder(Rectangle.NO_BORDER);
            brandCell.setPadding(24);
            Paragraph brand = new Paragraph("Cad-Optique", brandFont);
            brand.add(new Chunk("\n"));
            brand.add(new Chunk("Gestion optique  |  ICE: ---  |  IF: ---", brandSubFont));
            brandCell.addElement(brand);
            headerBand.addCell(brandCell);

            PdfPCell statusCell = new PdfPCell();
            statusCell.setBackgroundColor(SLATE_900);
            statusCell.setBorder(Rectangle.NO_BORDER);
            statusCell.setPadding(24);
            statusCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            statusCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            String statusLabel = salesOrder.getStatus() != null ? salesOrder.getStatus() : "en cours";
            Color statusColor = "Valide".equals(statusLabel) ? new Color(22, 163, 74)
                    : "Annulee".equals(statusLabel) ? new Color(220, 38, 38)
                    : BLUE_600;

            PdfPTable badge = new PdfPTable(1);
            badge.setHorizontalAlignment(Element.ALIGN_RIGHT);
            PdfPCell badgeCell = new PdfPCell(new Phrase(statusLabel.toUpperCase(), statusFont));
            badgeCell.setBackgroundColor(statusColor);
            badgeCell.setBorder(Rectangle.NO_BORDER);
            badgeCell.setPadding(6);
            badgeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            badge.addCell(badgeCell);
            statusCell.addElement(badge);
            headerBand.addCell(statusCell);

            document.add(headerBand);

            // ---- Corps ----
            PdfPTable body = new PdfPTable(1);
            body.setWidthPercentage(100);
            PdfPCell bodyPad = new PdfPCell();
            bodyPad.setBorder(Rectangle.NO_BORDER);
            bodyPad.setPaddingLeft(40);
            bodyPad.setPaddingRight(40);
            bodyPad.setPaddingTop(20);

            bodyPad.addElement(new Paragraph("RECU N° " + salesOrder.getId(), h1Font));
            bodyPad.addElement(new Paragraph(" "));

            // Bloc infos client / date
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(20);

            PdfPCell clientCell = new PdfPCell();
            clientCell.setBorder(Rectangle.NO_BORDER);
            clientCell.addElement(new Paragraph("CLIENT", labelFont));
            clientCell.addElement(new Paragraph(salesOrder.getClient().getNom() + " " + salesOrder.getClient().getPrenom(), valueBoldFont));
            if (salesOrder.getClient().getPhoneNumber() != null) {
                clientCell.addElement(new Paragraph(salesOrder.getClient().getPhoneNumber(), valueFont));
            }
            infoTable.addCell(clientCell);

            PdfPCell dateCell = new PdfPCell();
            dateCell.setBorder(Rectangle.NO_BORDER);
            dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            Paragraph dateLabel = new Paragraph("DATE", labelFont);
            dateLabel.setAlignment(Element.ALIGN_RIGHT);
            dateCell.addElement(dateLabel);
            Paragraph dateVal = new Paragraph(salesOrder.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), valueBoldFont);
            dateVal.setAlignment(Element.ALIGN_RIGHT);
            dateCell.addElement(dateVal);
            infoTable.addCell(dateCell);

            bodyPad.addElement(infoTable);

            // Table produits
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4, 1, 2, 2});
            table.setSpacingAfter(0);

            addHeaderCell(table, "PRODUIT", tableHeaderFont);
            addHeaderCell(table, "QTE", tableHeaderFont);
            addHeaderCell(table, "PRIX UNIT.", tableHeaderFont);
            addHeaderCell(table, "SOUS-TOTAL", tableHeaderFont);

            boolean alt = false;
            for (SalesOrderItems item : salesOrder.getOrderItemsList()) {
                double sousTotal = item.getQuantity() * item.getPrice();
                Color rowColor = alt ? SLATE_50 : WHITE;

                addBodyCell(table, item.getProduit().getNom(), tableCellFont, rowColor, Element.ALIGN_LEFT);
                addBodyCell(table, String.valueOf(item.getQuantity()), tableCellFont, rowColor, Element.ALIGN_CENTER);
                addBodyCell(table, String.format("%.2f DH", item.getPrice()), tableCellFont, rowColor, Element.ALIGN_RIGHT);
                addBodyCell(table, String.format("%.2f DH", sousTotal), tableCellFont, rowColor, Element.ALIGN_RIGHT);
                alt = !alt;
            }

            bodyPad.addElement(table);
            bodyPad.addElement(new Paragraph(" "));

            if (salesOrder.getDescription() != null && !salesOrder.getDescription().isBlank()) {
                bodyPad.addElement(new Paragraph("DESCRIPTION", labelFont));
                bodyPad.addElement(new Paragraph(salesOrder.getDescription(), valueFont));
            }

            body.addCell(bodyPad);
            document.add(body);

            // ---- Bandeau total (blue-600) ----
            PdfPTable totalBand = new PdfPTable(2);
            totalBand.setWidthPercentage(100);
            totalBand.setSpacingBefore(20);

            PdfPCell totalLabelCell = new PdfPCell(new Phrase("MONTANT TOTAL", totalLabelFont));
            totalLabelCell.setBackgroundColor(BLUE_600);
            totalLabelCell.setBorder(Rectangle.NO_BORDER);
            totalLabelCell.setPadding(18);
            totalLabelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            totalBand.addCell(totalLabelCell);

            PdfPCell totalValueCell = new PdfPCell(new Phrase(String.format("%.2f DH", salesOrder.getTotalprice()), totalValueFont));
            totalValueCell.setBackgroundColor(BLUE_600);
            totalValueCell.setBorder(Rectangle.NO_BORDER);
            totalValueCell.setPadding(18);
            totalValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            totalBand.addCell(totalValueCell);

            document.add(totalBand);

            // ---- Pied de page ----
            Paragraph footer = new Paragraph("Merci de votre confiance.", labelFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(30);
            document.add(footer);

            document.close();
            return out.toByteArray();
        } catch (DocumentException e) {
            throw new RuntimeException("Erreur lors de la generation du PDF", e);
        }
    }

    private void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(SLATE_900);
        cell.setPadding(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    private void addBodyCell(PdfPTable table, String text, Font font, Color bg, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bg);
        cell.setPadding(8);
        cell.setHorizontalAlignment(align);
        cell.setBorderColor(SLATE_200);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthBottom(1);
        table.addCell(cell);
    }
}