package com.example.backend.mapper;

import com.example.backend.dto.EventDto;
import com.example.backend.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper implements AbstractDefault<EventDto, Event> {
    @Override
    public EventDto mapToDto(Event entity) {
        return EventDto.builder()
                .eventId(entity.getEventId())
                .eventName(entity.getEventName())
                .eventLabel(entity.getEventLabel())
                .eventValue(entity.getEventValue())
                .build();
    }

    @Override
    public Event mapToEntity(EventDto entity) {
        return Event.builder()
                .eventId(entity.getEventId())
                .eventName(entity.getEventName())
                .eventLabel(entity.getEventLabel())
                .eventValue(entity.getEventValue())
                .build();
    }
}
