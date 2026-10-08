package org.dto;

import org.junit.jupiter.api.Test;
import org.model.Priority;
import org.model.Ticket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketOperationsTest {
    @Test
    public void withAssigneeTest(){
        TicketOperations ticketOperations = new TicketOperations();
        Ticket ticket = new Ticket("test", Priority.HIGH, "Андрей");

        Ticket ticket1 = TicketOperations.withAssignee(ticket, "Аркадий");

        assertEquals("Андрей", ticket.getAssignee(), "Исполнителем должен быть Андрей");
        assertEquals("Аркадий", ticket1.getAssignee(), "Исполнителем должен быть Аркадий");
        assertEquals(ticket1.getCreatedAt(), ticket.getCreatedAt(), "Время создания должно остаться прежним");

    }
}
