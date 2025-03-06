package fr.louann.backcoldcenter.controller;

import fr.louann.backcoldcenter.models.User;
import fr.louann.backcoldcenter.models.UserPreferencesDTO;
import fr.louann.backcoldcenter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/preferences")
    public ResponseEntity<User> updatePreferences(@PathVariable Long id, @RequestBody UserPreferencesDTO preferences) {
        User updatedUser = userService.updatePreferences(id, preferences);
        return ResponseEntity.ok(updatedUser);
    }

}
