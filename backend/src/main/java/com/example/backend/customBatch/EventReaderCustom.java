package com.example.backend.customBatch;

import com.example.backend.entity.Event;
import com.example.backend.repository.EventRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class EventReaderCustom implements ItemReader<List<Event>> {
    private final EventRepository eventRepository;
    private boolean readAlready = false;

    @Override
    public List<Event> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (!readAlready) {
            readAlready = true;
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime start = now.minusDays(3);
            return eventRepository.getALlEventsByTimestampBetween(start, now);
        } else {
            return null;
        }
    }
}
