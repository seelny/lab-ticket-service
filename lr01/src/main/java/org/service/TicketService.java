package org.service;

import org.model.DomainException;
import org.model.Priority;
import org.model.Status;
import org.model.Ticket;
import org.repository.TicketRepository;

import java.util.List;
import java.util.Optional;

public class TicketService {
    private final TicketRepository ticketRepository;
    private TicketPicker ticketPicker;

    public void setTicketPicker(TicketPicker ticketPicker) {
        this.ticketPicker = ticketPicker;
    }

    public TicketService(TicketRepository ticketRepository, TicketPicker ticketPicker) {
        this.ticketRepository = ticketRepository;
        this.ticketPicker = ticketPicker;
    }

    public List<Ticket> findAllTickets(){
        return ticketRepository.findAll().stream().toList();
    }

    public Optional<Ticket> suggestNext() {
        return ticketPicker.pickNextTicket(ticketRepository.findAll());
    }

    public void startProgress(Long ticketId){
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new DomainException("Заявка с id " + ticketId + " не найдена"));
        ticket.start();
        ticketRepository.update(ticket);
    }

    public void completeTicket(Long ticketId){
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new DomainException("Заявка с id " + ticketId + " не найдена"));
        ticket.complete();
        ticketRepository.update(ticket);
    }

    public Ticket createTicket(String title, Priority priority, String assignee) {
        return ticketRepository.save(new Ticket(title, priority, assignee));
    }

    public Ticket createTicket(String title, Priority priority) {
        return ticketRepository.save(new Ticket(title, priority));
    }

    public List<Ticket> listByStatus(Status status){
        return ticketRepository.findAll().stream().filter(ticket -> ticket.getStatus() == status).toList();
    }

    public void assign(Long ticketId, String assignee){
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new DomainException("Заявка с id " + ticketId + " не найдена"));
        ticket.assign(assignee);
        ticketRepository.update(ticket);
    }
}