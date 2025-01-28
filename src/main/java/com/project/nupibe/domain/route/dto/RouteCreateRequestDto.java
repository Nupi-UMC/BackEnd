package com.project.nupibe.domain.route.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RouteCreateRequestDto {

    // 경로 이름 , 날짜 , 장소들(리스트)

    private String routeName;
    private String content;
    private String location;
    private LocalDateTime date;
    private String category;
    private Long memberId; //나중에 코드 정리해야함 (없애야되는부분)
    private List<StoreDto> stores;

    @Getter
    @Setter
    public static class StoreDto {
        private Long id;
        private int orderIndex;

    }
}


