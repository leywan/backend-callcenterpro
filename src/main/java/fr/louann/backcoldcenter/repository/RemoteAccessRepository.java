package fr.louann.backcoldcenter.repository;

import fr.louann.backcoldcenter.models.RemoteAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RemoteAccessRepository extends JpaRepository<RemoteAccess, Long> {
    List<RemoteAccess> findByClientId(Long clientId);
}
