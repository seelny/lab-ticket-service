//package org;
//
//import org.facade.TicketAppFacade;
//import org.model.DomainException;
//import org.model.Priority;
//import org.model.Ticket;
//import org.repository.InMemoryTicketRepository;
//import org.repository.TicketRepository;
//import org.service.FifoPicker;
//import org.service.TicketService;
//
//import java.util.List;
//import java.util.Scanner;
//
//public class Main {
//
//    public static void main(String[] args) {
//        TicketRepository repository = new InMemoryTicketRepository();
//        TicketService service = new TicketService(repository, new FifoPicker());
//        TicketAppFacade facade = new TicketAppFacade(service);
//
//        Scanner scanner = new Scanner(System.in);
//        boolean running = true;
//
//        while (running) {
//            printMenu();
//            System.out.print("Выберите действие: ");
//            String choice = scanner.nextLine().trim();
//
//            try {
//                switch (choice) {
//                    case "1" -> handleAdd(scanner, facade);
//                    case "2" -> handleList(facade);
//                    case "3" -> handleNext(facade);
//                    case "4" -> handleDone(scanner, facade);
//                    case "5" -> handleStrategy(scanner, facade);
//                    case "0" -> {
//                        System.out.println("Завершение работы");
//                        running = false;
//                    }
//                    default -> System.out.println("Неизвестный пункт меню. Повторите ввод.");
//                }
//            } catch (DomainException e) {
//                System.out.println("Ошибка бизнес-логики: " + e.getMessage());
//            } catch (Exception e) {
//                System.out.println("Произошла непредвиденная ошибка: " + e.getMessage());
//            }
//            System.out.println();
//        }
//    }
//
//    private static void printMenu() {
//        System.out.println("1) Создать заявку");
//        System.out.println("2) Список всех заявок");
//        System.out.println("3) Взять следующую в работу");
//        System.out.println("4) Завершить заявку");
//        System.out.println("5) Сменить стратегию");
//        System.out.println("0) Выход");
//    }
//
//    private static void handleAdd(Scanner scanner, TicketAppFacade facade) {
//        System.out.print("Введите название заявки: ");
//        String title = scanner.nextLine();
//
//        System.out.print("Приоритет (LOW, MEDIUM, HIGH): ");
//        String priorityInput = scanner.nextLine().trim().toUpperCase();
//        Priority priority = Priority.MEDIUM;
//        if (!priorityInput.isBlank()) {
//            try {
//                priority = Priority.valueOf(priorityInput);
//            } catch (IllegalArgumentException e) {
//                System.out.println("Неизвестный приоритет. Установлен средний.");
//            }
//        }
//
//        System.out.print("Исполнитель (нажмите Enter для пропуска): ");
//        String assignee = scanner.nextLine().trim();
//
//        if (assignee.isBlank()) {
//            facade.addTicket(title, priority);
//        } else {
//            facade.addTicket(title, priority, assignee);
//        }
//        System.out.println("Заявка успешно добавлена");
//    }
//
//    private static void handleList(TicketAppFacade facade) {
//        List<Ticket> tickets = facade.showTickets();
//        if (tickets.isEmpty()) {
//            System.out.println("Список заявок пуст");
//            return;
//        }
//        System.out.println("Текущие заявки:");
//        tickets.forEach(System.out::println);
//    }
//
//    private static void handleNext(TicketAppFacade facade) {
//        Ticket ticket = facade.nextTicket();
//        System.out.println("Заявка взята в работу: " + ticket);
//    }
//
//    private static void handleDone(Scanner scanner, TicketAppFacade facade) {
//        System.out.print("Введите ID заявки для завершения: ");
//        String idInput = scanner.nextLine().trim();
//        try {
//            long id = Long.parseLong(idInput);
//            facade.doneTicket(id);
//            System.out.println("Заявка" + id + " успешно завершена.");
//        } catch (NumberFormatException e) {
//            System.out.println("ID должен быть целым числом.");
//        }
//    }
//
//    private static void handleStrategy(Scanner scanner, TicketAppFacade facade) {
//        System.out.print("Введите стратегию (FIFO / PRIORITY): ");
//        String strategy = scanner.nextLine();
//        facade.strategyUpdate(strategy);
//        System.out.println("Стратегия успешно переключена на: " + strategy.trim().toUpperCase());
//    }
//}




package org;

import org.analytics.ReportComposer;
import org.analytics.TicketReports;
import org.dto.TicketLoader;
import org.dto.TicketOperations;
import org.model.Priority;
import org.model.Ticket;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Ticket> tickets = TicketLoader.loadTickets();
        System.out.println("Загружено заявок: " + tickets.size() + "\n");


        if (!tickets.isEmpty()) {
            Ticket first = tickets.getFirst();
            System.out.println("Первая заявка: " + TicketOperations.isOpen(first));
            System.out.println("Вес приоритета HIGH: " + TicketOperations.priorityWeight(Priority.HIGH));

            TicketOperations ops = new TicketOperations();
            Ticket reassigned = ops.withAssignee(first, "new_assignee");
            System.out.println("Исходный исполнитель: " + first.getAssignee());
            System.out.println("Новый объект с исполнителем: " + reassigned.getAssignee());
        }
        System.out.println();


        System.out.println("Задание 2");
        TicketReports reports = new TicketReports();

        System.out.println("Открытые HIGH:");
        reports.openHighTickets(tickets).forEach(t ->
                System.out.println("ID " + t.getId() + " [" + t.getCreatedAt() + "]: " + t.getTitle())
        );

        System.out.printf("Среднее время закрытых: %.2f ч.%n", reports.averageProcessingTime(tickets));

        System.out.println("Счет заявок по статусам:");
        reports.groupByStatus(tickets).forEach((status, count) ->
                System.out.println(status + ": " + count)
        );

        System.out.println("Топ-3 исполнителей: " + reports.top3Assignee(tickets));
        System.out.println("Исполнители: " + reports.uniqueAssignees(tickets));
        System.out.println();




        ReportComposer composer = new ReportComposer();

        System.out.println("Отчёт filterOpen + filterHigh:");
        List<Ticket> openHigh = ReportComposer.runReport(
                tickets,
                ReportComposer.filterOpen,
                ReportComposer.filterHigh
        );
        System.out.println("Найдено заявок: " + openHigh.size());

        System.out.println("Отчёт исполнители с высоким приоритетом:");
        System.out.println(composer.filterHighPriorityAssignees(tickets));
    }
}