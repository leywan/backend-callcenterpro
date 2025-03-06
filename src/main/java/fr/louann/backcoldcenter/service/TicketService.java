package fr.louann.backcoldcenter.service;

import fr.louann.backcoldcenter.models.Client;
import fr.louann.backcoldcenter.models.Ticket;
import fr.louann.backcoldcenter.repository.ClientRepository;
import fr.louann.backcoldcenter.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ClientRepository clientRepository;

    public Ticket createTicket(Ticket ticket, Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé"));
        ticket.setClient(client); // Associer le client
        return ticketRepository.save(ticket);
    }

    public Page<Ticket> getAllTickets(Pageable pageable) {
        // La méthode findAll est maintenant annotée avec @EntityGraph dans le repository
        return ticketRepository.findAll(pageable);
    }

    public Ticket getTicketWithDetails(Long ticketId) {
        return ticketRepository.findByIdWithClientAndRemoteAccess(ticketId).orElse(null);
    }

}
