package net.brise_nocturne.brise_nocturne_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ContactFormDTO {
    @NotBlank(message = "Le nom ne peut pas être vide")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    private String name;

    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "Veuillez fournir une adresse email valide")
    @Size(max = 100, message = "L'email ne doit pas dépasser 100 caractères")
    private String email;

    @NotBlank(message = "Le sujet ne peut pas être vide")
    @Size(max = 150, message = "Le sujet ne doit pas dépasser 150 caractères")
    private String subject;

    @NotBlank(message = "Le message ne peut pas être vide")
    @Size(max = 10000, message = "Le message ne doit pas dépasser 10000 caractères")
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
