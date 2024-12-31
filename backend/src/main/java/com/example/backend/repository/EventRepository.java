package com.example.backend.repository;

import com.example.backend.dto.EventDto;
import com.example.backend.dto.response.NumberOfEventResponse;
import com.example.backend.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE e.timestamp = (SELECT MAX(e2.timestamp) " +
            "                     FROM Event e2 " +
            "                     WHERE e2.eventName = e.eventName AND e2.eventLabel = :eventLabel) " +
            "AND e.eventLabel = :eventLabel " +
            "ORDER BY e.timestamp DESC")
    Page<Event> findDistinctEventsByEventLabel(@Param("eventLabel") String eventLabel, Pageable pageable);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE e.timestamp = (SELECT MAX(e2.timestamp) " +
            "                     FROM Event e2 " +
            "                     WHERE e2.eventName = e.eventName AND e2.eventLabel = :eventLabel) " +
            "AND e.eventLabel = :eventLabel " +
            "AND e.timestamp BETWEEN :startDate AND :endDate " +
            "ORDER BY e.timestamp DESC")
    Page<Event> findDistinctEventsByEventLabelAndStartDateAndEndDate(
            @Param("eventLabel") String eventLabel,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Query("SELECT new com.example.backend.dto.response.NumberOfEventResponse(e.eventLabel, sum(cast(e.eventValue as INTEGER))) " +
            "FROM Event e " +
            "WHERE e.timestamp = (SELECT MAX(e2.timestamp) " +
            "                     FROM Event e2 " +
            "                     WHERE e2.eventName = e.eventName " +
            "                     AND e2.eventLabel = e.eventLabel" +
            "                     AND e2.timestamp between :startDate and :endDate) " +
            "group by e.eventLabel")
    List<NumberOfEventResponse> numberOfEventsByEventLabel(LocalDateTime startDate,LocalDateTime endDate);


}
