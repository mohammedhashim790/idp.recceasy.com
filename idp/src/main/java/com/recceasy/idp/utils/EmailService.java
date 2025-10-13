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
    public void sendVerificationEmail(String link, String recipient) throws UnsupportedEncodingException, jakarta.mail.MessagingException {
        String subject = "Welcome to Recceasy.";
        String content = format("We are happy to have you with us. " +
                "Please verify your email address by clicking on the link below. " +
                "\n\n <a href=\"%s\">%s</a>", link, link);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromEmailAddress, "Applaud Innovations");
        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }


    @Async
    public void sendOTPEmail(String code, String recipient) throws UnsupportedEncodingException, jakarta.mail.MessagingException {
        String subject = "Password Reset Requested";
        String content = format("Please use the code to reset your password {%s}", code);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromEmailAddress, "Applaud Innovations");
        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }
}
