package com.project.nupibe.domain.store.dto.response;

import lombok.Builder;

import java.util.List;

public class StoreResponseDTO {

    @Builder
    public static record MemberRouteDTO (
            Long routeId,
            String routeName,
            String location,
            int likeNum,
            int bookmarkNum,
            List<String> images
    ){}
    @Builder
    public static record MemberRouteListDTO (
            List<MemberRouteDTO> routes

    ){}

    @Builder
    public record StoreDetailResponseDTO(
            Long id,
            String name,
            String content,
            String location,
            String address,
            String businessHours,
            String number,
            String snsUrl,
            List<String> slideImages,
            String category,
            String groupInfo,
            int likeNum,
            int bookmarkNum,
            Boolean isLiked,
            Boolean isBookmarked,
            float latitude,
            float longitude
    ) {}

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


    public record StoreImagesDTO(
            Long storeId,
            List<String> tabImages
    ) {}
}
