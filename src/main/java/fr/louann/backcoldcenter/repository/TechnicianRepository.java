package fr.louann.backcoldcenter.repository;

import fr.louann.backcoldcenter.models.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician, Long> {
    // Laissez Spring Data construire la requête
    List<Technician> findByCompany_Id(Long companyId);
}
