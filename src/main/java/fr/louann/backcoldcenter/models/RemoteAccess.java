package fr.louann.backcoldcenter.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "remote_access")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RemoteAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // Ex: "teamviewer", "danfoss", "actionfroid"
    private String url;
    private String username;
    private String password;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @JsonIgnoreProperties("remoteAccess")
    private Client client;
}
