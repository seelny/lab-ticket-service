package org.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.model.Priority;
import org.model.Status;
import org.model.Ticket;

import java.time.LocalDateTime;

public record TicketRecord(Long id, String title, Priority priority, Status status, String assignee,
                           @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssX") LocalDateTime createdAt,
                           @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssX") LocalDateTime updatedAt) {
    public Ticket toTicket(){
        return new Ticket(this.id, this.title, this.priority, this.status, this.assignee, this.createdAt, this.updatedAt);
    }
}
