package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

     // Finds events by event label and timestamp within a specified date range.
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
            """, countQuery = """
                SELECT COUNT(*) FROM generate_series(
                    CAST(:startDate AS DATE),
                    CAST(:endDate AS DATE),
                    '1 day'::interval
                ) AS nth_day
            """, nativeQuery = true)
    List<Object[]> findEventsByEventLabelAndTimestamp(
            @Param("eventLabel") String eventLabel,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

     //Retrieves city statistics within a date range.
    @Query(value = """
            SELECT
                DATE(timestamp) AS dateEventOccurred,
                event_name AS City,
                SUM(CAST(event_value AS NUMERIC)) AS value
            FROM
                db_event
            WHERE
                timestamp BETWEEN CAST(:startDate AS TIMESTAMP) AND CAST(:endDate AS TIMESTAMP)
                AND event_label = 'city'
            GROUP BY
                DATE(timestamp), event_name
            ORDER BY
                DATE(timestamp), event_name
            """, nativeQuery = true)
    List<Object[]> findCityStatisticsByDateRange(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

     // Retrieves event statistics by date range.
    @Query(value = """
            SELECT
                DATE(timestamp) AS dateEventOccurred,
                event_name AS eventName,
                SUM(CAST(event_value AS NUMERIC)) AS value
            FROM
                db_event
            WHERE
                timestamp BETWEEN CAST(:startDate AS TIMESTAMP) AND CAST(:endDate AS TIMESTAMP)
                AND event_label = 'eventName'
            GROUP BY
                DATE(timestamp), event_name
            ORDER BY
                DATE(timestamp), event_name
            """, nativeQuery = true)
    List<Object[]> findEventStatisticsByDateRange(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

     // Retrieves purchase statistics by date range.
    @Query(value = """
        SELECT
            DATE(timestamp) AS dateEventOccurred,
            event_name AS eventName,
            SUM(CAST(event_value AS NUMERIC)) AS value
        FROM
            db_event
        WHERE
            timestamp BETWEEN CAST(:startDate AS TIMESTAMP) AND CAST(:endDate AS TIMESTAMP)
            AND event_name = 'purchase'
        GROUP BY
            DATE(timestamp), event_name
        ORDER BY
            DATE(timestamp)
        """, nativeQuery = true)
    List<Object[]> findPurchaseStatisticsByDateRange(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );


     // Retrieves media statistics by date range.
    @Query(value = """
        SELECT
            m.medium_name AS Media,
            SUM(CAST(e.event_value AS NUMERIC)) AS value
        FROM 
            db_event e
        JOIN 
            db_medium m ON e.medium_id = m.medium_id
        WHERE 
            e.timestamp BETWEEN CAST(:startDate AS TIMESTAMP) AND CAST(:endDate AS TIMESTAMP)
        GROUP BY 
            m.medium_name
        ORDER BY 
            m.medium_name
        """, nativeQuery = true)
    List<Object[]> findMediaStatisticsByDateRange(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

}
