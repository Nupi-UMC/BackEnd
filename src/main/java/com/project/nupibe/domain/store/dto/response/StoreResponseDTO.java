package com.project.nupibe.domain.store.dto.response;

import lombok.Builder;

import java.util.List;

public class StoreResponseDTO {
    @Builder
    public record StorePreviewDTO(
            Long id,
            String image,
            String name,
            String groupInfo,
            String location,
            int likeNum,
            int bookmarkNum
    ){}

    @Builder
    public record StorePageDTO(
            List<StorePreviewDTO> storeList,
            boolean hasNext,
            Long cursor
    ){}
}
