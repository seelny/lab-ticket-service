package org.analytics;

import org.dto.TicketOperations;
import org.model.Priority;
import org.model.Ticket;

import java.util.List;
import java.util.function.Function;

public class ReportComposer {
    public static final Function<List<Ticket>, List<Ticket>> filterOpen =
            tickets -> tickets.stream().filter(TicketOperations::isOpen).toList();

    public static final Function<List<Ticket>, List<Ticket>> filterHigh =
            tickets -> tickets.stream().filter(t -> t.getPriority() == Priority.HIGH).toList();

    public static final Function<List<Ticket>, List<String>> extractAssignees =
            tickets -> tickets.stream()
                    .map(Ticket::getAssignee).filter(a -> a != null && !a.isBlank()).distinct()
                    .sorted().toList();

    public List<Ticket> filterOpenHigh(List<Ticket> tickets) {
        Function<List<Ticket>, List<Ticket>> filterOpenHigh = filterOpen.andThen(filterHigh);
        return filterOpenHigh.apply(tickets);
    }

    public List<String> filterHighPriorityAssignees(List<Ticket> tickets) {
        Function<List<Ticket>, List<String>> filterHighPriorityAssignees = extractAssignees.compose(filterHigh);
        return filterHighPriorityAssignees.apply(tickets);
    }

    @SafeVarargs
    public static List<Ticket> runReport(
            List<Ticket> tickets,
            Function<List<Ticket>, List<Ticket>>... transformers) {
        List<Ticket> current = tickets;
        for (Function<List<Ticket>, List<Ticket>> transformer : transformers) {
            current = transformer.apply(current);
        }
        return current;
    }
}