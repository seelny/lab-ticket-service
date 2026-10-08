package org.dto;

import org.model.Priority;
import org.model.Status;
import org.model.Ticket;

public class TicketOperations {
    public static boolean isOpen(Ticket ticket){
        return ticket.getStatus() == Status.NEW || ticket.getStatus() == Status.IN_PROGRESS;
    }

    public static int priorityWeight(Priority priority) {
        int priorityWeight = 0;
        switch (priority) {
            case LOW:
                priorityWeight = 1;
                break;
            case MEDIUM:
                priorityWeight = 2;
                break;
            case HIGH:
                priorityWeight = 3;
                break;    

        }
        return priorityWeight;
    }

    public static Ticket withAssignee(Ticket ticket, String name){
        return new Ticket(ticket.getId(), ticket.getTitle(), ticket.getPriority(), ticket.getStatus(),
                name, ticket.getCreatedAt(), ticket.getUpdatedAt());
    }

}
