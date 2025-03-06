package fr.louann.backcoldcenter.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "clients")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Nom du client (Supermarché Central)
    private String address; // Adresse
    private String phone;
    private String email;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @JsonIgnoreProperties("clients") // Évite la boucle infinie
    private Company company;

    private String equipments; // Ex: "3 chambres froides, 5 vitrines réfrigérées"

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("client") // Empêche la boucle infinie, mais garde les données accessibles
    private List<RemoteAccess> remoteAccess;

    private String status; // Statut du client (Actif, Inactif, En attente)
}
