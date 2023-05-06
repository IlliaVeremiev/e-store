package ua.illia.estore.services.management.impl;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ua.illia.estore.services.management.EmailService;

import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class DefaultEmailService implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Override
    @SneakyThrows
    public void sendTemplateMail(String fromEmail, String fromName, List<String> to, String header, String htmlBody) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        message.setFrom(fromEmail, fromName);
        message.setTo(to.toArray(new String[]{}));
        message.setSubject(header);
        message.setText(htmlBody, true);

        emailSender.send(mimeMessage);
    }
}
