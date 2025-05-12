package com.example.quanlynhansu.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendHtmlEmailWithAttachment(
            String toEmail,
            String subject,
            String htmlContent,
            MultipartFile attachment // có thể là null nếu không đính kèm
    ) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("ptitnhansu@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // Gửi HTML

        // Xử lý file đính kèm
        if (attachment != null && !attachment.isEmpty()) {
            helper.addAttachment(
                    Objects.requireNonNull(attachment.getOriginalFilename()), // Tên file hiển thị
                    attachment // MultipartFile implements InputStreamSource
            );
        }

        mailSender.send(message);
    }

}
