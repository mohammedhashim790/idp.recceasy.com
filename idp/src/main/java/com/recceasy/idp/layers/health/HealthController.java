package com.recceasy.idp.layers.health;

import com.recceasy.idp.utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/health")
public class HealthController {

    @Autowired
    private EmailService emailService;

    @GetMapping
    public String health() throws MessagingException, jakarta.mail.MessagingException, UnsupportedEncodingException {
        emailService.sendEmail("mohammedhashim790@gmail.com","Test Messae","asdasd");
        return "{\"status\":\"I'm healthy\"}";
    }

    @GetMapping("/time")
    public long time() {
        return  System.currentTimeMillis();
    }




}
