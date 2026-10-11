package org.service;

import org.model.Priority;
import org.model.Status;
import org.model.Ticket;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class TimingTicketService implements TicketServiceApi {
    private final TicketServiceApi delegate;
    private final Map<String, Long> methodCalls = new ConcurrentHashMap<>();

    public TimingTicketService(TicketServiceApi delegate) {
        this.delegate = delegate;
    }

    private void recordCall(String methodName) {
        methodCalls.put(methodName, methodCalls.getOrDefault(methodName, 0L) + 1L);
    }

    @Override
    public void dumpMetrics() {
        for (Map.Entry<String, Long> entry : methodCalls.entrySet()) {
            System.out.printf("%-20s %d%n", entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Ticket createTicket(String title, Priority priority, String assignee) {
        recordCall("createTicket");
        return delegate.createTicket(title, priority, assignee);
    }

    @Override
    public Ticket createTicket(String title, Priority priority) {
        recordCall("createTicket");
        return delegate.createTicket(title, priority);
    }

    @Override
    public void startProgress(Long ticketId) {
        recordCall("startProgress");
        delegate.startProgress(ticketId);
    }

    @Override
    public void completeTicket(Long ticketId) {
        recordCall("completeTicket");
        delegate.completeTicket(ticketId);
    }

    @Override
    public void assign(Long ticketId, String assignee) {
        recordCall("assign");
        delegate.assign(ticketId, assignee);
    }

    @Override
    public List<Ticket> listByStatus(Status status) {
        recordCall("listByStatus");
        return delegate.listByStatus(status);
    }

    @Override
    public List<Ticket> findAllTickets() {
        recordCall("findAllTickets");
        return delegate.findAllTickets();
    }

    @Override
    public Optional<Ticket> suggestNext() {
        recordCall("suggestNext");
        return delegate.suggestNext();
    }

    @Override
    public void setTicketPicker(TicketPicker ticketPicker) {
        recordCall("setTicketPicker");
        delegate.setTicketPicker(ticketPicker);
    }
}