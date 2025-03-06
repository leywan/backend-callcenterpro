package fr.louann.backcoldcenter.controller;

import fr.louann.backcoldcenter.models.Ticket;
import fr.louann.backcoldcenter.models.TicketRequest;
import fr.louann.backcoldcenter.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody TicketRequest ticketRequest) {
        Ticket newTicket = ticketService.createTicket(ticketRequest.toTicket(), ticketRequest.getClientId());
        return ResponseEntity.status(HttpStatus.CREATED).body(newTicket);
    }

    @GetMapping
    public ResponseEntity<Page<Ticket>> getAllTickets(Pageable pageable) {
        return ResponseEntity.ok(ticketService.getAllTickets(pageable));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<Ticket> getTicketDetails(@PathVariable Long id) {
        // On appelle un service qui fait un findByIdWithDetails
        Ticket ticket = ticketService.getTicketWithDetails(id);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ticket);
    }


}

