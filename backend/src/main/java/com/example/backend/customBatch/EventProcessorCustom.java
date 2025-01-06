package com.example.backend.customBatch;

import com.example.backend.entity.Event;
import com.example.backend.enums.DimensionType;
import com.example.backend.enums.ErrorCode;
import com.example.backend.enums.GoogleAnalyticEnumKeyWord;
import com.example.backend.enums.MetricType;
import com.example.backend.exception.ApiException;
import com.example.backend.service.GoogleAnalyticService.GoogleAnalyticService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventProcessorCustom implements ItemProcessor<List<Event>,List<Event>> {
    private final GoogleAnalyticService googleAnalyticService;
    private void checkEventExist(List<Event> ga4Event, List<Event> dbEvents, List<Event> events){
        for(Event gaEvent: ga4Event){
            boolean isCheck= false;
            for(Event dbEvent: dbEvents){
                if(gaEvent.getEventLabel().equals(dbEvent.getEventLabel()) &&
                        gaEvent.getEventName().equals(dbEvent.getEventName()) &&
                        gaEvent.getTimestamp().getDayOfMonth() == dbEvent.getTimestamp().getDayOfMonth() &&
                        gaEvent.getTimestamp().getMonth() == dbEvent.getTimestamp().getMonth() &&
                        gaEvent.getTimestamp().getYear() == dbEvent.getTimestamp().getYear() &&
                        !gaEvent.getEventValue().equals(dbEvent.getEventValue())
                ){
                    isCheck= true;
                    dbEvent.setEventValue(gaEvent.getEventValue());
                    events.add(dbEvent);
                    break;
                }
            }
            if (!isCheck){
                events.add(gaEvent);
            }
        }
    }
    @Override
    public List<Event> process(@NotNull List<Event> item) {
        try{
            List<Event> events = new ArrayList<>();
            List<Event> getEventNames= googleAnalyticService.getReportFromGA4MapToEvent(
                    DimensionType.EventName.getName(),
                    MetricType.EventCount.getName(),
                    GoogleAnalyticEnumKeyWord.PROPERTIES_DEFAULT.getName()
            );
            List<Event> getCity= googleAnalyticService.getReportFromGA4MapToEvent(
                    DimensionType.City.getName(),
                    MetricType.ActiveUser.getName(),
                    GoogleAnalyticEnumKeyWord.PROPERTIES_DEFAULT.getName()
            );
            checkEventExist(getEventNames, item, events);
            checkEventExist(getCity, item, events);
            return events;
        }catch (Exception e){
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR.getStatusCode().value(), e.getMessage());
        }
    }
}
