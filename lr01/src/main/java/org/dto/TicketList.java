package org.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.model.DomainException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TicketList {
    public List<TicketRecord> ticketList(InputStream inputStream){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.readValue(
                    inputStream,
                    new TypeReference<List<TicketRecord>>() {
                    }
            );
        }
        catch (IOException ioException){
            throw new DomainException("Не удалось прочитать список заявок из JSON");
        }
    }
}
