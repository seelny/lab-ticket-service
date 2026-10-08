package org.service;

import org.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketPicker {
    Optional<Ticket> pickNextTicket(List<Ticket> tickets);
}
