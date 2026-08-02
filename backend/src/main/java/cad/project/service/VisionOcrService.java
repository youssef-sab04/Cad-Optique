package cad.project.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class VisionOcrService {

    @Value("${google.vision.api.key}")
    private String apiKey;

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(VisionOcrService.class);

    private final RestTemplate restTemplate = new RestTemplate();

    public String extractText(byte[] imageBytes) {
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        Map<String, Object> imageMap = Map.of("content", base64Image);
        Map<String, Object> feature = Map.of("type", "DOCUMENT_TEXT_DETECTION");
        Map<String, Object> request = Map.of(
                "requests", List.of(Map.of(
                        "image", imageMap,
                        "features", List.of(feature)
                ))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        String url = "https://vision.googleapis.com/v1/images:annotate?key=" + apiKey;

        Map response = restTemplate.postForObject(url, entity, Map.class);

        List<Map> responses = (List<Map>) response.get("responses");
        if (responses == null || responses.isEmpty()) return "";

        Map firstResponse = responses.get(0);
        Map fullTextAnnotation = (Map) firstResponse.get("fullTextAnnotation");
        if (fullTextAnnotation == null) return "";

        log.info("REPONSE VISION API BRUTE: {}", response);

        return (String) fullTextAnnotation.get("text");
    }
}