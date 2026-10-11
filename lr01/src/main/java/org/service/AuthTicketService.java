package org.service;

import org.model.Priority;
import org.model.Role;
import org.model.Status;
import org.model.Ticket;

import java.util.List;
import java.util.Optional;

public class AuthTicketService implements TicketServiceApi {
    private final TicketServiceApi ticketServiceApi;
    private final SecurityContext securityContext;

    public AuthTicketService(TicketServiceApi ticketServiceApi, SecurityContext securityContext) {
        this.ticketServiceApi = ticketServiceApi;
        this.securityContext = securityContext;
    }

    private void checkAdmin() {
        if (securityContext.getRole() != Role.ADMIN) {
            throw new SecurityException("Forbidden: ADMIN required");
        }
    }

    @Override
    public void completeTicket(Long ticketId) {
        checkAdmin();
        ticketServiceApi.completeTicket(ticketId);
    }

    @Override
    public void dumpMetrics() {
        checkAdmin();
        ticketServiceApi.dumpMetrics();
    }

    @Override
    public Ticket createTicket(String title, Priority priority, String assignee) {
        return ticketServiceApi.createTicket(title, priority, assignee);
    }

    @Override
    public Ticket createTicket(String title, Priority priority) {
        return ticketServiceApi.createTicket(title, priority);
    }

    @Override
    public void startProgress(Long ticketId) {
        ticketServiceApi.startProgress(ticketId);
    }

    @Override
    public void assign(Long ticketId, String assignee) {
        ticketServiceApi.assign(ticketId, assignee);
    }

    @Override
    public List<Ticket> listByStatus(Status status) {
        return ticketServiceApi.listByStatus(status);
    }

    @Override
    public List<Ticket> findAllTickets() {
        return ticketServiceApi.findAllTickets();
    }

    @Override
    public Optional<Ticket> suggestNext() {
        return ticketServiceApi.suggestNext();
    }

    @Override
    public void setTicketPicker(TicketPicker ticketPicker) {
        ticketServiceApi.setTicketPicker(ticketPicker);
    }
}