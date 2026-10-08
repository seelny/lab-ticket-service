package org.service;

import org.model.Status;
import org.model.Ticket;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class HighestPriorityPicker implements TicketPicker {

    @Override
    public Optional<Ticket> pickNextTicket(List<Ticket> tickets){
        return tickets.stream().filter(ticket -> ticket.getStatus() == Status.NEW || ticket.getStatus() == Status.IN_PROGRESS)
                .max(Comparator.comparing(Ticket::getPriority)
                .thenComparing(Ticket::getCreatedAt, Comparator.reverseOrder()));
    }
}
