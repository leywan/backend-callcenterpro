package fr.louann.backcoldcenter.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password; // Hashed

    @Enumerated(EnumType.STRING)
    private UserRole role; // Opérateur, Superviseur, Admin

    @Enumerated(EnumType.STRING)
    private Shift shift; // Matin, Après-midi, Nuit

    @Enumerated(EnumType.STRING)
    private UserStatus status; // En ligne, Occupé, Hors ligne

    @ElementCollection
    private List<String> skills; // Liste de compétences

    private boolean notificationsEnabled = true; // Préférence notifications

    @Enumerated(EnumType.STRING) // 🔹 Convertit l'Enum en STRING pour la BDD
    @Column(name = "theme")
    private Theme theme = Theme.SOMBRE; // Préférence d'affichage


    @Enumerated(EnumType.STRING)
    private Language language = Language.FRANCAIS; // Langue préférée

    private boolean twoFactorAuth = false; // Authentification à deux facteurs

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;
}
