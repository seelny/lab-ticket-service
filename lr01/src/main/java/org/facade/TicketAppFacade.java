package org.facade;

import org.model.DomainException;
import org.model.Priority;
import org.model.Ticket;
import org.service.FifoPicker;
import org.service.HighestPriorityPicker;
import org.service.TicketService;

import java.util.List;

public class TicketAppFacade {
    private final TicketService ticketService;

    public TicketAppFacade(TicketService ticketService){
        this.ticketService = ticketService;
    }

    public void addTicket(String title, Priority priority, String assignee){
        ticketService.createTicket(title, priority, assignee);
    }

    public void addTicket(String title, Priority priority){
        ticketService.createTicket(title, priority);
    }

    public List<Ticket> showTickets(){
        return ticketService.findAllTickets();
    }

    public Ticket nextTicket(){
        Ticket ticket = ticketService.suggestNext().orElseThrow(() -> new DomainException("Нет подходящих заявок для работы"));
        ticketService.startProgress(ticket.getId());
        return ticket;
    }

    public void doneTicket(long ticketId){
        ticketService.completeTicket(ticketId);
    }

    public void strategyUpdate(String strategy) {
        if (strategy == null || strategy.isBlank()) {
            throw new DomainException("Стратегия не указана");
        }

        String normalized = strategy.trim().toUpperCase();

        switch (normalized) {
            case "FIFO":
                ticketService.setTicketPicker(new FifoPicker());
                break;
            case "PRIORITY":
                ticketService.setTicketPicker(new HighestPriorityPicker());
                break;
            default:
                throw new DomainException("Неизвестная стратегия: " + strategy);
        }
    }
}
