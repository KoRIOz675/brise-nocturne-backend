package net.brise_nocturne.brise_nocturne_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class mainController {
    @GetMapping("/api/hello")
    public String hello() {
        return "Spring Boot Connected YaY";
    }
}
