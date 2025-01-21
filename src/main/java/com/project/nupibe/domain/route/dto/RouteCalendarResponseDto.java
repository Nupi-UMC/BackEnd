package com.project.nupibe.domain.route.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RouteCalendarResponseDto {
    private String month; // 요청된 월(yyyy-MM 형식)
    private List<LocalDate> dates; // 일정이 있는 날짜 목록
}
