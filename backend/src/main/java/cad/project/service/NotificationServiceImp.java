package cad.project.service;

import cad.project.config.AppConstants;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.Client;
import cad.project.model.Notification;
import cad.project.playload.ClientDTO;
import cad.project.playload.NotificationDTO;
import cad.project.playload.NotificationResponse;
import cad.project.playload.ProduitDTO;
import cad.project.repositries.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImp implements  NotificationService {

    @Autowired
    private OrdonanceLunetteRepositry ordonnanceLunetteRepository ;

    @Autowired
    private OrdonnanceLentilleRepository ordonnanceLentilleRepository;

    @Autowired
    private ExamenRepository examenRepository;

    @Autowired
    private NotificationRepositry notificationRepositry;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private WhatsAppService whatsAppService;
    @Autowired
    private ClientRepositry clientRepositry;



    @Scheduled(cron = "0 0 7 * * *")
    //@Scheduled(cron = "0 */1 * * * *")

    public void genererRappelsOrdonnancesEtExamens() {
        LocalDate today = LocalDate.now();
        LocalDate limite = today.plusDays(AppConstants.DELAI_RAPPEL_JOURS);

        List<Notification> notifications = new ArrayList<>();

        ordonnanceLunetteRepository.findByDateExpirationBetween(today, limite).forEach(o -> {
            if (!notificationRepositry.existsByClientIdAndTypeAndMessage(o.getClient().getId(), "rappel_ordonnance_lunette", "L'ordonnance lunette expire le " + o.getDateExpiration())) {
                boolean envoye = envoyerRappelWhatsApp(o.getClient(), "Ordonnance lunette", o.getDateExpiration().toString());
                if (envoye) {
                    Notification n = new Notification();
                    n.setClient(o.getClient());
                    n.setType("rappel_ordonnance_lunette");
                    n.setMessage("L'ordonnance lunette expire le " + o.getDateExpiration());
                    n.set_read(false);
                    notifications.add(n);
                }
            }
        });

        ordonnanceLentilleRepository.findByDateExpirationBetween(today, limite).forEach(o -> {
            if (!notificationRepositry.existsByClientIdAndTypeAndMessage(o.getClient().getId(), "rappel_ordonnance_lentille", "L'ordonnance lentille expire le " + o.getDateExpiration())) {
                boolean envoye = envoyerRappelWhatsApp(o.getClient(), "Ordonnance lentille", o.getDateExpiration().toString());
                if (envoye) {
                    Notification n = new Notification();
                    n.setClient(o.getClient());
                    n.setType("rappel_ordonnance_lentille");
                    n.setMessage("L'ordonnance lentille expire le " + o.getDateExpiration());
                    n.set_read(false);
                    notifications.add(n);
                }
            }
        });

        examenRepository.findByProchaineVisiteBetween(today, limite).forEach(e -> {
            if (!notificationRepositry.existsByClientIdAndTypeAndMessage(e.getClient().getId(), "rappel_examen", "Prochaine visite prevue le " + e.getProchaineVisite())) {
                boolean envoye = envoyerRappelWhatsApp(e.getClient(), "Examen de vue", e.getProchaineVisite().toString());
                if (envoye) {
                    Notification n = new Notification();
                    n.setClient(e.getClient());
                    n.setType("rappel_examen");
                    n.setMessage("Prochaine visite prevue le " + e.getProchaineVisite());
                    n.set_read(false);
                    notifications.add(n);
                }
            }
        });

        notificationRepositry.saveAll(notifications);
    }

    private boolean envoyerRappelWhatsApp(Client client, String raison, String date) {
        try {
            whatsAppService.sendTemplateMessage(
                    client.getPhoneNumber(),
                    "rappel_ordonnance_examen",
                    client.getNom() + " " + client.getPrenom(),
                    raison,
                    date
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    @Override
    public NotificationDTO viewNotifications(Long notifId) {
        Notification notification = notificationRepositry.findById(notifId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "notificationId", notifId));

        notification.set_read(true);
        notificationRepositry.save(notification);

        return mapToDTO(notification);
    }

    @Override
    public NotificationResponse getAllNotificationsAdmin(String keyword, Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        Page<Notification> pageNotifications = notificationRepositry.findByClientIsNullAndProduitNomContainingIgnoreCase(keyword, pageDetails);

        return mapToResponse(pageNotifications);
    }

    @Override
    public NotificationResponse getAllNotificationsClient(String keyword, Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        Page<Notification> pageNotifications = notificationRepositry.findAllClientByKeyword(keyword, pageDetails);

        return mapToResponse(pageNotifications);
    }

    @Override
    public NotificationResponse getNotificationsByClient(Long clientId, Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        Page<Notification> pageNotifications = notificationRepositry.findByClientId(clientId, pageDetails);

        return mapToResponse(pageNotifications);
    }

    @Override
    public List<NotificationDTO> getNotificationsByProduit(Long produitId) {
        List<Notification> notifications = notificationRepositry.findByProduitId(produitId);

        return notifications.stream()
                .map(this::mapToDTO)
                .toList();
    }

    private NotificationDTO mapToDTO(Notification n) {
        NotificationDTO dto = modelMapper.map(n, NotificationDTO.class);
        if (n.getClient() != null) {
            dto.setClientDTO(modelMapper.map(n.getClient(), ClientDTO.class));
        }
        if (n.getProduit() != null) {
            dto.setProduitDTO(modelMapper.map(n.getProduit(), ProduitDTO.class));
        }
        return dto;
    }

    private NotificationResponse mapToResponse(Page<Notification> pageNotifications) {
        List<NotificationDTO> notificationDTOS = pageNotifications.getContent().stream()
                .map(this::mapToDTO)
                .toList();

        NotificationResponse notificationResponse = new NotificationResponse();
        notificationResponse.setContent(notificationDTOS);
        notificationResponse.setPageNumber(pageNotifications.getNumber());
        notificationResponse.setPageSize(pageNotifications.getSize());
        notificationResponse.setTotalElements(pageNotifications.getTotalElements());
        notificationResponse.setTotalPages(pageNotifications.getTotalPages());
        notificationResponse.setLastPage(pageNotifications.isLast());

        return notificationResponse;
    }

}