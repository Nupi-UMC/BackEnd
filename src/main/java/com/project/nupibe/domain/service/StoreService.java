package com.project.nupibe.domain.service;

import com.project.nupibe.domain.converter.StoreConverter;
import com.project.nupibe.domain.dto.response.StoreDetailResponseDTO;
import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.entity.MemberStore;
import com.project.nupibe.domain.repository.MemberStoreRepository;
import com.project.nupibe.domain.repository.StoreRepository;
import com.project.nupibe.domain.store.entity.Store;
import com.project.nupibe.global.apiPayload.code.GeneralErrorCode;
import com.project.nupibe.global.apiPayload.exception.handler.StoreHandler;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {
    //Repository
    private final StoreRepository storeRepository;
    private final MemberStoreRepository memberStoreRepository;

    public StoreDetailResponseDTO getStoreDetail(Long storeId) {
        //Store 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreHandler(GeneralErrorCode.BAD_REQUEST_400)); //추후 errorcode 수정 예정
        return StoreConverter.toStoreDetailResponseDTO(store);
    }
    public void bookmarkStore(Long memberId, Long storeId) {
        boolean exists = memberStoreRepository.existsByMemberIdAndStoreId(memberId, storeId);
        if (exists) {
            throw new StoreHandler(GeneralErrorCode.BAD_REQUEST_400); //추후 errorcode 수정 예정
        }
        //Store 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreHandler(GeneralErrorCode.BAD_REQUEST_400)); //추후 errorcode 수정 예정

        //MemberStore 테이블에 데이터 저장
        MemberStore memberStore = MemberStore.builder()
                .member(Member.builder().id(memberId).build())
                .store(store)
                .build();

        memberStoreRepository.save(memberStore);

        // 가게의 북마크 수 증가
        store.setBookmarkNum(store.getBookmarkNum() + 1);
        storeRepository.save(store);
    }
    }

