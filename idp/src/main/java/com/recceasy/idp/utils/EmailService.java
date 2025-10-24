package com.recceasy.idp.utils;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

import static java.lang.String.format;

@Service
public class EmailService {

    @Value("${from.email.address}")
    private String fromEmailAddress;

    // TODO add credentials on POM.xml in production
    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendEmail(String recipient, String subject, String content) throws UnsupportedEncodingException, MessagingException, jakarta.mail.MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromEmailAddress, "My Email Address");
        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    @Async
    public void sendVerificationEmail(String recipient, String token) throws UnsupportedEncodingException, jakarta.mail.MessagingException {
        String subject = "Welcome to Recceasy.";
        // TODO : CHANGE TO DYNAMIC URL
        String verificationUrl = "http://localhost:8073/health/verify?token=" + token;
        String content = format("We are happy to have you with us. " + "Please verify your email address by clicking on the link below. " + "\n\n <a href=\"%s\">%s</a>", verificationUrl, verificationUrl);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromEmailAddress, "Applaud Innovations");
        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }
}
