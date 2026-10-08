package org.model;

import java.time.LocalDateTime;

public class Ticket {
    private Long id;
    private final String title;
    private final Priority priority;
    private Status status;
    private String assignee;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Ticket(String title, Priority priority, String assignee) {
        if (title == null || title.isBlank()) {
            throw new DomainException("Название заявки пустое");
        }
        this.title = title;
        this.priority = priority != null ? priority : Priority.MEDIUM;
        this.assignee = assignee != null ? assignee : "БЕЗ ИСПОЛНИТЕЛЯ";
        this.status = Status.NEW;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public Ticket(String title, Priority priority) {
        this(title, priority, null);
    }



    public Ticket(Long id, String title, Priority priority, Status status, String assignee, LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (title == null || title.isBlank()) {
            throw new DomainException("Название заявки пустое");
        }
        this.id = id;
        this.title = title;
        this.priority = priority != null ? priority : Priority.MEDIUM;
        this.status = status != null ? status : Status.NEW;
        this.assignee = assignee != null ? assignee : "БЕЗ ИСПОЛНИТЕЛЯ";
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : this.createdAt;
    }




    public void setId(Long id) {
        if (this.id != null) {
            throw new DomainException("ID заявки уже установлен");
        }
        this.id = id;
    }
    public void start() {
        if (this.status != Status.NEW) {
            throw new DomainException(
                    String.format("Нельзя взять в работу заявку #%d: текущий статус %s.", id, status)
            );
        }
        this.status = Status.IN_PROGRESS;
        this.updatedAt = LocalDateTime.now();
    }

    public void complete() {
        if (this.status != Status.IN_PROGRESS) {
            throw new DomainException(
                    String.format("Нельзя завершить заявку #%d: текущий статус %s", id, status)
            );
        }
        this.status = Status.DONE;
        this.updatedAt = LocalDateTime.now();
    }

    public void assign(String assignee) {
        if (this.status == Status.DONE) {
            throw new DomainException(
                    String.format("Нельзя назначить исполнителя для закрытой заявки #%d", id)
            );
        }
        if (assignee == null || assignee.isBlank()) {
            throw new DomainException("Имя исполнителя пустое");
        }

        this.assignee = assignee;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Priority getPriority() { return priority; }
    public Status getStatus() { return status; }
    public String getAssignee() { return assignee; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @Override
    public String toString() {
        return String.format("[%d] %s | Приоритет: %s | Статус: %s | Исполнитель: %s | Создана: %s | Обновлена: %s",
                id, title, priority, status, (assignee != null ? assignee : "—"), createdAt, updatedAt);
    }
}