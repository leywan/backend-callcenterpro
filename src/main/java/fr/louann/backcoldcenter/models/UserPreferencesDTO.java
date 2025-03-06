package fr.louann.backcoldcenter.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPreferencesDTO {
    private boolean notificationsEnabled;
    private Theme theme;
    private Language language;
    private boolean twoFactorAuth;
}

