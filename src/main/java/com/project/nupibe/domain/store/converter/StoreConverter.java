package com.project.nupibe.domain.store.converter;

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
}
