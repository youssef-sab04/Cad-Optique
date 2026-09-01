package cad.project.controller;


import cad.project.playload.NotificationDTO;
import cad.project.playload.NotificationResponse;
import cad.project.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NotificationController {

    @Autowired
    NotificationService notificationService  ;


    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Get Notifications admin (stock)")
    @GetMapping("/admin/notifications")
    public ResponseEntity<NotificationResponse> getAllNotificationsAdmin(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        NotificationResponse notificationResponse = notificationService.getAllNotificationsAdmin(keyword, pageNumber, pageSize);
        return new ResponseEntity<>(notificationResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Get all Notifications du clients")
    @GetMapping("/public/notifications/clients")
    public ResponseEntity<NotificationResponse> getNotificationsByClient(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        NotificationResponse notificationResponse = notificationService.getAllNotificationsClient(keyword, pageNumber, pageSize);
        return new ResponseEntity<>(notificationResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Get Notifications par client")
    @GetMapping("/public/notifications/clients/{clientId}")
    public ResponseEntity<NotificationResponse> getNotificationsByClient(
            @PathVariable Long clientId,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        NotificationResponse notificationResponse = notificationService.getNotificationsByClient(clientId, pageNumber, pageSize);
        return new ResponseEntity<>(notificationResponse, HttpStatus.OK);
    }



    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Get Notifications par produit")
    @GetMapping("/admin/notifications/produits/{produitId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByProduit(@PathVariable Long produitId) {
        List<NotificationDTO> notificationDTOS = notificationService.getNotificationsByProduit(produitId);
        return new ResponseEntity<>(notificationDTOS, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Lire notif")
    @PutMapping("/admin/notifications/{notifId}")
    public ResponseEntity<NotificationDTO> viewNotifications(@PathVariable Long notifId) {
        NotificationDTO notificationDTO = notificationService.viewNotifications(notifId);
        return new ResponseEntity<>(notificationDTO, HttpStatus.OK);
    }




}
