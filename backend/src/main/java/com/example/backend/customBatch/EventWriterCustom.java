package com.example.backend.customBatch;

import com.example.backend.entity.Event;
import com.example.backend.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EventWriterCustom implements ItemWriter<List<Event>> {
    private final EventRepository eventRepository;
    @Override
    public void write(@NotNull Chunk<? extends List<Event>> chunk) throws Exception {
        for(List<Event> events : chunk){
            eventRepository.saveAll(events);
        }
    }
}
