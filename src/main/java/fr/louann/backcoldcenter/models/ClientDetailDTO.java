package fr.louann.backcoldcenter.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientDetailDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private Long companyId; // On stocke l'ID de la société
    private String companyName;
    private String equipments;
    private String status;
    private List<RemoteAccess> remoteAccess; // Chargement uniquement pour le détail

    public static ClientDetailDTO fromEntity(Client client) {
        ClientDetailDTO dto = new ClientDetailDTO();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setAddress(client.getAddress());
        dto.setPhone(client.getPhone());
        dto.setEmail(client.getEmail());
        dto.setCompanyId(client.getCompany() != null ? client.getCompany().getId() : null);
        dto.setCompanyName(client.getCompany() != null ? client.getCompany().getName() : null);
        dto.setEquipments(client.getEquipments());
        dto.setStatus(client.getStatus());
        dto.setRemoteAccess(client.getRemoteAccess()); // La collection est chargée via fetch join
        return dto;
    }
}
