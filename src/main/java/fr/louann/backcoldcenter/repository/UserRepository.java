package fr.louann.backcoldcenter.repository;

import fr.louann.backcoldcenter.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByCompanyId(Long companyId);
    User findByEmail(String email);
}