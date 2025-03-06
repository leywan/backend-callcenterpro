package fr.louann.backcoldcenter.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketRequest {
    private Long clientId;
    private String callerName;
    private String callerPhone;
    private String description;
    private Priority priority;
    private String notes;

    public Ticket toTicket() {
        Ticket ticket = new Ticket();
        ticket.setCallerName(this.callerName);
        ticket.setCallerPhone(this.callerPhone);
        ticket.setDescription(this.description);
        ticket.setPriority(this.priority);
        ticket.setNotes(this.notes);
        return ticket;
    }
}

