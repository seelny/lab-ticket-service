package org.service;

import org.junit.jupiter.api.Test;
import org.model.DomainException;
import org.model.Priority;
import org.model.Status;
import org.model.Ticket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TicketPickerTest {
    private final Ticket ticket1 = new Ticket(
            1L, "Задача 1", Priority.MEDIUM, Status.NEW, "-",
            LocalDateTime.parse("2026-09-01T10:00:00"),
            LocalDateTime.parse("2026-09-01T10:00:00")
    );

    private final Ticket ticket2 = new Ticket(
            2L, "Задача 2", Priority.HIGH, Status.NEW, "-",
            LocalDateTime.parse("2026-09-01T12:00:00"),
            LocalDateTime.parse("2026-09-01T12:00:00")
    );

    private final Ticket ticket3 = new Ticket(
            3L, "Задача 3", Priority.HIGH, Status.NEW, "-",
            LocalDateTime.parse("2026-09-01T11:00:00"),
            LocalDateTime.parse("2026-09-01T11:00:00")
    );

    private final Ticket ticket4 = new Ticket(
            4L, "Задача 4", Priority.LOW, Status.IN_PROGRESS, "-",
            LocalDateTime.parse("2026-09-01T09:00:00"),
            LocalDateTime.parse("2026-09-01T09:00:00")
    );

    private final Ticket ticket5 = new Ticket(
            5L, "Задача 5", Priority.HIGH, Status.DONE, "-",
            LocalDateTime.parse("2026-09-01T08:00:00"),
            LocalDateTime.parse("2026-09-01T08:00:00")
    );


    @Test
    public void pickNextTicketHPPTest(){
        List<Ticket> list = List.of(ticket1, ticket2, ticket3, ticket4);
        HighestPriorityPicker picker = new HighestPriorityPicker();
        Optional<Ticket> ans = Optional.of(ticket3);

        Optional<Ticket> ticketOpt = picker.pickNextTicket(list);

        assertEquals(ans, ticketOpt, "Должен быть выбран ticket3");
    }

    @Test
    public void pickNextTicketFIFOTest(){
        List<Ticket> list = List.of(ticket1, ticket2, ticket3, ticket4);
        FifoPicker picker = new FifoPicker();
        Optional<Ticket> ans = Optional.of(ticket4);

        Optional<Ticket> ticketOpt = picker.pickNextTicket(list);

        assertEquals(ans, ticketOpt, "Должен быть выбран ticket4");
    }

    @Test
    public void pickNextTicketHPPTimeTest(){
        List<Ticket> list = List.of(ticket2, ticket3);
        HighestPriorityPicker picker = new HighestPriorityPicker();
        Optional<Ticket> ans = Optional.of(ticket3);

        Optional<Ticket> ticketOpt = picker.pickNextTicket(list);

        assertEquals(ans, ticketOpt, "Должен быть выбран ticket3");
    }

    @Test
    public void pickNextTicketEmptyListTest(){
        List<Ticket> list = List.of();
        HighestPriorityPicker picker = new HighestPriorityPicker();

        Optional<Ticket> ticketOpt = picker.pickNextTicket(list);

        assertTrue(ticketOpt.isEmpty(), "Должно быть True");
    }

    @Test
    public void pickNextTicketAllDoneTest(){
        List<Ticket> list = List.of(ticket5);
        HighestPriorityPicker picker = new HighestPriorityPicker();

        Optional<Ticket> ticketOpt = picker.pickNextTicket(list);

        assertTrue(ticketOpt.isEmpty(), "Должно быть True");
    }
}
