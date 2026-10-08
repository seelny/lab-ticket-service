package org.analytics;

import org.dto.TicketOperations;
import org.model.Priority;
import org.model.Status;
import org.model.Ticket;

import java.time.Duration;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TicketReports {
    public List<Ticket> openHighTickets(List<Ticket> ticketList) {
        return ticketList.stream()
                .filter(ticket -> ticket.getPriority() == Priority.HIGH && TicketOperations.isOpen(ticket))
                .sorted(Comparator.comparing(Ticket::getCreatedAt))
                .toList();
    }

    public List<String> uniqueAssignees(List<Ticket> ticketList) {
        return ticketList.stream()
                .filter(ticket -> ticket.getAssignee() != null && !ticket.getAssignee().isBlank())
                .map(Ticket::getAssignee).distinct().sorted().toList();
    }

    public List<String> top3Assignee(List<Ticket> ticketList){
        Map<String, Long> countsByAssignee = ticketList.stream().filter(ticket -> ticket.getStatus() == Status.DONE
                && ticket.getAssignee() != null && !ticket.getAssignee().isBlank())
                .collect(Collectors.groupingBy(Ticket::getAssignee,Collectors.counting()));
        //List<Ticket> ticketsSorted = Collectors.groupingBy();
        return countsByAssignee.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(3).map(Map.Entry::getKey).toList();
    }

    public double averageProcessingTime(List<Ticket> ticketList) {
        return ticketList.stream()
                .filter(ticket -> ticket.getStatus() == Status.DONE)
                .mapToDouble(ticket -> Duration.between(ticket.getCreatedAt(), ticket.getUpdatedAt()).toMinutes() / 60.0)
                .average()
                .orElse(0.0);
    }

    public Map<Status, Long> groupByStatus(List<Ticket> ticketList){
        return ticketList.stream().collect(Collectors.groupingBy(Ticket::getStatus, Collectors.counting()));
    }

    public Map<Status, Long> groupByStatusImperative(List<Ticket> ticketList){
        Map<Status, Long> map = new HashMap<>();
        for (Ticket ticket : ticketList) {
            Status status = ticket.getStatus();
            if (!map.containsKey(status)){
                map.put(status, 1L);
            }
            else {
                Long currentCount = map.get(status) + 1L;
                map.put(status, currentCount);
            }
        }
        return map;
    }
}
