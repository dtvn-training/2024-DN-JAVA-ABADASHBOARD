package com.example.backend.repository;

import com.example.backend.dto.DailyStatisticDTO;
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
    @Query("SELECT DISTINCT e.eventLabel FROM Event e")
    List<String> findDistinctEventLabels();

    @Query(value = """
    WITH date_series AS (
        SELECT
            generate_series(
                CAST(:startDate AS DATE),
                CAST(:endDate AS DATE),
                '1 day'::interval
            ) AS nth_day
    )
    SELECT
        EXTRACT(DAY FROM (ds.nth_day - CAST(:startDate AS DATE))) AS day,
        COALESCE(SUM(CAST(e.event_value AS NUMERIC)), 0) AS value
    FROM
        date_series ds
    LEFT JOIN
        db_event e
    ON
        ds.nth_day = DATE(e.timestamp)
        AND e.event_label = :eventLabel
    WHERE
        ds.nth_day BETWEEN CAST(:startDate AS DATE) AND CAST(:endDate AS DATE)
    GROUP BY
        ds.nth_day
    ORDER BY
        ds.nth_day
""",
            countQuery = """
    SELECT COUNT(*) FROM generate_series(
        CAST(:startDate AS DATE),
        CAST(:endDate AS DATE),
        '1 day'::interval
    ) AS nth_day
""",
            nativeQuery = true)
    Page<Object[]> findEventsByEventLabelAndTimestampPaginated(
            @Param("eventLabel") String eventLabel,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );



}