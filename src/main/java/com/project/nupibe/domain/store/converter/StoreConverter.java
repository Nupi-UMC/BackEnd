package com.project.nupibe.domain.store.converter;

import com.project.nupibe.domain.route.entity.Route;
import com.project.nupibe.domain.route.entity.RouteStore;
import com.project.nupibe.domain.store.dto.response.HomeResponseDTO;
import com.project.nupibe.domain.store.dto.response.StoreResponseDTO;
import com.project.nupibe.domain.store.entity.Store;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreConverter {
    public static StoreResponseDTO.StoreDetailResponseDTO toStoreDetailResponseDTO(
            Store store,
            boolean isLiked,
            boolean isBookmarked,
            List<String> slideImages) {
        return StoreResponseDTO.StoreDetailResponseDTO.builder()
                .id(store.getId())
                .name(store.getName())
                .content(store.getContent())
                .slideImages(slideImages)
                .category(store.getCategory())
                .likeNum(store.getLikeNum())
                .bookmarkNum(store.getBookmarkNum())
                .isLiked(isLiked)
                .isBookmarked(isBookmarked)
                .location(store.getLocation())
                .address(store.getAddress())
                .businessHours(store.getBusinessHours())
                .number(store.getNumber())
                .snsUrl(store.getSnsUrl())
                .groupInfo(store.getGroupInfo())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .build();
    }


    public static StoreResponseDTO.StorePreviewDTO toStorePreviewDto(Store store, List<String> slideImages){
        return StoreResponseDTO.StorePreviewDTO.builder()
                .id(store.getId())
                .name(store.getName())
                .groupInfo(store.getGroupInfo())
                .category(store.getCategory())
                .bookmarkNum(store.getBookmarkNum())
                .likeNum(store.getLikeNum())
                .location(store.getLocation())
                .slideImages(slideImages)
                .build();
    }

    public static StoreResponseDTO.StorePageDTO tostorePageDTO(Slice<StoreResponseDTO.StorePreviewDTO> storePreviews) {
        return StoreResponseDTO.StorePageDTO.builder()
                .storeList(storePreviews.getContent())
                .hasNext(storePreviews.hasNext())
                .cursor(storePreviews.getContent().isEmpty() ? 0L : storePreviews.getContent().get(storePreviews.getContent().size() - 1).id())
                .build();
    }


    public static StoreResponseDTO.savedDTO save(Long storeId, boolean save) {
        return StoreResponseDTO.savedDTO.builder()
                .storeId(storeId)
                .saved(save).build();
    }

    public static StoreResponseDTO.MemberRouteDTO toMemberRouteDTO(Route route, List<RouteStore> routeStores) {
        // 이미지 가져오기
        List<String> images = routeStores.stream()
                .map(routeStore -> routeStore.getStore().getImage())
                .collect(Collectors.toList());

        return StoreResponseDTO.MemberRouteDTO.builder()
                .routeId(route.getId())
                .routeName(route.getRouteName())
                .location(route.getLocation())
                .likeNum(route.getLikeNum())
                .bookmarkNum(route.getBookmarkNum())
                .images(images)
                .build();
    }

    public static StoreResponseDTO.MemberRouteListDTO toMemberRouteListDTO(List<RouteStore> routeStores) {
        return StoreResponseDTO.MemberRouteListDTO.builder()
                .routes(routeStores.stream()
                        .collect(Collectors.groupingBy(RouteStore::getRoute))
                        .entrySet()
                        .stream()
                        .map(entry -> toMemberRouteDTO(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList())
                )
                .build();
    }
}
