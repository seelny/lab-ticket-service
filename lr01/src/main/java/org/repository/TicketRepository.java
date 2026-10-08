package org.repository;

import org.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    Ticket save(Ticket ticket);
    Optional<Ticket> findById(Long Id);
    List<Ticket> findAll();
    boolean delete(Long Id);
    void update(Ticket ticket);
}
