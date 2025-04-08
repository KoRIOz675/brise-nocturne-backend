package net.brise_nocturne.brise_nocturne_backend.controller;

import net.brise_nocturne.brise_nocturne_backend.dto.*;
import net.brise_nocturne.brise_nocturne_backend.services.*;
import jakarta.validation.Valid; // Ou javax.validation.Valid
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/contact") // Préfixe pour toutes les méthodes de ce contrôleur
public class ContactController {

    private static final Logger log = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private EmailService emailService;

    @PostMapping // Gère les requêtes POST sur /api/contact
    public ResponseEntity<?> submitContactForm(@Valid @RequestBody ContactFormDTO contactFormDto) {
        // L'annotation @Valid déclenche la validation du DTO
        // Si la validation échoue, une MethodArgumentNotValidException sera levée (gérée plus bas)
        log.info("Réception d'une soumission de formulaire de contact de {}", contactFormDto.getEmail());

        try {
            emailService.sendContactFormEmail(contactFormDto);
            // Si l'envoi réussit, renvoyer une réponse OK
            return ResponseEntity.ok().body(Map.of("message", "Message envoyé avec succès !"));
        } catch (MailException e) {
            // Si EmailService lève une MailException (ou autre exception liée à l'email)
            log.error("Erreur serveur lors de l'envoi de l'email : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("message", "Erreur serveur lors de l'envoi de l'email. Veuillez réessayer plus tard."));
        } catch (Exception e) {
            // Attrape d'autres erreurs inattendues
             log.error("Erreur inattendue lors du traitement du formulaire : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("message", "Une erreur inattendue est survenue."));
        }
    }

    // --- Gestionnaire d'erreurs de validation ---
    // Cette méthode intercepte les erreurs de validation levées par @Valid
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Définit le code de statut HTTP à 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.warn("Erreurs de validation du formulaire de contact: {}", errors);
        // On pourrait renvoyer une structure plus complexe si besoin
        // Pour l'instant, on renvoie une map champ -> message d'erreur
        return Map.of("message", "Erreurs de validation", "errors", errors.toString()); // Simplifié, idéalement renvoyer la map 'errors'
    }

    // (Optionnel) Gestionnaire pour d'autres exceptions spécifiques si nécessaire
}