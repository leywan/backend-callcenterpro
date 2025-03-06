package fr.louann.backcoldcenter.repository;

import fr.louann.backcoldcenter.models.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByName(String name);

    // Charger uniquement les clients sans remoteAccess pour les listes
    @Query(value = """
    SELECT DISTINCT c
    FROM Client c
    LEFT JOIN FETCH c.company comp
    WHERE (:companyId IS NULL OR c.company.id = :companyId)
    """,
            countQuery = """
    SELECT COUNT(c)
    FROM Client c
    WHERE (:companyId IS NULL OR c.company.id = :companyId)
    """
    )
    Page<Client> findAllWithoutRemoteAccess(@Param("companyId") Long companyId, Pageable pageable);

    // Charger un client avec remoteAccess pour la fiche détaillée
    @Query("SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.remoteAccess WHERE c.id = :id")
    Optional<Client> findByIdWithRemoteAccess(@Param("id") Long id);

    // (Optionnel) Pour l'export CSV, si nécessaire
    @Query("SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.remoteAccess WHERE c.company.id = :companyId")
    List<Client> findClientsWithRemoteAccessOptimized(@Param("companyId") Long companyId);

    @Query("SELECT DISTINCT c FROM Client c " +
            "LEFT JOIN FETCH c.company comp " +
            "LEFT JOIN FETCH c.remoteAccess ra " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "   OR LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Client> searchReactive(@Param("query") String query);

}
