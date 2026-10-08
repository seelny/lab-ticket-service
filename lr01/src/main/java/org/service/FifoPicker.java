package org.service;

import org.model.Status;
import org.model.Ticket;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class FifoPicker implements TicketPicker {

    @Override
    public Optional<Ticket> pickNextTicket(List<Ticket> tickets){
        //Comparator.comparing(Ticket::getCreatedAt); //ссылка
        return tickets.stream().filter(ticket -> ticket.getStatus() == Status.NEW || ticket.getStatus() == Status.IN_PROGRESS)
                .min(Comparator.comparing(Ticket::getCreatedAt));
    }
}
