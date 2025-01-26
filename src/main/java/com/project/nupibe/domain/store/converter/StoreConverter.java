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
    //entity -> previewDTO

    public static StoreResponseDTO.StoreDetailResponseDTO toStoreDetailResponseDTO(Store store){
        return StoreResponseDTO.StoreDetailResponseDTO.builder()
                .id(store.getId())
                .name(store.getName())
                .content(store.getContent())
                .image(store.getImage())
                .category(store.getCategory())
                .like_num(store.getLikeNum())
                .bookmark_num(store.getBookmarkNum())
                .build();
    }
    public static StoreResponseDTO.StorePreviewDTO toStorePreviewDto(Store store){
        return StoreResponseDTO.StorePreviewDTO.builder()
                .id(store.getId())
                .name(store.getName())
                .groupInfo(store.getGroupInfo())
                .bookmarkNum(store.getBookmarkNum())
                .likeNum(store.getLikeNum())
                .location(store.getLocation())
                .image(store.getImage())
                .build();
    }

    //entity리스트 -> previewDTO리스트
    public static List<StoreResponseDTO.StorePreviewDTO> toStorePreviewList(List<Store> stores){
        return stores.stream()
                .map(store -> toStorePreviewDto(store))
                .collect(Collectors.toList());
    }

    //리스트 -> slice
    public static StoreResponseDTO.StorePageDTO tostorePageDTO(Slice<Store> stores){
        List<StoreResponseDTO.StorePreviewDTO> storeList = toStorePreviewList(stores.getContent());
        return StoreResponseDTO.StorePageDTO.builder()
                .storeList(storeList)
                .hasNext(stores.hasNext())
                .cursor(stores.getContent().get(stores.getContent().size() - 1).getId())
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
