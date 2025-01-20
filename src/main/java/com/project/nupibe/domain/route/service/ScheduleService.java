package com.project.nupibe.domain.route.service;

import com.project.nupibe.domain.route.dto.RouteDto;
import com.project.nupibe.domain.route.entity.Route;
import com.project.nupibe.domain.route.repository.RouteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final RouteRepository routeRepository;
    public ScheduleService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public List<RouteDto> getScheduleByDate(LocalDateTime startOfDay, LocalDateTime endOfDay) {
       // 데이터베이스에서 날짜 범위로 조회
        List<Route> routes = routeRepository.findByDateBetween(startOfDay, endOfDay);

        // Entity를 DTO로 변환
        return routes.stream()
                .map(route -> RouteDto.builder()
                        .routeId(route.getId())
                        .memberId(route.getMember().getId()) // Member ID 가져오기
                        .routeName(route.getRouteName())
                        .content(route.getContent())
                        .location(route.getLocation())
                        .createdAt(route.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public List<LocalDate> getDatesWithRoutes(LocalDate startDate, LocalDate endDate) {
        // 데이터베이스에서 해당 범위의 경로가 있는 날짜 조회
        List<LocalDateTime> routeDates = routeRepository.findDatesBetween(startDate.atStartOfDay(), endDate.atTime(23, 59, 59));

        // LocalDateTime -> LocalDate로 변환 후 중복 제거
        return routeDates.stream()
                .map(LocalDateTime::toLocalDate)
                .distinct()
                .collect(Collectors.toList());
    }


}
