package com.project.nupibe.domain.converter;

import com.project.nupibe.domain.dto.response.StoreDetailResponseDTO;
import com.project.nupibe.domain.store.entity.Store;

public class StoreConverter {
    public static StoreDetailResponseDTO toStoreDetailResponseDTO(Store store){
        return StoreDetailResponseDTO.builder()
                .store_id(store.getId())
                .name(store.getName())
                .content(store.getContent())
                .image(store.getImage())
                .category(store.getCategory())
                .like_num(store.getLikeNum())
                .bookmark_num(store.getBookmarkNum())
                .build();
   }
}
