package fr.louann.backcoldcenter.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private Long companyId;
    private String companyName;
    private String equipments;
    private String status;
    // Initialisation vide pour éviter le chargement N+1
    private List<RemoteAccess> remoteAccess = new ArrayList<>();

    public static ClientDTO fromEntity(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setAddress(client.getAddress());
        dto.setPhone(client.getPhone());
        dto.setEmail(client.getEmail());
        dto.setCompanyId(client.getCompany() != null ? client.getCompany().getId() : null);
        dto.setCompanyName(client.getCompany() != null ? client.getCompany().getName() : null);
        dto.setEquipments(client.getEquipments());
        dto.setStatus(client.getStatus());
        // On laisse remoteAccess vide pour ne pas forcer son chargement
        dto.setRemoteAccess(new ArrayList<>());
        return dto;
    }
}
