package com.example.backend.repository;

import com.example.backend.dto.EventDto;
import com.example.backend.dto.response.EventChartResponse;
import com.example.backend.dto.response.EventTableResponse;
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

    @Query("select new com.example.backend.dto.response.EventTableResponse(e.eventName,sum(cast(e.eventValue as Long))) " +
            "from Event e " +
            "where e.timestamp between :startDate and :endDate " +
            "and e.eventLabel=:eventLabel " +
            "group by e.eventName")
    Page<EventTableResponse> findEventsByEventLabelAndTimestampBetween(
            @Param("eventLabel") String eventLabel,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Query("""
        SELECT new com.example.backend.dto.response.NumberOfEventResponse(
            case
                when e.eventLabel ='eventName' and e.eventName='purchase' then 'purchase'
                when e.eventLabel ='eventName' then 'Event Count'
                when e.eventLabel ='city' then 'Active Users'
                else 'unknown'
            end,
            sum(cast(e.eventValue as Long)))
            FROM Event e
            WHERE e.timestamp between :startDate and :endDate
            group by e.eventLabel,
            case
                when e.eventLabel ='eventName' and e.eventName='purchase' then 'purchase'
                when e.eventLabel ='eventName' then 'Event Count'
                when e.eventLabel ='city' then 'Active Users'
                else 'unknown'
            end
""")
    List<NumberOfEventResponse> numberOfEventsByEventLabel(LocalDateTime startDate,LocalDateTime endDate);

    @Query("""
        SELECT
            date_trunc('day',e.timestamp) as time_event,
            CASE
                WHEN e.eventName = 'purchase' THEN 'purchase'
                WHEN e.eventName = 'form_start' THEN 'form submission'
                WHEN e.eventLabel = 'city' THEN 'active users'
                ELSE 'other'
                END AS event_type,
            SUM(CAST(e.eventValue AS Long)) AS event_value
        FROM
            Event e
        WHERE
            e.timestamp BETWEEN :startDate AND :endDate
          AND (
            e.eventName IN ('purchase', 'form_start') OR
            e.eventLabel = 'city'
            )
        GROUP BY
            time_event,
            event_type
        ORDER BY
            date_trunc('day',e.timestamp),
            CASE
                WHEN e.eventName = 'purchase' THEN 'purchase'
                WHEN e.eventName = 'form_start' THEN 'form submission'
                WHEN e.eventLabel = 'city' THEN 'active users'
                ELSE 'other'
                END
""")
    List<Object[]> getEventsForChart(@Param("startDate") LocalDateTime startDate,@Param("endDate") LocalDateTime endDate);

}
