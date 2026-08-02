package cad.project.playload;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DashboardStatsDTO {

    private int annee;

    private double caTotal;
    private long nombreVentesTotal;
    private double montantResteCumule;
    private double panierMoyen;

    private long nombreDevisTotal;
    private double tauxConversionGlobal;
    private long nombreDevisEnAttente;

    private long produitsStockFaible;
    private long mouvementsStockDuJour;
    private double valeurTotaleStock;
    private List<TopProduitDTO> topProduits;

    private long nouveauxClients;
    private long clientsActifs;

    private List<MonthlyStatDTO> statsParMois;
}