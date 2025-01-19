package com.project.nupibe.domain.route.controller;

import com.project.nupibe.domain.route.dto.RouteDto;
import com.project.nupibe.domain.route.service.ScheduleService;
import com.project.nupibe.global.apiPayload.CustomResponse;
import com.project.nupibe.global.apiPayload.code.GeneralErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class ScheduleController {


    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<CustomResponse<?>> getScheduleByDate(@RequestParam String date) {
        try {
            // 날짜 형식 검증 및 변환
            LocalDate parsedDate = LocalDate.parse(date);

            // 하루의 시작과 끝 시간 계산
            LocalDateTime startOfDay = parsedDate.atStartOfDay(); // 2024-12-13 00:00:00
            LocalDateTime endOfDay = parsedDate.atTime(23, 59, 59, 999999); // 2024-12-13 23:59:59.999999

            // 서비스 호출
            List<RouteDto> routes = scheduleService.getScheduleByDate(startOfDay,endOfDay);

            if (routes.isEmpty()) {
                return ResponseEntity.ok(
                        CustomResponse.onSuccess(HttpStatus.OK, List.of())
                );
            }

            return ResponseEntity.ok(
                    CustomResponse.onSuccess(HttpStatus.OK, new ScheduleResponse(date, routes))
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    GeneralErrorCode.VALIDATION_FAILED.getErrorResponse()
            );
        }
    }

    private static class ScheduleResponse {
        private String date;
        private List<RouteDto> routes;

        public ScheduleResponse(String date, List<RouteDto> routes) {
            this.date = date;
            this.routes = routes;
        }

        // Getter
        public String getDate() {
            return date;
        }

        public List<RouteDto> getRoutes() {
            return routes;
        }
    }

    @GetMapping("/calendar")
    public ResponseEntity<CustomResponse<?>> getDatesWithRoutes(@RequestParam String month) {
        try {
            // 요청받은 month를 LocalDate로 파싱
            YearMonth parsedMonth = YearMonth.parse(month, DateTimeFormatter.ofPattern("yyyy-MM"));

            // 해당 월의 시작일과 마지막일 계산
            LocalDate startDate = parsedMonth.atDay(1); // 해당 월의 첫 날
            LocalDate endDate = parsedMonth.atEndOfMonth(); // 해당 월의 마지막 날

            // 서비스 호출
            List<LocalDate> datesWithRoutes = scheduleService.getDatesWithRoutes(startDate, endDate);

            return ResponseEntity.ok(CustomResponse.onSuccess(datesWithRoutes));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    CustomResponse.onFailure("VALID400_0", "잘못된 요청입니다.")
            );
        }
    }

}
