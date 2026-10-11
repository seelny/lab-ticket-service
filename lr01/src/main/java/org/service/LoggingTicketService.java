package org.service;

import org.model.Priority;
import org.model.Status;
import org.model.Ticket;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class LoggingTicketService implements TicketServiceApi {
    private final TicketServiceApi delegate;

    public LoggingTicketService(TicketServiceApi delegate) {
        this.delegate = delegate;
    }

    private void logBefore(String title, String args) {
        writeLog("INFO", String.format("%s(%s)", title, args));
    }

    private void logSuccess(String title, long elapsedMs) {
        writeLog("INFO", String.format("%s ok %dms", title, elapsedMs));
    }

    private void logError(String title, String args, String errorMessage) {
        writeLog("ERROR", String.format("%s %s error=%s", title, args, errorMessage));
    }

    private void writeLog(String level, String message) {
        String logLine = String.format("%s %s %s", Instant.now(), level, message);
        System.out.println(logLine);
        try {
            Files.writeString(
                    Path.of("service.log"),
                    logLine + System.lineSeparator(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.err.println("Не удалось записать лог: " + e.getMessage());
        }
    }

    @Override
    public Ticket createTicket(String title, Priority priority, String assignee) {
        String args = String.format("title=\"%s\", priority=%s, assignee=\"%s\"", title, priority, assignee);
        logBefore("createTicket", args);
        long start = System.nanoTime();
        try {
            Ticket ticket = delegate.createTicket(title, priority, assignee);
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            logSuccess("createTicket", elapsedMs);
            return ticket;
        } catch (Exception e) {
            logError("createTicket", args, e.getMessage());
            throw e;
        }
    }

    @Override
    public Ticket createTicket(String title, Priority priority) {
        String args = String.format("title=\"%s\", priority=%s", title, priority);
        logBefore("createTicket", args);
        long start = System.nanoTime();
        try {
            Ticket ticket = delegate.createTicket(title, priority);
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            logSuccess("createTicket", elapsedMs);
            return ticket;
        } catch (Exception e) {
            logError("createTicket", args, e.getMessage());
            throw e;
        }
    }

    @Override
    public void startProgress(Long ticketId) {
        String args = "id=" + ticketId;
        logBefore("startProgress", args);
        long start = System.nanoTime();
        try {
            delegate.startProgress(ticketId);
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            logSuccess("startProgress", elapsedMs);
        } catch (Exception e) {
            logError("startProgress", args, e.getMessage());
            throw e;
        }
    }

    @Override
    public void completeTicket(Long ticketId) {
        String args = "id=" + ticketId;
        logBefore("completeTicket", args);
        long start = System.nanoTime();
        try {
            delegate.completeTicket(ticketId);
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            logSuccess("completeTicket", elapsedMs);
        } catch (Exception e) {
            logError("completeTicket", args, e.getMessage());
            throw e;
        }
    }

    @Override
    public void assign(Long ticketId, String assignee) {
        String args = String.format("id=%d, assignee=\"%s\"", ticketId, assignee);
        logBefore("assign", args);
        long start = System.nanoTime();
        try {
            delegate.assign(ticketId, assignee);
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            logSuccess("assign", elapsedMs);
        } catch (Exception e) {
            logError("assign", args, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Ticket> listByStatus(Status status) {
        String args = "status=" + status;
        logBefore("listByStatus", args);
        long start = System.nanoTime();
        try {
            List<Ticket> list = delegate.listByStatus(status);
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            logSuccess("listByStatus", elapsedMs);
            return list;
        } catch (Exception e) {
            logError("listByStatus", args, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Ticket> findAllTickets() {
        logBefore("findAllTickets", "");
        long start = System.nanoTime();
        try {
            List<Ticket> list = delegate.findAllTickets();
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            logSuccess("findAllTickets", elapsedMs);
            return list;
        } catch (Exception e) {
            logError("findAllTickets", "", e.getMessage());
            throw e;
        }
    }

    @Override
    public Optional<Ticket> suggestNext() {
        logBefore("suggestNext", "");
        long start = System.nanoTime();
        try {
            Optional<Ticket> ticket = delegate.suggestNext();
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            logSuccess("suggestNext", elapsedMs);
            return ticket;
        } catch (Exception e) {
            logError("suggestNext", "", e.getMessage());
            throw e;
        }
    }

    @Override
    public void setTicketPicker(TicketPicker ticketPicker) {
        delegate.setTicketPicker(ticketPicker);
    }

    @Override
    public void dumpMetrics(){
        logBefore("dumpMetrics", "");
        long start = System.nanoTime();
        try {
            delegate.dumpMetrics();
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            logSuccess("dumpMetrics", elapsedMs);
        } catch (Exception e) {
            logError("dumpMetrics", "", e.getMessage());
            throw e;
        }
    }
}