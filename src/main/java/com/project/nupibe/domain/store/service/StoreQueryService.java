package com.project.nupibe.domain.store.service;

import com.project.nupibe.domain.store.converter.StoreConverter;
import com.project.nupibe.domain.store.dto.response.StoreResponseDTO;
import com.project.nupibe.domain.store.entity.Store;
import com.project.nupibe.domain.store.exception.code.StoreErrorCode;
import com.project.nupibe.domain.store.exception.handler.StoreException;
import com.project.nupibe.domain.store.repository.StoreRepositroy;
import com.project.nupibe.global.apiPayload.code.GeneralErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreQueryService {
    private final StoreRepositroy storeRepositroy;
    private final int RADIUS = 3000; //3km 반경에 있는 가게 조회

    //단일 가게 조회(detail)
    public StoreResponseDTO.StoreDetailResponseDTO getStoreDetail(Long storeId) {
        Store store = storeRepositroy.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.NOT_FOUND));
        return StoreConverter.toStoreDetailResponseDTO(store);
    }

    //단일 가게 조회(preview)
    public StoreResponseDTO.StorePreviewDTO getStorePreview(Long storeId){
        Store store = storeRepositroy.findById(storeId)
                .orElseThrow(()-> new StoreException(StoreErrorCode.NOT_FOUND));
        return StoreConverter.toStorePreviewDto(store);
    }

    //내 위치 주변 가게 조회
    public StoreResponseDTO.StorePageDTO getStores(Float lat, Float lng, int cursor, int offset){
        Pageable pageable = PageRequest.of(cursor, offset);
        Slice<Store> stores = storeRepositroy.findByEarthDistance(pageable,lat, lng, RADIUS);

        // stores가 비어있는지 확인하고 빈 리스트일 경우 처리
        if (stores.isEmpty()) {
            return new StoreResponseDTO.StorePageDTO(new ArrayList<>(), false, 0L); // 빈 리스트로 초기화
        }

        return StoreConverter.tostorePageDTO(stores);

    }

    //장소 검색 조회
    public StoreResponseDTO.StorePageDTO getStoresWithQuery(String query, int cursor, int offset){
        Pageable pageable = PageRequest.of(cursor, offset);
        Slice<Store> stores = storeRepositroy.findByQuery(pageable, query);

        // stores가 비어있는지 확인하고 빈 리스트일 경우 처리
        if (stores.isEmpty()) {
            return new StoreResponseDTO.StorePageDTO(new ArrayList<>(), false,0L); // 빈 리스트로 초기화
        }

        return StoreConverter.tostorePageDTO(stores);
    }
}
