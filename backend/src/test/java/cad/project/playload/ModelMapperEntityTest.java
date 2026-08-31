package cad.project.playload;

import cad.project.model.*;
import cad.project.playload.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelMapperEntityTest {

    private final ModelMapper modelMapper = new ModelMapper();

    @Test
    @DisplayName("Doit mapper tous les champs de Client vers ClientDTO")
    void test_mapping_client_vers_dto() {
        Client client = new Client();
        client.setId(1L);
        client.setNom("Alami");
        client.setPrenom("Sara");
        client.setPhoneNumber("0600000000");
        client.setAdresse("Fès");
        client.setEmail("sara@mail.com");
        client.setMutuelle("CNOPS");
        client.setDateNaissance(LocalDate.of(1990, 1, 1));
        client.setDernierExamen(LocalDate.of(2026, 1, 1));
        client.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));

        ClientDTO dto = modelMapper.map(client, ClientDTO.class);

        assertEquals(client.getId(), dto.getId());
        assertEquals(client.getNom(), dto.getNom());
        assertEquals(client.getPrenom(), dto.getPrenom());
        assertEquals(client.getPhoneNumber(), dto.getPhoneNumber());
        assertEquals(client.getAdresse(), dto.getAdresse());
        assertEquals(client.getEmail(), dto.getEmail());
        assertEquals(client.getMutuelle(), dto.getMutuelle());
        assertEquals(client.getDateNaissance(), dto.getDateNaissance());
        assertEquals(client.getDernierExamen(), dto.getDernierExamen());
        assertEquals(client.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    @DisplayName("Doit mapper tous les champs de Commande vers CommandeDTO")
    void test_mapping_commande_vers_dto() {
        Commande commande = new Commande();
        commande.setId(1L);
        commande.setStatus("EN_COURS");
        commande.setDescription("desc");
        commande.setTotalprice(1000.0);
        commande.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));

        CommandeDTO dto = modelMapper.map(commande, CommandeDTO.class);

        assertEquals(commande.getId(), dto.getId());
        assertEquals(commande.getStatus(), dto.getStatus());
        assertEquals(commande.getDescription(), dto.getDescription());
        assertEquals(commande.getTotalprice(), dto.getTotalprice());
        assertEquals(commande.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    @DisplayName("Doit mapper tous les champs de CommandeItem vers CommandeItemDTO")
    void test_mapping_commandeItem_vers_dto() {
        CommandeItem item = new CommandeItem();
        item.setId(1L);
        item.setQuantity(5);
        item.setPrice(320.0);
        item.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));

        CommandeItemDTO dto = modelMapper.map(item, CommandeItemDTO.class);

        assertEquals(item.getId(), dto.getId());
        assertEquals(item.getQuantity(), dto.getQuantity());
        assertEquals(item.getPrice(), dto.getPrice());
        assertEquals(item.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    @DisplayName("Doit mapper tous les champs de Devis vers DevisDTO")
    void test_mapping_devis_vers_dto() {
        Devis devis = new Devis();
        devis.setId(1L);
        devis.setStatus("envoye");
        devis.setDescription("desc");
        devis.setTotalprice(500.0);
        devis.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));

        DevisDTO dto = modelMapper.map(devis, DevisDTO.class);

        assertEquals(devis.getId(), dto.getId());
        assertEquals(devis.getStatus(), dto.getStatus());
        assertEquals(devis.getDescription(), dto.getDescription());
        assertEquals(devis.getTotalprice(), dto.getTotalprice());
        assertEquals(devis.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    @DisplayName("Doit mapper tous les champs de DevisItems vers DevisItemDTO")
    void test_mapping_devisItem_vers_dto() {
        DevisItems item = new DevisItems();
        item.setId(1L);
        item.setQuantity(3);
        item.setPrixHT(100.0);
        item.setTva(20f);
        item.setDiscount(10f);
        item.setPrice(240.0);
        item.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));

        DevisItemDTO dto = modelMapper.map(item, DevisItemDTO.class);

        assertEquals(item.getId(), dto.getId());
        assertEquals(item.getQuantity(), dto.getQuantity());
        assertEquals(item.getPrixHT(), dto.getPrixHT());
        assertEquals(item.getTva(), dto.getTva());
        assertEquals(item.getDiscount(), dto.getDiscount());
        assertEquals(item.getPrice(), dto.getPrice());
        assertEquals(item.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    @DisplayName("Doit mapper tous les champs de Examen vers ExamenDTO")
    void test_mapping_examen_vers_dto() {
        Examen examen = new Examen();
        examen.setId(1L);
        examen.setDateExamen(LocalDate.of(2026, 1, 1));
        examen.setSphereOd(1.5f);
        examen.setCylindreOd(0.5f);
        examen.setAxeOd(90);
        examen.setEcartOd(30f);
        examen.setSphereOg(1.0f);
        examen.setCylindreOg(0.25f);
        examen.setAxeOg(80);
        examen.setEcartOg(31f);
        examen.setAddition(2.0f);
        examen.setRemarques("RAS");
        examen.setProchaineVisite(LocalDate.of(2028, 1, 1));
        examen.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));
        examen.setUpdatedAt(LocalDateTime.of(2026, 1, 2, 10, 0));

        ExamenDTO dto = modelMapper.map(examen, ExamenDTO.class);

        assertEquals(examen.getId(), dto.getId());
        assertEquals(examen.getDateExamen(), dto.getDateExamen());
        assertEquals(examen.getSphereOd(), dto.getSphereOd());
        assertEquals(examen.getCylindreOd(), dto.getCylindreOd());
        assertEquals(examen.getAxeOd(), dto.getAxeOd());
        assertEquals(examen.getEcartOd(), dto.getEcartOd());
        assertEquals(examen.getSphereOg(), dto.getSphereOg());
        assertEquals(examen.getCylindreOg(), dto.getCylindreOg());
        assertEquals(examen.getAxeOg(), dto.getAxeOg());
        assertEquals(examen.getEcartOg(), dto.getEcartOg());
        assertEquals(examen.getAddition(), dto.getAddition());
        assertEquals(examen.getRemarques(), dto.getRemarques());
        assertEquals(examen.getProchaineVisite(), dto.getProchaineVisite());
        assertEquals(examen.getCreatedAt(), dto.getCreatedAt());
        assertEquals(examen.getUpdatedAt(), dto.getUpdatedAt());
    }

    @Test
    @DisplayName("Doit mapper tous les champs de Fournisseur vers FournisseurDTO")
    void test_mapping_fournisseur_vers_dto() {
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setId(1L);
        fournisseur.setNom("Optic Supply");
        fournisseur.setPhoneNumber("0600000000");
        fournisseur.setAdresse("Casablanca");
        fournisseur.setEmail("contact@optic.com");
        fournisseur.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));

        FournisseurDTO dto = modelMapper.map(fournisseur, FournisseurDTO.class);

        assertEquals(fournisseur.getId(), dto.getId());
        assertEquals(fournisseur.getNom(), dto.getNom());
        assertEquals(fournisseur.getPhoneNumber(), dto.getPhoneNumber());
        assertEquals(fournisseur.getAdresse(), dto.getAdresse());
        assertEquals(fournisseur.getEmail(), dto.getEmail());
        assertEquals(fournisseur.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    @DisplayName("Doit mapper tous les champs de Mouvement_Stock vers Mouvement_StockDTO")
    void test_mapping_mouvementStock_vers_dto() {
        Mouvement_Stock mvt = new Mouvement_Stock();
        mvt.setId(1L);
        mvt.setDescription("desc");
        mvt.setQuantity(3);
        mvt.setType("ENTREE");
        mvt.setPrix_Unit(100.0);
        mvt.setPrix_total(300.0);
        mvt.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));

        Mouvement_StockDTO dto = modelMapper.map(mvt, Mouvement_StockDTO.class);

        assertEquals(mvt.getId(), dto.getId());
        assertEquals(mvt.getDescription(), dto.getDescription());
        assertEquals(mvt.getQuantity(), dto.getQuantity());
        assertEquals(mvt.getType(), dto.getType());
        assertEquals(mvt.getPrix_Unit(), dto.getPrix_Unit());
        assertEquals(mvt.getPrix_total(), dto.getPrix_total());
        assertEquals(mvt.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    @DisplayName("Doit mapper tous les champs de Notification vers NotificationDTO")
    void test_mapping_notification_vers_dto() {
        Notification notif = new Notification();
        notif.setId(1L);
        notif.setType("STOCK");
        notif.setDescription("desc");
        notif.setMessage("Stock faible");
        notif.set_read(false);
        notif.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));

        NotificationDTO dto = modelMapper.map(notif, NotificationDTO.class);

        assertEquals(notif.getId(), dto.getId());
        assertEquals(notif.getType(), dto.getType());
        assertEquals(notif.getDescription(), dto.getDescription());
        assertEquals(notif.getMessage(), dto.getMessage());
        assertEquals(notif.is_read(), dto.is_read());
        assertEquals(notif.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    @DisplayName("Doit mapper tous les champs de OrdonnanceLentille vers OrdonnanceLentilleDTO")
    void test_mapping_ordonnanceLentille_vers_dto() {
        OrdonnanceLentille ord = new OrdonnanceLentille();
        ord.setId(1L);
        ord.setPrescripteur("Dr Alami");
        ord.setDateEmission(LocalDate.of(2026, 1, 1));
        ord.setDateExpiration(LocalDate.of(2028, 1, 1));
        ord.setSphereOd(1.0f);
        ord.setCylindreOd(0.5f);
        ord.setAxeOd(90);
        ord.setRayonOd(8.6f);
        ord.setDiametreOd(14.2f);
        ord.setMatiereOd("Silicone");
        ord.setSphereOg(1.5f);
        ord.setCylindreOg(0.25f);
        ord.setAxeOg(85);
        ord.setRayonOg(8.5f);
        ord.setDiametreOg(14.0f);
        ord.setMatiereOg("Silicone");
        ord.setImage("url");
        ord.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));
        ord.setUpdatedAt(LocalDateTime.of(2026, 1, 2, 10, 0));

        OrdonnanceLentilleDTO dto = modelMapper.map(ord, OrdonnanceLentilleDTO.class);

        assertEquals(ord.getId(), dto.getId());
        assertEquals(ord.getPrescripteur(), dto.getPrescripteur());
        assertEquals(ord.getDateEmission(), dto.getDateEmission());
        assertEquals(ord.getDateExpiration(), dto.getDateExpiration());
        assertEquals(ord.getSphereOd(), dto.getSphereOd());
        assertEquals(ord.getCylindreOd(), dto.getCylindreOd());
        assertEquals(ord.getAxeOd(), dto.getAxeOd());
        assertEquals(ord.getRayonOd(), dto.getRayonOd());
        assertEquals(ord.getDiametreOd(), dto.getDiametreOd());
        assertEquals(ord.getMatiereOd(), dto.getMatiereOd());
        assertEquals(ord.getSphereOg(), dto.getSphereOg());
        assertEquals(ord.getCylindreOg(), dto.getCylindreOg());
        assertEquals(ord.getAxeOg(), dto.getAxeOg());
        assertEquals(ord.getRayonOg(), dto.getRayonOg());
        assertEquals(ord.getDiametreOg(), dto.getDiametreOg());
        assertEquals(ord.getMatiereOg(), dto.getMatiereOg());
        assertEquals(ord.getImage(), dto.getImage());
        assertEquals(ord.getCreatedAt(), dto.getCreatedAt());
        assertEquals(ord.getUpdatedAt(), dto.getUpdatedAt());
    }

    @Test
    @DisplayName("Doit mapper tous les champs de OrdonnanceLunette vers OrdonnanceLunetteDTO")
    void test_mapping_ordonnanceLunette_vers_dto() {
        OrdonnanceLunette ord = new OrdonnanceLunette();
        ord.setId(1L);
        ord.setPrescripteur("Dr Alami");
        ord.setDateEmission(LocalDate.of(2026, 1, 1));
        ord.setDateExpiration(LocalDate.of(2028, 1, 1));
        ord.setSphereOd(1.0f);
        ord.setCylindreOd(0.5f);
        ord.setAxeOd(90);
        ord.setAdditionOd(1.5f);
        ord.setSphereOg(1.25f);
        ord.setCylindreOg(0.25f);
        ord.setAxeOg(85);
        ord.setAdditionOg(1.25f);
        ord.setImage("url");
        ord.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));
        ord.setUpdatedAt(LocalDateTime.of(2026, 1, 2, 10, 0));

        OrdonnanceLunetteDTO dto = modelMapper.map(ord, OrdonnanceLunetteDTO.class);

        assertEquals(ord.getId(), dto.getId());
        assertEquals(ord.getPrescripteur(), dto.getPrescripteur());
        assertEquals(ord.getDateEmission(), dto.getDateEmission());
        assertEquals(ord.getDateExpiration(), dto.getDateExpiration());
        assertEquals(ord.getSphereOd(), dto.getSphereOd());
        assertEquals(ord.getCylindreOd(), dto.getCylindreOd());
        assertEquals(ord.getAxeOd(), dto.getAxeOd());
        assertEquals(ord.getAdditionOd(), dto.getAdditionOd());
        assertEquals(ord.getSphereOg(), dto.getSphereOg());
        assertEquals(ord.getCylindreOg(), dto.getCylindreOg());
        assertEquals(ord.getAxeOg(), dto.getAxeOg());
        assertEquals(ord.getAdditionOg(), dto.getAdditionOg());
        assertEquals(ord.getImage(), dto.getImage());
        assertEquals(ord.getCreatedAt(), dto.getCreatedAt());
        assertEquals(ord.getUpdatedAt(), dto.getUpdatedAt());
    }

    @Test
    @DisplayName("Doit mapper tous les champs de Paiment vers PaimentDTO")
    void test_mapping_paiment_vers_dto() {
        Paiment paiment = new Paiment();
        paiment.setId(1L);
        paiment.setDescription("desc");
        paiment.setMontant_Paye(500.0);
        paiment.setMethod("CB");
        paiment.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));

        PaimentDTO dto = modelMapper.map(paiment, PaimentDTO.class);

        assertEquals(paiment.getId(), dto.getId());
        assertEquals(paiment.getDescription(), dto.getDescription());
        assertEquals(paiment.getMontant_Paye(), dto.getMontant_Paye());
        assertEquals(paiment.getMethod(), dto.getMethod());
        assertEquals(paiment.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    @DisplayName("Doit mapper tous les champs de Produit vers ProduitDTO")
    void test_mapping_produit_vers_dto() {
        Produit produit = new Produit();
        produit.setId(1L);
        produit.setNom("Verre Zeiss");
        produit.setDescription("desc");
        produit.setImage("url");
        produit.setCode_barre("123456");
        produit.setQuantity(10);
        produit.setPrixAchat(300.0);
        produit.setPrixHT(400.0);
        produit.setTva(20f);
        produit.setDiscount(5f);
        produit.setPrice(450.0);
        produit.setMarque("Zeiss");
        produit.setCouleur("Noir");
        produit.setModele("XZ1");
        produit.setIndice(1.6f);
        produit.setDiametre(65.0f);
        produit.setSeuilMin(2);
        produit.setTraitement("Anti-reflet");
        produit.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));

        ProduitDTO dto = modelMapper.map(produit, ProduitDTO.class);

        assertEquals(produit.getId(), dto.getId());
        assertEquals(produit.getNom(), dto.getNom());
        assertEquals(produit.getDescription(), dto.getDescription());
        assertEquals(produit.getImage(), dto.getImage());
        assertEquals(produit.getCode_barre(), dto.getCode_barre());
        assertEquals(produit.getQuantity(), dto.getQuantity());
        assertEquals(produit.getPrixAchat(), dto.getPrixAchat());
        assertEquals(produit.getPrixHT(), dto.getPrixHT());
        assertEquals(produit.getTva(), dto.getTva());
        assertEquals(produit.getDiscount(), dto.getDiscount());
        assertEquals(produit.getPrice(), dto.getPrice());
        assertEquals(produit.getMarque(), dto.getMarque());
        assertEquals(produit.getCouleur(), dto.getCouleur());
        assertEquals(produit.getModele(), dto.getModele());
        assertEquals(produit.getIndice(), dto.getIndice());
        assertEquals(produit.getDiametre(), dto.getDiametre());
        assertEquals(produit.getSeuilMin(), dto.getSeuilMin());
        assertEquals(produit.getTraitement(), dto.getTraitement());
        assertEquals(produit.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    @DisplayName("Doit mapper tous les champs de Remboursement vers RemboursementDTO")
    void test_mapping_remboursement_vers_dto() {
        Remboursement remb = new Remboursement();
        remb.setId(1L);
        remb.setStatus("EN_ATTENTE");
        remb.setDescription("desc");
        remb.setMontant_mutuelle(200.0);
        remb.setMontant_patient(50.0);
        remb.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));

        RemboursementDTO dto = modelMapper.map(remb, RemboursementDTO.class);

        assertEquals(remb.getId(), dto.getId());
        assertEquals(remb.getStatus(), dto.getStatus());
        assertEquals(remb.getDescription(), dto.getDescription());
        assertEquals(remb.getMontant_mutuelle(), dto.getMontant_mutuelle());
        assertEquals(remb.getMontant_patient(), dto.getMontant_patient());
        assertEquals(remb.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    @DisplayName("Doit mapper tous les champs de SalesOrder vers SalesOrderDTO")
    void test_mapping_salesOrder_vers_dto() {
        SalesOrder so = new SalesOrder();
        so.setId(1L);
        so.setStatus("payee");
        so.setDescription("desc");
        so.setAdresse("Fès");
        so.setTotalprice(1500.0);
        so.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));

        SalesOrderDTO dto = modelMapper.map(so, SalesOrderDTO.class);

        assertEquals(so.getId(), dto.getId());
        assertEquals(so.getStatus(), dto.getStatus());
        assertEquals(so.getDescription(), dto.getDescription());
        assertEquals(so.getAdresse(), dto.getAdresse());
        assertEquals(so.getTotalprice(), dto.getTotalprice());
        assertEquals(so.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    @DisplayName("Doit mapper tous les champs de SalesOrderItems vers SalesOrderItemDTO")
    void test_mapping_salesOrderItems_vers_dto() {
        SalesOrderItems item = new SalesOrderItems();
        item.setId(1L);
        item.setQuantity(2);
        item.setPrixHT(200.0);
        item.setTva(20f);
        item.setDiscount(0f);
        item.setPrice(480.0);
        item.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));

        SalesOrderItemDTO dto = modelMapper.map(item, SalesOrderItemDTO.class);

        assertEquals(item.getId(), dto.getId());
        assertEquals(item.getQuantity(), dto.getQuantity());
        assertEquals(item.getPrixHT(), dto.getPrixHT());
        assertEquals(item.getTva(), dto.getTva());
        assertEquals(item.getDiscount(), dto.getDiscount());
        assertEquals(item.getPrice(), dto.getPrice());
        assertEquals(item.getCreatedAt(), dto.getCreatedAt());
    }
}