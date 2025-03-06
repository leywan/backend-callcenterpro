package fr.louann.backcoldcenter.service;

import fr.louann.backcoldcenter.models.User;
import fr.louann.backcoldcenter.models.UserPreferencesDTO;
import fr.louann.backcoldcenter.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updatePreferences(Long id, UserPreferencesDTO preferences) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        user.setNotificationsEnabled(preferences.isNotificationsEnabled());
        user.setTheme(preferences.getTheme());
        user.setLanguage(preferences.getLanguage());
        user.setTwoFactorAuth(preferences.isTwoFactorAuth());

        return userRepository.save(user);
    }

}
