package net.brise_nocturne.brise_nocturne_backend.services;

import net.brise_nocturne.brise_nocturne_backend.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${app.contact.recipient-email}") // Définissez cette propriété !
    private String recipientEmail;

    // Récupérer l'adresse d'envoi (peut être la même que spring.mail.username)
    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendContactFormEmail(ContactFormDTO contactForm) {
        log.info("Tentative d'envoi d'email de contact de {} pour le sujet '{}'", contactForm.getEmail(), contactForm.getSubject());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail); // L'email configuré dans Spring Mail
            message.setTo(recipientEmail); // Votre adresse email où recevoir les messages
            message.setReplyTo(contactForm.getEmail()); // Important pour répondre à l'utilisateur !
            message.setSubject("Nouveau message de contact: " + contactForm.getSubject());

            String emailBody = String.format(
                "Vous avez reçu un nouveau message de contact :\n\n" +
                "Nom: %s\n" +
                "Email: %s\n" +
                "Sujet: %s\n\n" +
                "Message:\n%s",
                contactForm.getName(),
                contactForm.getEmail(),
                contactForm.getSubject(),
                contactForm.getMessage()
            );
            message.setText(emailBody);

            javaMailSender.send(message);
            log.info("Email de contact envoyé avec succès à {}", recipientEmail);

        } catch (MailException e) {
            log.error("Erreur lors de l'envoi de l'email de contact: {}", e.getMessage(), e);
            // Relancer une exception personnalisée ou gérer l'erreur ici si nécessaire
            // Par exemple, throw new RuntimeException("Impossible d'envoyer l'email de contact.", e);
            // Pour l'instant, on logue juste, le contrôleur renverra une erreur 500 par défaut.
             throw e; // Relancer pour que le contrôleur sache qu'il y a eu un problème
        }
    }
}
