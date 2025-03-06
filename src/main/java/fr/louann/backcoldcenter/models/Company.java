package fr.louann.backcoldcenter.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Nom de la permanence
    private String address; // Adresse
    private String contactPerson; // Personne de contact
    private String email;
    private String phone;
    private String website;

    private String notes; // Notes générales

    @Column(nullable = false)
    private String status = "active"; // Par défaut, l'entreprise est active

    // Informations contractuelles
    private Date contractStartDate;
    private Date contractEndDate;
    private LocalTime interventionStartTime;
    private LocalTime interventionEndTime;

    @Embedded
    private BillingInfo billingInfo; // Informations de facturation (ex: Nom, TVA, Adresse)

    // Nouveau champ OperatingHours (objet Embedded)
    @Embedded
    private OperatingHours operatingHours;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // Ajoutez cette annotation pour empêcher la sérialisation de la liste des clients
    private List<Client> clients;
}
