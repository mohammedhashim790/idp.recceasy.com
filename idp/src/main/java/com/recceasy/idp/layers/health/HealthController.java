package com.recceasy.idp.layers.health;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public String health() {
        return "{\"status\":\"I'm healthy\"}";
    }

    @GetMapping("/time")
    public long time() {
        return  System.currentTimeMillis();
    }




}
