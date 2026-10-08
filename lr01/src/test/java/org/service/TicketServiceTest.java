package org.service;

import org.junit.jupiter.api.Test;
import org.model.DomainException;
import org.model.Priority;
import org.model.Status;
import org.model.Ticket;
import org.repository.InMemoryTicketRepository;

import static org.junit.jupiter.api.Assertions.*;

public class TicketServiceTest {

    @Test
    public void createTicketTest(){
        InMemoryTicketRepository repository = new InMemoryTicketRepository();
        TicketService service = new TicketService(repository, new FifoPicker());

        Ticket created = service.createTicket("test", Priority.HIGH, "Андрей");

        assertNotNull(created.getId(), "ID должен быть назначен");
        assertEquals(Status.NEW, created.getStatus(), "Статус должен быть NEW");
        assertNotNull(created.getCreatedAt(), "Время создания должно быть определено");
        assertNotNull(created.getUpdatedAt(), "Время обновления должно быть определено");
        assertEquals(created.getCreatedAt(), created.getUpdatedAt(), "Время обновления должно быть равно времени создания");
    }

    @Test
    public void startProgressTest(){
        InMemoryTicketRepository repository = new InMemoryTicketRepository();
        TicketService service = new TicketService(repository, new FifoPicker());
        Ticket created = service.createTicket("test", Priority.HIGH, "Андрей");

        service.startProgress(created.getId());

        Ticket updated = repository.findById(created.getId()).orElseThrow();
        assertEquals(Status.IN_PROGRESS, updated.getStatus(),"Статус должен быть IN_PROGRESS");
    }

    @Test
    public void completeTicketTest(){
        InMemoryTicketRepository repository = new InMemoryTicketRepository();
        TicketService service = new TicketService(repository, new FifoPicker());
        Ticket created = service.createTicket("test", Priority.HIGH, "Андрей");
        service.startProgress(created.getId());

        service.completeTicket(created.getId());

        Ticket updated = repository.findById(created.getId()).orElseThrow();
        assertEquals(Status.DONE, updated.getStatus(),"Статус должен быть DONE");
    }

    @Test
    public void startProgressWhenDoneTest(){
        InMemoryTicketRepository repository = new InMemoryTicketRepository();
        TicketService service = new TicketService(repository, new FifoPicker());
        Ticket created = service.createTicket("test", Priority.HIGH, "Андрей");
        service.startProgress(created.getId());
        service.completeTicket(created.getId());

        assertThrows(DomainException.class, () -> {
            service.startProgress(created.getId());
        }, "Должен объявиться DomainException");
    }

    @Test
    public void completeTicketWhenNewTest(){
        InMemoryTicketRepository repository = new InMemoryTicketRepository();
        TicketService service = new TicketService(repository, new FifoPicker());
        Ticket created = service.createTicket("test", Priority.HIGH, "Андрей");

        assertThrows(DomainException.class, () -> {
            service.completeTicket(created.getId());
        }, "Должен объявиться DomainException");
    }

    @Test
    public void assignTest(){
        InMemoryTicketRepository repository = new InMemoryTicketRepository();
        TicketService service = new TicketService(repository, new FifoPicker());
        Ticket created = service.createTicket("test", Priority.HIGH);

        service.assign(created.getId(), "Андрей");

        Ticket updated = repository.findById(created.getId()).orElseThrow();
        assertEquals("Андрей", updated.getAssignee(), "Исполнителем должен стать Андрей");
        assertEquals(Status.NEW, updated.getStatus(), "Статус должен остаться NEW");
    }
}
