package com.project.nupibe.domain.store.service;

import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.entity.MemberStore;
import com.project.nupibe.domain.member.repository.MemberStoreRepository;
import com.project.nupibe.domain.store.entity.Store;
import com.project.nupibe.domain.store.exception.code.StoreErrorCode;
import com.project.nupibe.domain.store.exception.handler.StoreException;
import com.project.nupibe.domain.store.repository.StoreRepository;
import com.project.nupibe.global.apiPayload.code.GeneralErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreCommandService {
    //Repository
    private final StoreRepository storeRepository;
    private final MemberStoreRepository memberStoreRepository;


    public void bookmarkStore(Long memberId, Long storeId) {
        boolean exists = memberStoreRepository.existsByMemberIdAndStoreId(memberId, storeId);
        //존재하는지 확인
        if (exists) {
            throw new StoreException(StoreErrorCode.ALREADY_EXISTS);
        }
        //Store 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.NOT_FOUND));

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

