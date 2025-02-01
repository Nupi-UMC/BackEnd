package com.project.nupibe.domain.store.service;

import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.exception.code.MemberErrorCode;
import com.project.nupibe.domain.member.exception.handler.MemberException;
import com.project.nupibe.domain.member.repository.MemberRepository;
import com.project.nupibe.domain.member.repository.MemberStoreRepository;
import com.project.nupibe.domain.member.repository.StoreLikeRepository;
import com.project.nupibe.domain.store.converter.StoreConverter;
import com.project.nupibe.domain.store.dto.response.StoreResponseDTO;
import com.project.nupibe.domain.store.entity.ImageType;
import com.project.nupibe.domain.store.entity.Store;
import com.project.nupibe.domain.store.entity.StoreImage;
import com.project.nupibe.domain.store.entity.StoreSearchQuery;
import com.project.nupibe.domain.store.exception.code.StoreErrorCode;
import com.project.nupibe.domain.store.exception.handler.StoreException;
import com.project.nupibe.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreQueryService {
    private final StoreRepository storeRepository;
    private final StoreLikeRepository storeLikeRepository;
    private final MemberStoreRepository memberStoreRepository;


    private final int RADIUS = 1000; //1km 반경에 있는 가게 조회

    // 단일 가게 조회(detail)
    public StoreResponseDTO.StoreDetailResponseDTO getStoreDetail(Long storeId, Long memberId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.NOT_FOUND));

        boolean isLiked = false;
        boolean isBookmarked = false;

        if (memberId != null) {
            isLiked = storeLikeRepository.existsByMemberIdAndStoreId(memberId, storeId);
            isBookmarked = memberStoreRepository.existsByMemberIdAndStoreId(memberId, storeId);
        }

        List<String> slideImages = getSlideImages(store);

        if (slideImages == null || slideImages.isEmpty()) {
            throw new StoreException(StoreErrorCode.IMAGE_NOT_FOUND);
        }

        return StoreConverter.toStoreDetailResponseDTO(store, isLiked, isBookmarked, slideImages);
    }


    private List<String> getSlideImages(Store store) {
        List<String> slideImages = new ArrayList<>();

        // 대표 이미지를 리스트의 첫 번째 요소로 추가
        slideImages.add(store.getImage());

        slideImages.addAll(store.getImages().stream()
                .filter(image -> image.getType() == ImageType.MAIN)
                .map(StoreImage::getImageUrl)
                .toList());

        return slideImages;
    }

    //이미지 탭
    public StoreResponseDTO.StoreImagesDTO getTabImages(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.NOT_FOUND));

        List<String> tabImages = store.getImages().stream()
                .filter(image -> image.getType() == ImageType.TAB)
                .map(StoreImage::getImageUrl)
                .toList();

        if (tabImages.isEmpty()) {
            throw new StoreException(StoreErrorCode.IMAGE_NOT_FOUND);
        }

        return new StoreResponseDTO.StoreImagesDTO(storeId, tabImages);
    }

    //단일 가게 조회(preview)
    public StoreResponseDTO.StorePreviewDTO getStorePreview(Long storeId){
        Store store = storeRepository.findById(storeId)
                .orElseThrow(()-> new StoreException(StoreErrorCode.NOT_FOUND));
        return StoreConverter.toStorePreviewDto(store);
    }

    //내 위치 주변 가게 조회
    public StoreResponseDTO.StorePageDTO getStores(String query, float lat, float lng, Long cursor, int offset){
        Pageable pageable = PageRequest.of(0, offset);
        Slice<Store> stores;

        String enumQuery;
        switch (query) {
            case "거리순":
                enumQuery = StoreSearchQuery.DISTANCE.name();
                break;
            case "북마크순":
                enumQuery = StoreSearchQuery.BOOKMARKNUM.name();
                break;
            case "추천순":
                enumQuery = StoreSearchQuery.RECOMMEND.name();
                break;
            default:
                throw new StoreException(StoreErrorCode.UNSUPPORTED_QUERY);
        }

        if (enumQuery.equals(StoreSearchQuery.DISTANCE.name())) {
            if (cursor.equals(0L)) {
                stores = storeRepository.findAroundOrderByDistanceAscIdAsc(pageable,lat, lng, RADIUS);
            }
            else{
                stores = storeRepository.findAroundOrderByDistanceWithCursor(cursor, pageable,lat, lng, RADIUS);
            }
        }
        else if (enumQuery.equals(StoreSearchQuery.BOOKMARKNUM.name())) {
            if (cursor.equals(0L)) {
                stores = storeRepository.findAroundOrderByBOOKMARKNUMAscIdAsc(pageable,lat, lng, RADIUS);
            }
            else{
                stores = storeRepository.findAroundOrderByBOOKMARKNUMWithCursor(cursor, pageable,lat, lng, RADIUS);
            }
        }
        else if (enumQuery.equals(StoreSearchQuery.RECOMMEND.name())) {
            if (cursor.equals(0L)) {
                stores = storeRepository.findAroundOrderByLikeNumAscIdAsc(pageable,lat, lng, RADIUS);
            }
            else{
                stores = storeRepository.findAroundOrderByLikeNumWithCursor(cursor, pageable,lat, lng, RADIUS);
            }
        }
        else {
            throw new StoreException(StoreErrorCode.UNSUPPORTED_QUERY);
        }

        // stores가 비어있는지 확인하고 빈 리스트일 경우 처리
        if (stores.isEmpty()) {
            return new StoreResponseDTO.StorePageDTO(new ArrayList<>(), false, 0L); // 빈 리스트로 초기화
        }

        return StoreConverter.tostorePageDTO(stores);

    }

    //장소 검색 조회
    public StoreResponseDTO.StorePageDTO getStoresWithQuery(String query, float lat, float lng, String search, Long cursor, int offset){
        Pageable pageable = PageRequest.of(0, offset);
        Slice<Store> stores;

        String enumQuery;
        switch (query) {
            case "거리순":
                enumQuery = StoreSearchQuery.DISTANCE.name();
                break;
            case "북마크순":
                enumQuery = StoreSearchQuery.BOOKMARKNUM.name();
                break;
            case "추천순":
                enumQuery = StoreSearchQuery.RECOMMEND.name();
                break;
            default:
                throw new StoreException(StoreErrorCode.UNSUPPORTED_QUERY);
        }

        if (enumQuery.equals(StoreSearchQuery.DISTANCE.name())) {
            if (cursor.equals(0L)) {
                stores = storeRepository.findBySearchOrderByDistanceAscIdAsc(pageable, lat, lng, search);
            } else {
                stores = storeRepository.findBySearchOrderByDistanceWithCursor(cursor, pageable, lat, lng, search);
            }
        }
        else if (enumQuery.equals(StoreSearchQuery.BOOKMARKNUM.name())) {
            if (cursor.equals(0L)) {
                stores = storeRepository.findBySearchOrderByBOOKMARKNUMAscIdAsc(pageable, search);
            } else {
                stores = storeRepository.findBySearchOrderByBOOKMARKNUMWithCursor(cursor, pageable, search);
            }
        }
        else if (enumQuery.equals(StoreSearchQuery.RECOMMEND.name())) {
            if (cursor.equals(0L)) {
                stores = storeRepository.findBySearchOrderByLikeNumAscIdAsc(pageable, search);
            } else {
                stores = storeRepository.findBySearchOrderByLikeNumWithCursor(cursor, pageable, search);
            }
        }
        else {
            throw new StoreException(StoreErrorCode.UNSUPPORTED_QUERY);
        }

         //stores가 비어있는지 확인하고 빈 리스트일 경우 처리
        if (stores.isEmpty()) {
            return new StoreResponseDTO.StorePageDTO(new ArrayList<>(), false,0L); // 빈 리스트로 초기화
        }

        return StoreConverter.tostorePageDTO(stores);
    }
}
