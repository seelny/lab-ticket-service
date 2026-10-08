package org.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.model.DomainException;
import org.model.Ticket;

import java.io.InputStream;
import java.util.List;

public class TicketLoader {

    public static List<Ticket> loadTickets() {
        InputStream stream = ClassLoader.getSystemResourceAsStream("tickets.json");
        if (stream == null) {
            throw new DomainException("Файл tickets.json не найден в ресурсах");
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            List<TicketRecord> records = mapper.readValue(
                    stream,
                    new TypeReference<List<TicketRecord>>() {}
            );

            return records.stream()
                    .map(TicketRecord::toTicket)
                    .toList();

        } catch (Exception e) {
            throw new DomainException("Ошибка при чтении или парсинге tickets.json" + e.getMessage());
        }
    }
}
