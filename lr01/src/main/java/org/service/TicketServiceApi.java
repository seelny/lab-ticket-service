package org.service;

import org.model.Priority;
import org.model.Status;
import org.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketServiceApi {
    Ticket createTicket(String title, Priority priority, String assignee);
    Ticket createTicket(String title, Priority priority);
    void startProgress(Long ticketId);
    void completeTicket(Long ticketId);
    void assign(Long ticketId, String assignee);
    List<Ticket> listByStatus(Status status);
    List<Ticket> findAllTickets();
    Optional<Ticket> suggestNext();
    void setTicketPicker(TicketPicker ticketPicker);
    void dumpMetrics();
}
