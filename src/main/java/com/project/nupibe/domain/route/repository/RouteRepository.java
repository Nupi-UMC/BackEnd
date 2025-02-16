package com.project.nupibe.domain.route.repository;

import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.route.entity.Route;
import com.project.nupibe.domain.store.entity.Store;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route,Long> {

    // 날짜 기준으로 경로 조회
    List<Route> findByDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    // 특정 날짜 범위 내의 경로가 있는 날짜 조회
    @Query("SELECT r.date FROM Route r WHERE r.date BETWEEN :startDate AND :endDate AND r.member.id = :memberId")
    List<LocalDateTime> findDatesBetweenAndMemberId(LocalDateTime startDate, LocalDateTime endDate, Long memberId);

    @Query("SELECT r FROM Route r WHERE r.member = :member")
    List<Route> findByMember(@Param("member") Member member);


    @Query(
            value = """
            SELECT r.* FROM route r 
                JOIN route_store rs ON r.id = rs.route_id
                JOIN store s ON rs.store_id = s.id
            WHERE (r.location LIKE LOWER(CONCAT('%', :search, '%')) OR
                   LOWER(r.route_name) LIKE LOWER(CONCAT('%', :search, '%')) OR
                   LOWER(r.category) LIKE LOWER(CONCAT('%', :search, '%')))
            AND rs.order_index = 1
            ORDER BY ST_Distance(s.coordinates, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) ASC
            """,
            nativeQuery = true
    )
    Slice<Route> findBySearchOrderByDistanceDesc(Pageable pageable, @Param("latitude") Float latitude,
                                                     @Param("longitude") Float longitude, @Param("search") String search);

    @Query(
            value = """
                SELECT r.* FROM route r 
                JOIN route_store rs ON r.id = rs.route_id 
                JOIN store s ON rs.store_id = s.id 
                WHERE (r.location LIKE LOWER(CONCAT('%', :search, '%')) OR
                       LOWER(r.route_name) LIKE LOWER(CONCAT('%', :search, '%')) OR 
                       LOWER(r.category) LIKE LOWER(CONCAT('%', :search, '%')))
                AND rs.order_index = 1
                AND (
                    :cursor IS NULL OR 
                    ST_Distance(s.coordinates1, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) > (
                        SELECT ST_Distance(s3.coordinates1, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) 
                        FROM store s3 
                        JOIN route_store rs3 ON s3.id = rs3.store_id
                        WHERE rs3.route_id = r.id AND rs3.order_index = 1 
                        AND s3.id = :cursor
                    )
                )
                ORDER BY 
                    ST_Distance(s.coordinates1, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) ASC 
                """,
            nativeQuery = true
    )
    Slice<Route> findBySearchOrderByDistanceWithCursor(
            @Param("cursor") Long cursor,
            Pageable pageable,
            @Param("latitude") Float latitude,
            @Param("longitude") Float longitude,
            @Param("search") String search
    );
    @Query(
            value = """
                    SELECT * FROM route r 
                    WHERE (r.location LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(r.route_name) LIKE LOWER(CONCAT('%', :search, '%')) OR 
            LOWER(r.category) LIKE LOWER(CONCAT('%', :search, '%')))
                    ORDER BY r.bookmark_num DESC , r.id DESC 
                    """,
            nativeQuery = true
    )
    Slice<Route> findBySearchOrderByBOOKMARKNUMAscIdAsc(Pageable pageable, @Param("search") String search);
    @Query(value = """
    SELECT r.*, cursorTable.cursor_id FROM route r 
    JOIN (
        SELECT r2.id AS cursor_id, CONCAT(
            LPAD(CAST(r2.bookmark_num AS TEXT), 10, '0'), 
            LPAD(CAST(r2.id AS TEXT), 10, '0')
        ) AS cursorValue
        FROM route r2  
    ) AS cursorTable ON r.id = cursorTable.cursor_id
    WHERE(
            (r.location LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(r.route_name) LIKE LOWER(CONCAT('%', :search, '%')) OR 
            LOWER(r.category) LIKE LOWER(CONCAT('%', :search, '%')))
        )
        AND cursorValue < (
            SELECT CONCAT(
                LPAD(CAST(r3.bookmark_num AS TEXT), 10, '0'), 
                LPAD(CAST(r3.id AS TEXT), 10, '0')
            ) 
            FROM route r3
            WHERE r3.id = :cursor
        )
    ORDER BY cursorTable.cursorValue DESC 
    """, nativeQuery = true)
    Slice<Route> findBySearchOrderByBOOKMARKNUMWithCursor(@Param("storeId") Long cursor, Pageable pageable, @Param("search") String search);

    @Query(
            value = """
                    SELECT * FROM route r 
                    WHERE (r.location LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(r.route_name) LIKE LOWER(CONCAT('%', :search, '%')) OR 
            LOWER(r.category) LIKE LOWER(CONCAT('%', :search, '%')))
                    ORDER BY r.like_num DESC , r.id DESC 
                    """,
            nativeQuery = true
    )
    Slice<Route> findBySearchOrderByLikeNumAscIdAsc(Pageable pageable, @Param("search") String search);
    @Query(value = """
    SELECT r.*, cursorTable.cursor_id FROM route r 
    JOIN (
        SELECT r2.id AS cursor_id, CONCAT(
            LPAD(CAST(r2.like_num AS TEXT), 10, '0'), 
            LPAD(CAST(r2.id AS TEXT), 10, '0')
        ) AS cursorValue
        FROM route r2
    ) AS cursorTable ON r.id = cursorTable.cursor_id
    WHERE(
            (r.location LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(r.route_name) LIKE LOWER(CONCAT('%', :search, '%')) OR 
            LOWER(r.category) LIKE LOWER(CONCAT('%', :search, '%')))
        )
        AND cursorValue < (
            SELECT CONCAT(
                LPAD(CAST(r3.like_num AS TEXT), 10, '0'), 
                LPAD(CAST(r3.id AS TEXT), 10, '0')
            ) 
            FROM route r3
            WHERE r3.id = :cursor
        )
    ORDER BY cursorTable.cursorValue DESC 
    """, nativeQuery = true)
    Slice<Route> findBySearchOrderByLikeNumWithCursor(@Param("cursor") Long cursor, Pageable pageable, @Param("search") String search);

    @Query("SELECT r FROM Route r WHERE r.member = :member AND r.date >= :now ORDER BY r.date ASC")
    Route getLatestRoute(@Param("member") Member member, @Param("now") LocalDateTime now);
}


