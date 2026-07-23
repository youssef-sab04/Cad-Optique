package cad.project.service;

public interface WhatsAppService {
    void sendTemplateMessage(String toPhoneNumber, String templateName, String... params);
}