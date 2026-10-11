package org.service;

import org.model.DomainException;
import org.model.Priority;
import org.model.Status;
import org.model.Ticket;
import org.repository.TicketRepository;

import java.util.List;
import java.util.Optional;

public class TicketService implements TicketServiceApi{
    private final TicketRepository ticketRepository;
    private TicketPicker ticketPicker;

    @Override
    public void setTicketPicker(TicketPicker ticketPicker) {
        this.ticketPicker = ticketPicker;
    }

    public TicketService(TicketRepository ticketRepository, TicketPicker ticketPicker) {
        this.ticketRepository = ticketRepository;
        this.ticketPicker = ticketPicker;
    }

    @Override
    public List<Ticket> findAllTickets(){
        return ticketRepository.findAll().stream().toList();
    }

    @Override
    public Optional<Ticket> suggestNext() {
        return ticketPicker.pickNextTicket(ticketRepository.findAll());
    }

    @Override
    public void startProgress(Long ticketId){
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new DomainException("Заявка с id " + ticketId + " не найдена"));
        ticket.start();
        ticketRepository.update(ticket);
    }

    @Override
    public void completeTicket(Long ticketId){
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new DomainException("Заявка с id " + ticketId + " не найдена"));
        ticket.complete();
        ticketRepository.update(ticket);
    }

    @Override
    public Ticket createTicket(String title, Priority priority, String assignee) {
        return ticketRepository.save(new Ticket(title, priority, assignee));
    }

    @Override
    public Ticket createTicket(String title, Priority priority) {
        return ticketRepository.save(new Ticket(title, priority));
    }

    @Override
    public List<Ticket> listByStatus(Status status){
        return ticketRepository.findAll().stream().filter(ticket -> ticket.getStatus() == status).toList();
    }

    @Override
    public void assign(Long ticketId, String assignee){
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new DomainException("Заявка с id " + ticketId + " не найдена"));
        ticket.assign(assignee);
        ticketRepository.update(ticket);
    }

    @Override
    public void dumpMetrics() {

    }
}