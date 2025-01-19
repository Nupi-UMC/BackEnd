package com.project.nupibe.domain.route.repository;

import com.project.nupibe.domain.route.entity.Route;
import io.lettuce.core.dynamic.annotation.Param;
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
    @Query("SELECT r.date FROM Route r WHERE r.date BETWEEN :startDate AND :endDate")
    List<LocalDateTime> findDatesBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}

