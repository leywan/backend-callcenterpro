package fr.louann.backcoldcenter.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RemoteAccessDTO {
    private Long id;
    private String type;
    private String url;
    private String username;
    private String password;

    public static RemoteAccessDTO fromEntity(RemoteAccess remoteAccess) {
        return new RemoteAccessDTO(
                remoteAccess.getId(),
                remoteAccess.getType(),
                remoteAccess.getUrl(),
                remoteAccess.getUsername(),
                remoteAccess.getPassword()
        );
    }
}

