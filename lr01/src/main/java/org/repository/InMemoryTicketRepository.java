package org.repository;

import org.model.DomainException;
import org.model.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTicketRepository implements TicketRepository {
    private final Map<Long, Ticket> ticketMap;
    private final AtomicLong idGenerator;

    public InMemoryTicketRepository(){
        this.ticketMap = new ConcurrentHashMap<Long, Ticket>();
        this.idGenerator = new AtomicLong(0);
    }

    @Override
    public Ticket save(Ticket ticket) {
        if(ticket.getId() == null) {
            ticket.setId(idGenerator.incrementAndGet());
            //ticketMap.put(ticket.getId(), ticket);
        }
        ticketMap.put(ticket.getId(), ticket);
        return ticket;
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        //Optional.ofNullable(ticketMap.get(id));
        return Optional.ofNullable(ticketMap.get(id));
    }

    @Override
    public List<Ticket> findAll(){
        return new ArrayList<>(ticketMap.values());
    }

    @Override
    public boolean delete(Long id){
        boolean notEmpty = (ticketMap.remove(id) != null);
        return notEmpty;
    }

    @Override
    public void update(Ticket ticket){
        if (ticket.getId() == null || !ticketMap.containsKey(ticket.getId())){
            throw new DomainException("Заявки не существует");
        }
        else {
            ticketMap.put(ticket.getId(), ticket);
        }
    }
}
