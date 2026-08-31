package cad.project.service;

import cad.project.playload.DashboardStatsDTO;
import cad.project.repositries.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatistiqueServiceTest {

    @Mock
    private SaleOrderRepositry saleOrderRepositry;
    @Mock
    private DevisRepositry devisRepositry;
    @Mock
    private ProduitRepositry produitRepositry;
    @Mock
    private Mouvement_StockRepositry mouvementStockRepositry;
    @Mock
    private ClientRepositry clientRepositry;

    @InjectMocks
    private StatistiqueServiceImp statistiqueServiceImp;

    @Test
    @DisplayName("Doit calculer le panier moyen quand il y a des ventes")
    void test_getDashboardStats_panierMoyen_nominal() {
        int annee = 2026;

        when(saleOrderRepositry.sumCaByYear(annee)).thenReturn(10000.0);
        when(saleOrderRepositry.countVentesByYear(annee)).thenReturn(5L);
        when(saleOrderRepositry.sumMontantResteByYear(annee)).thenReturn(0.0);
        when(devisRepositry.countDevisByYear(annee)).thenReturn(0L);
        when(devisRepositry.countDevisEnAttente()).thenReturn(0L);
        when(produitRepositry.countStockFaible()).thenReturn(0L);
        when(produitRepositry.valeurTotaleStock()).thenReturn(0.0);
        when(mouvementStockRepositry.countMouvementsEntre(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(0L);
        when(mouvementStockRepositry.topProduitsVendus(anyInt(), any(PageRequest.class))).thenReturn(Collections.emptyList());
        when(clientRepositry.countNouveauxClientsByYear(annee)).thenReturn(0L);
        when(clientRepositry.count()).thenReturn(0L);
        when(saleOrderRepositry.statsVentesParMois(annee)).thenReturn(Collections.emptyList());
        when(devisRepositry.statsDevisParMois(annee)).thenReturn(Collections.emptyList());

        DashboardStatsDTO dto = statistiqueServiceImp.getDashboardStats(annee);

        assertEquals(2000.0, dto.getPanierMoyen());
    }

    @Test
    @DisplayName("Doit retourner un panier moyen de zéro quand il n'y a aucune vente")
    void test_getDashboardStats_panierMoyen_division_par_zero() {
        int annee = 2026;

        when(saleOrderRepositry.sumCaByYear(annee)).thenReturn(0.0);
        when(saleOrderRepositry.countVentesByYear(annee)).thenReturn(0L);
        when(saleOrderRepositry.sumMontantResteByYear(annee)).thenReturn(0.0);
        when(devisRepositry.countDevisByYear(annee)).thenReturn(0L);
        when(devisRepositry.countDevisEnAttente()).thenReturn(0L);
        when(produitRepositry.countStockFaible()).thenReturn(0L);
        when(produitRepositry.valeurTotaleStock()).thenReturn(0.0);
        when(mouvementStockRepositry.countMouvementsEntre(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(0L);
        when(mouvementStockRepositry.topProduitsVendus(anyInt(), any(PageRequest.class))).thenReturn(Collections.emptyList());
        when(clientRepositry.countNouveauxClientsByYear(annee)).thenReturn(0L);
        when(clientRepositry.count()).thenReturn(0L);
        when(saleOrderRepositry.statsVentesParMois(annee)).thenReturn(Collections.emptyList());
        when(devisRepositry.statsDevisParMois(annee)).thenReturn(Collections.emptyList());

        DashboardStatsDTO dto = statistiqueServiceImp.getDashboardStats(annee);

        assertEquals(0.0, dto.getPanierMoyen());
    }

    @Test
    @DisplayName("Doit calculer le taux de conversion des devis quand il y a des devis")
    void test_getDashboardStats_tauxConversion_nominal() {
        int annee = 2026;

        when(saleOrderRepositry.sumCaByYear(annee)).thenReturn(0.0);
        when(saleOrderRepositry.countVentesByYear(annee)).thenReturn(0L);
        when(saleOrderRepositry.sumMontantResteByYear(annee)).thenReturn(0.0);
        when(devisRepositry.countDevisByYear(annee)).thenReturn(10L);
        when(devisRepositry.countDevisValideByYear(annee)).thenReturn(4L);
        when(devisRepositry.countDevisEnAttente()).thenReturn(0L);
        when(produitRepositry.countStockFaible()).thenReturn(0L);
        when(produitRepositry.valeurTotaleStock()).thenReturn(0.0);
        when(mouvementStockRepositry.countMouvementsEntre(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(0L);
        when(mouvementStockRepositry.topProduitsVendus(anyInt(), any(PageRequest.class))).thenReturn(Collections.emptyList());
        when(clientRepositry.countNouveauxClientsByYear(annee)).thenReturn(0L);
        when(clientRepositry.count()).thenReturn(0L);
        when(saleOrderRepositry.statsVentesParMois(annee)).thenReturn(Collections.emptyList());
        when(devisRepositry.statsDevisParMois(annee)).thenReturn(Collections.emptyList());

        DashboardStatsDTO dto = statistiqueServiceImp.getDashboardStats(annee);

        assertEquals(40.0, dto.getTauxConversionGlobal());
    }

    @Test
    @DisplayName("Doit retourner un taux de conversion de zéro quand il n'y a aucun devis")
    void test_getDashboardStats_tauxConversion_division_par_zero() {
        int annee = 2026;

        when(saleOrderRepositry.sumCaByYear(annee)).thenReturn(0.0);
        when(saleOrderRepositry.countVentesByYear(annee)).thenReturn(0L);
        when(saleOrderRepositry.sumMontantResteByYear(annee)).thenReturn(0.0);
        when(devisRepositry.countDevisByYear(annee)).thenReturn(0L);
        when(devisRepositry.countDevisEnAttente()).thenReturn(0L);
        when(produitRepositry.countStockFaible()).thenReturn(0L);
        when(produitRepositry.valeurTotaleStock()).thenReturn(0.0);
        when(mouvementStockRepositry.countMouvementsEntre(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(0L);
        when(mouvementStockRepositry.topProduitsVendus(anyInt(), any(PageRequest.class))).thenReturn(Collections.emptyList());
        when(clientRepositry.countNouveauxClientsByYear(annee)).thenReturn(0L);
        when(clientRepositry.count()).thenReturn(0L);
        when(saleOrderRepositry.statsVentesParMois(annee)).thenReturn(Collections.emptyList());
        when(devisRepositry.statsDevisParMois(annee)).thenReturn(Collections.emptyList());

        DashboardStatsDTO dto = statistiqueServiceImp.getDashboardStats(annee);

        assertEquals(0.0, dto.getTauxConversionGlobal());
    }

    @Test
    @DisplayName("Doit calculer le CA du mois à partir des lignes de statsVentesParMois")
    void test_getDashboardStats_caParMois_nominal() {
        int annee = 2026;

        when(saleOrderRepositry.sumCaByYear(annee)).thenReturn(0.0);
        when(saleOrderRepositry.countVentesByYear(annee)).thenReturn(0L);
        when(saleOrderRepositry.sumMontantResteByYear(annee)).thenReturn(0.0);
        when(devisRepositry.countDevisByYear(annee)).thenReturn(0L);
        when(devisRepositry.countDevisEnAttente()).thenReturn(0L);
        when(produitRepositry.countStockFaible()).thenReturn(0L);
        when(produitRepositry.valeurTotaleStock()).thenReturn(0.0);
        when(mouvementStockRepositry.countMouvementsEntre(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(0L);
        when(mouvementStockRepositry.topProduitsVendus(anyInt(), any(PageRequest.class))).thenReturn(Collections.emptyList());
        when(clientRepositry.countNouveauxClientsByYear(annee)).thenReturn(0L);
        when(clientRepositry.count()).thenReturn(0L);
        Object[] ligneMars = new Object[]{3, 7L, 1500.0};
        when(saleOrderRepositry.statsVentesParMois(annee)).thenReturn(Collections.singletonList(ligneMars));
        when(devisRepositry.statsDevisParMois(annee)).thenReturn(Collections.emptyList());

        DashboardStatsDTO dto = statistiqueServiceImp.getDashboardStats(annee);

        assertEquals(1500.0, dto.getStatsParMois().get(2).getCa());
        assertEquals(7L, dto.getStatsParMois().get(2).getNombreVentes());
    }
}