package cad.project.service;

import cad.project.playload.DashboardStatsDTO;
import cad.project.playload.MonthlyStatDTO;
import cad.project.playload.TopProduitDTO;
import cad.project.repositries.ClientRepositry;
import cad.project.repositries.DevisRepositry;
import cad.project.repositries.Mouvement_StockRepositry;
import cad.project.repositries.ProduitRepositry;
import cad.project.repositries.SaleOrderRepositry;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatistiqueServiceImp implements StatistiqueService {

    @Autowired
    private SaleOrderRepositry saleOrderRepositry;

    @Autowired
    private DevisRepositry devisRepositry;

    @Autowired
    private ProduitRepositry produitRepositry;

    @Autowired
    private Mouvement_StockRepositry mouvementStockRepositry;

    @Autowired
    private ClientRepositry clientRepositry;

    private static final int TOP_PRODUITS_LIMIT = 5;

    private static final String[] MOIS_NOMS = {
            "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
            "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
    };

    @Override
    public DashboardStatsDTO getDashboardStats(int annee) {
        DashboardStatsDTO dto = new DashboardStatsDTO();
        dto.setAnnee(annee);

        // Ventes / CA
        double caTotal = saleOrderRepositry.sumCaByYear(annee);
        long nombreVentesTotal = saleOrderRepositry.countVentesByYear(annee);
        dto.setCaTotal(caTotal);
        dto.setNombreVentesTotal(nombreVentesTotal);
        dto.setMontantResteCumule(saleOrderRepositry.sumMontantResteByYear(annee));
        dto.setPanierMoyen(nombreVentesTotal > 0 ? caTotal / nombreVentesTotal : 0);

        // Devis
        long nombreDevisTotal = devisRepositry.countDevisByYear(annee);
        long nombreDevisValide = devisRepositry.countDevisValideByYear(annee);
        dto.setNombreDevisTotal(nombreDevisTotal);
        dto.setTauxConversionGlobal(nombreDevisTotal > 0 ? (nombreDevisValide * 100.0) / nombreDevisTotal : 0);
        dto.setNombreDevisEnAttente(devisRepositry.countDevisEnAttente());

        // Stock
        dto.setProduitsStockFaible(produitRepositry.countStockFaible());
        dto.setValeurTotaleStock(produitRepositry.valeurTotaleStock());
        LocalDateTime debutJour = LocalDate.now().atStartOfDay();
        LocalDateTime finJour = debutJour.plusDays(1);
        dto.setMouvementsStockDuJour(mouvementStockRepositry.countMouvementsEntre(debutJour, finJour));
        dto.setTopProduits(getTopProduits(annee));

        // Clients
        dto.setNouveauxClients(clientRepositry.countNouveauxClientsByYear(annee));
        dto.setClientsActifs(clientRepositry.count());

        // Détail par mois
        dto.setStatsParMois(buildStatsParMois(annee));

        return dto;
    }

    private List<TopProduitDTO> getTopProduits(int annee) {
        List<Object[]> rows = mouvementStockRepositry.topProduitsVendus(annee, PageRequest.of(0, TOP_PRODUITS_LIMIT));
        List<TopProduitDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            Long produitId = (Long) row[0];
            String nom = (String) row[1];
            long quantiteVendue = ((Number) row[2]).longValue();
            result.add(new TopProduitDTO(produitId, nom, quantiteVendue));
        }
        return result;
    }

    private List<MonthlyStatDTO> buildStatsParMois(int annee) {
        Map<Integer, MonthlyStatDTO> parMois = new HashMap<>();
        for (int mois = 1; mois <= 12; mois++) {
            parMois.put(mois, new MonthlyStatDTO(mois, 0, 0, 0, 0));
        }

        for (Object[] row : saleOrderRepositry.statsVentesParMois(annee)) {
            int mois = ((Number) row[0]).intValue();
            long nombreVentes = ((Number) row[1]).longValue();
            double ca = ((Number) row[2]).doubleValue();
            MonthlyStatDTO m = parMois.get(mois);
            m.setNombreVentes(nombreVentes);
            m.setCa(ca);
        }

        for (Object[] row : devisRepositry.statsDevisParMois(annee)) {
            int mois = ((Number) row[0]).intValue();
            long nombreDevis = ((Number) row[1]).longValue();
            long nombreDevisValide = row[2] == null ? 0 : ((Number) row[2]).longValue();
            MonthlyStatDTO m = parMois.get(mois);
            m.setNombreDevis(nombreDevis);
            m.setTauxConversion(nombreDevis > 0 ? (nombreDevisValide * 100.0) / nombreDevis : 0);
        }

        List<MonthlyStatDTO> result = new ArrayList<>(parMois.values());
        result.sort((a, b) -> Integer.compare(a.getMois(), b.getMois()));
        return result;
    }

    @Override
    public byte[] genererRapportPdf(int annee) {
        DashboardStatsDTO stats = getDashboardStats(annee);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Color slate900 = new Color(15, 23, 42);
        Color slate100 = new Color(241, 245, 249);
        Color sky = new Color(14, 165, 233);
        Color skyBg = new Color(240, 249, 255);
        Color emerald = new Color(16, 185, 129);
        Color emeraldBg = new Color(236, 253, 245);
        Color violet = new Color(139, 92, 246);
        Color violetBg = new Color(245, 243, 255);
        Color amber = new Color(245, 158, 11);
        Color amberBg = new Color(255, 251, 235);

        try {
            Document document = new Document(PageSize.A4, 36, 36, 40, 40);
            PdfWriter.getInstance(document, out);
            document.open();

            // Bandeau titre
            PdfPTable headerBand = new PdfPTable(1);
            headerBand.setWidthPercentage(100);
            PdfPCell headerCell = new PdfPCell();
            headerCell.setBackgroundColor(slate900);
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setPadding(18);
            headerCell.addElement(new Paragraph("Rapport annuel — " + annee,
                    new Font(Font.HELVETICA, 22, Font.BOLD, Color.WHITE)));
            Paragraph subtitle = new Paragraph("Vue d'ensemble de l'activité, des ventes et du stock",
                    new Font(Font.HELVETICA, 11, Font.NORMAL, new Color(203, 213, 225)));
            subtitle.setSpacingBefore(4);
            headerCell.addElement(subtitle);
            headerBand.addCell(headerCell);
            headerBand.setSpacingAfter(20);
            document.add(headerBand);

            // Cartes KPI
            document.add(sectionTitle("Vue d'ensemble", slate900));

            PdfPTable kpiGrid = new PdfPTable(2);
            kpiGrid.setWidthPercentage(100);
            kpiGrid.setSpacingBefore(8);
            kpiGrid.setSpacingAfter(20);
            kpiGrid.setWidths(new float[]{1, 1});

            kpiGrid.addCell(kpiCard("Chiffre d'affaires", formatMoney(stats.getCaTotal()), skyBg, sky));
            kpiGrid.addCell(kpiCard("Montant restant à encaisser", formatMoney(stats.getMontantResteCumule()), skyBg, sky));
            kpiGrid.addCell(kpiCard("Ventes confirmées", String.valueOf(stats.getNombreVentesTotal()), emeraldBg, emerald));
            kpiGrid.addCell(kpiCard("Panier moyen", formatMoney(stats.getPanierMoyen()), emeraldBg, emerald));
            kpiGrid.addCell(kpiCard("Devis créés", String.valueOf(stats.getNombreDevisTotal()), violetBg, violet));
            kpiGrid.addCell(kpiCard("Taux de conversion", String.format("%.1f%%", stats.getTauxConversionGlobal()), violetBg, violet));
            kpiGrid.addCell(kpiCard("Devis en attente", String.valueOf(stats.getNombreDevisEnAttente()), violetBg, violet));
            kpiGrid.addCell(kpiCard("Produits en stock faible", String.valueOf(stats.getProduitsStockFaible()), amberBg, amber));
            kpiGrid.addCell(kpiCard("Valeur totale du stock", formatMoney(stats.getValeurTotaleStock()), amberBg, amber));
            kpiGrid.addCell(kpiCard("Mouvements de stock (jour)", String.valueOf(stats.getMouvementsStockDuJour()), amberBg, amber));
            kpiGrid.addCell(kpiCard("Nouveaux clients", String.valueOf(stats.getNouveauxClients()), slate100, slate900));
            kpiGrid.addCell(kpiCard("Clients au total", String.valueOf(stats.getClientsActifs()), slate100, slate900));
            document.add(kpiGrid);

            // Détail par mois
            document.add(sectionTitle("Détail par mois", slate900));

            PdfPTable monthTable = new PdfPTable(5);
            monthTable.setWidthPercentage(100);
            monthTable.setSpacingBefore(8);
            monthTable.setSpacingAfter(20);
            monthTable.setWidths(new float[]{1.2f, 1.3f, 1f, 1f, 1.2f});
            for (String h : new String[]{"Mois", "CA", "Ventes", "Devis", "Conversion"}) {
                monthTable.addCell(headerCellPdf(h, slate900));
            }
            boolean alt = false;
            for (MonthlyStatDTO m : stats.getStatsParMois()) {
                Color rowColor = alt ? slate100 : Color.WHITE;
                monthTable.addCell(bodyCell(MOIS_NOMS[m.getMois() - 1], rowColor, false));
                monthTable.addCell(bodyCell(formatMoney(m.getCa()), rowColor, false));
                monthTable.addCell(bodyCell(String.valueOf(m.getNombreVentes()), rowColor, false));
                monthTable.addCell(bodyCell(String.valueOf(m.getNombreDevis()), rowColor, false));
                monthTable.addCell(bodyCell(String.format("%.1f%%", m.getTauxConversion()), rowColor, false));
                alt = !alt;
            }
            document.add(monthTable);

            // Top produits
            document.add(sectionTitle("Top produits vendus", slate900));

            PdfPTable topTable = new PdfPTable(2);
            topTable.setWidthPercentage(100);
            topTable.setSpacingBefore(8);
            topTable.setWidths(new float[]{3f, 1f});
            topTable.addCell(headerCellPdf("Produit", slate900));
            topTable.addCell(headerCellPdf("Quantité vendue", slate900));
            alt = false;
            for (TopProduitDTO p : stats.getTopProduits()) {
                Color rowColor = alt ? violetBg : Color.WHITE;
                topTable.addCell(bodyCell(p.getNom(), rowColor, false));
                topTable.addCell(bodyCell(String.valueOf(p.getQuantiteVendue()), rowColor, true));
                alt = !alt;
            }
            document.add(topTable);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du rapport PDF", e);
        }

        return out.toByteArray();
    }

    private Paragraph sectionTitle(String text, Color color) {
        Paragraph p = new Paragraph(text, new Font(Font.HELVETICA, 14, Font.BOLD, color));
        p.setSpacingBefore(6);
        return p;
    }

    private PdfPCell kpiCard(String label, String value, Color bg, Color valueColor) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(bg);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(12);
        cell.addElement(new Paragraph(label.toUpperCase(), new Font(Font.HELVETICA, 9, Font.BOLD, new Color(100, 116, 139))));
        Paragraph valueP = new Paragraph(value, new Font(Font.HELVETICA, 16, Font.BOLD, valueColor));
        valueP.setSpacingBefore(4);
        cell.addElement(valueP);
        return cell;
    }

    private PdfPCell headerCellPdf(String text, Color bg) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE)));
        cell.setBackgroundColor(bg);
        cell.setPadding(7);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private PdfPCell bodyCell(String text, Color bg, boolean bold) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.HELVETICA, 10, bold ? Font.BOLD : Font.NORMAL, new Color(30, 41, 59))));
        cell.setBackgroundColor(bg);
        cell.setPadding(7);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColor(new Color(226, 232, 240));
        return cell;
    }

    private String formatMoney(double value) {
        return String.format("%,.2f MAD", value);
    }
}