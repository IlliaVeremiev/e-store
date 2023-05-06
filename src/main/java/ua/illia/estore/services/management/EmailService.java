package ua.illia.estore.services.management;

import java.util.List;

public interface EmailService {

    void sendTemplateMail(String fromEmail, String fromName, List<String> to, String header, String htmlBody);
}
