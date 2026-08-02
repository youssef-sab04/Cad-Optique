package cad.project.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class WhatsAppServiceImp implements WhatsAppService {

    @Value("${whatsapp.api.token}")
    private String token;

    @Value("${whatsapp.phone.number.id}")
    private String phoneNumberId;

    @Value("${whatsapp.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public WhatsAppServiceImp(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void sendTemplateMessage(String toPhoneNumber, String templateName, String... params) {
        toPhoneNumber = normalizePhoneNumber(toPhoneNumber);
        String url = apiUrl + "/" + phoneNumberId + "/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        List<Map<String, Object>> parameters = new ArrayList<>();
        for (String param : params) {
            parameters.add(Map.of("type", "text", "text", param));
        }

        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("to", toPhoneNumber);
        body.put("type", "template");
        body.put("template", Map.of(
                "name", templateName,
                "language", Map.of("code", "fr"),
                "components", List.of(Map.of("type", "body", "parameters", parameters))
        ));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(url, request, String.class);
    }

    private String normalizePhoneNumber(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.startsWith("0")) {
            return "212" + phoneNumber.substring(1);
        }
        return phoneNumber;
    }
}