package fr.louann.backcoldcenter.repository;

import fr.louann.backcoldcenter.models.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // Cette méthode sera utilisée pour charger les tickets avec leurs associations (client, company, remote_access)
    @Override
    @EntityGraph(attributePaths = {"client", "client.company"})
    Page<Ticket> findAll(Pageable pageable);


    @Query("SELECT t FROM Ticket t " +
            "LEFT JOIN FETCH t.client " +
            "LEFT JOIN FETCH t.client.company " +
            "WHERE t.client.company.id = :companyId")
    List<Ticket> findByCompanyIdWithDetails(@Param("companyId") Long companyId);

    @Query("""
    SELECT t
    FROM Ticket t
    LEFT JOIN FETCH t.client c
    LEFT JOIN FETCH c.remoteAccess ra
    WHERE t.id = :ticketId
""")
    Optional<Ticket> findByIdWithClientAndRemoteAccess(@Param("ticketId") Long ticketId);


}
