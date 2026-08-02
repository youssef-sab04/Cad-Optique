package cad.project.service;

import cad.project.playload.NotificationResponse;
import cad.project.playload.NotificationDTO;

import java.util.List;


public interface NotificationService {
    List<NotificationDTO> getNotificationsByProduit(Long produitId);

    NotificationResponse getNotificationsByClient(Long clientId, Integer pageNumber, Integer pageSize);

    NotificationResponse getAllNotificationsAdmin(String keyword, Integer pageNumber, Integer pageSize);
    NotificationResponse getAllNotificationsClient(String keyword, Integer pageNumber, Integer pageSize);


    NotificationDTO viewNotifications(Long notifId);

}
