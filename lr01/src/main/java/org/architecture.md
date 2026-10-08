# Архитектура Ticket System

## 1. Сущности и Value Objects (Домен)
* **`Ticket`** - базовая сущность . Содержит поля: `id`, `title`, `priority`, `assignee`, `status`, `createdAt`, `updatedAt`. Защищает инварианты переходов статусов через методы `start()`, `complete()`, `assign()`.
* **`Status`** - перечисление: `NEW`, `IN_PROGRESS`, `DONE`.
* **`Priority`** - перечисление: `LOW`, `MEDIUM`, `HIGH`.
* **`DomainException`** - непроверяемое исключение для фиксации нарушений бизнес-правил.

## 2. Интерфейсы
* **`TicketRepository`** - контроль слоя доступа к данным:
    * `save(Ticket): Ticket`
    * `findById(Long): Optional<Ticket>`
    * `findAll(): List<Ticket>`
    * `update(Ticket): void`
* **`TicketPicker`** - стратегия выбора следующей заявки (паттерн Strategy):
    * `pickNextTicket(List<Ticket>): Optional<Ticket>`
    * Реализации: `FifoPicker`, `HighestPriorityPicker`.

## 3. Сервисы и Фасад
* **`TicketService`** - бизнес-логика (создание, перевод статусов, вызов стратегии).
* **`TicketAppFacade`** - единая высокоуровневая точка входа для интерфейса: изолирует CLI от доменных сервисов и репозиториев.