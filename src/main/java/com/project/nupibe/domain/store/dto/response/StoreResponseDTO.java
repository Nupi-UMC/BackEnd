package com.project.nupibe.domain.store.dto.response;

import lombok.Builder;

import java.util.List;

public class StoreResponseDTO {
    @Builder
    public record StoreDetailResponseDTO (
            Long id,
            String name,
            String content,
            String image,
            String category,
            Integer like_num,
            Integer bookmark_num){
    }
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

    @Builder
    public record savedDTO(
            Long storeId,
            boolean saved
    ) {};
}
