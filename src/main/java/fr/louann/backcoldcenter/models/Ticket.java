package fr.louann.backcoldcenter.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.louann.backcoldcenter.models.Priority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String callerName; // Nom de l'appelant
    private String callerPhone; // Téléphone de l'appelant

    @Column(nullable = false)
    private String description; // Description du problème

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private String notes; // Notes additionnelles

    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.OUVERT;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties("tickets")
    private Client client; // ⚡ Relation avec le client

    @ManyToOne
    @JoinColumn(name = "assigned_to", referencedColumnName = "id")
    private User assignedTo; // L'agent ou technicien assigné

    // ⚡ Ajout d'une méthode pour récupérer la Company via le Client
    public Company getCompany() {
        return client != null ? client.getCompany() : null;
    }
}

