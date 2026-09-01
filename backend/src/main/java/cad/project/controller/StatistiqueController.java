package cad.project.controller;

import cad.project.playload.DashboardStatsDTO;
import cad.project.service.StatistiqueService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Year;

@RestController
@RequestMapping("/api")
public class StatistiqueController {

    @Autowired
    private StatistiqueService statistiqueService;

    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE')")
    @Operation(summary = "Tableau de bord : CA, ventes, devis, stock, clients pour une année (avec détail par mois)")
    @GetMapping("/admin/statistiques/{annee}")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats(@PathVariable int annee) {
        DashboardStatsDTO stats = statistiqueService.getDashboardStats(annee);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE')")
    @Operation(summary = "Tableau de bord pour l'année en cours")
    @GetMapping("/admin/statistiques")
    public ResponseEntity<DashboardStatsDTO> getDashboardStatsAnneeEnCours() {
        DashboardStatsDTO stats = statistiqueService.getDashboardStats(Year.now().getValue());
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE')")
    @Operation(summary = "Télécharger le rapport annuel en PDF")
    @GetMapping("/admin/statistiques/{annee}/rapport-pdf")
    public ResponseEntity<byte[]> telechargerRapportPdf(@PathVariable int annee) {
        byte[] pdf = statistiqueService.genererRapportPdf(annee);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "rapport-" + annee + ".pdf");
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}