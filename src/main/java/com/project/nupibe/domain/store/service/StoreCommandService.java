package com.project.nupibe.domain.store.service;

import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.entity.MemberStore;
import com.project.nupibe.domain.member.entity.StoreLike;
import com.project.nupibe.domain.member.exception.code.MemberErrorCode;
import com.project.nupibe.domain.member.exception.handler.MemberException;
import com.project.nupibe.domain.member.repository.MemberRepository;
import com.project.nupibe.domain.member.repository.MemberStoreRepository;
import com.project.nupibe.domain.member.repository.StoreLikeRepository;
import com.project.nupibe.domain.route.entity.RouteStore;
import com.project.nupibe.domain.route.repository.RouteStoreRepository;
import com.project.nupibe.domain.store.converter.StoreConverter;
import com.project.nupibe.domain.store.dto.response.StoreResponseDTO;
import com.project.nupibe.domain.store.entity.Store;
import com.project.nupibe.domain.store.exception.code.StoreErrorCode;
import com.project.nupibe.domain.store.exception.handler.StoreException;
import com.project.nupibe.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreCommandService {
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final MemberStoreRepository memberStoreRepository;
    private final StoreLikeRepository storeLikeRepository;
    private final RouteStoreRepository routeStoreRepository;

    @Transactional(readOnly = true)
    public StoreResponseDTO.MemberRouteListDTO getRoutesByStoreId(Long storeId) {
        // storeId로 RouteStore 리스트 조회
        List<RouteStore> routeStores = routeStoreRepository.findByStoreId(storeId);

        // List<RouteStore> -> MemberRouteListDTO 변환
        return StoreConverter.toMemberRouteListDTO(routeStores);
    }
    @Transactional
    public StoreResponseDTO.savedDTO bookmarkStore(Long memberId, Long storeId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        boolean exists = memberStoreRepository.existsByMemberIdAndStoreId(memberId, storeId);
        //존재하는지 확인
        if (exists) {
            //Store 조회 후 삭제
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new StoreException(StoreErrorCode.NOT_FOUND));
            MemberStore memberStore = memberStoreRepository.findByMemberandStore(member, store);
            memberStoreRepository.delete(memberStore);

            // 가게의 북마크 수 감소
            store.setBookmarkNum(store.getBookmarkNum() - 1);
            storeRepository.save(store);

        }
        else{
        //Store 조회 후 추가
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.NOT_FOUND));
        MemberStore memberStore = MemberStore.builder()
                .member(member)
                .store(store)
                .build();
        memberStoreRepository.save(memberStore);

        // 가게의 북마크 수 증가
        store.setBookmarkNum(store.getBookmarkNum() + 1);
        storeRepository.save(store);
        }
        return StoreConverter.save(storeId,!exists);
    }
    @Transactional
    public StoreResponseDTO.savedDTO likeStore(Long memberId, Long storeId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        boolean exists = storeLikeRepository.existsByMemberIdAndStoreId(memberId, storeId);
        //존재하는지 확인
        if (exists) {
            //Store 조회 후 삭제
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new StoreException(StoreErrorCode.NOT_FOUND));
            StoreLike storeLike = storeLikeRepository.findByMemberandStore(member, store);
            storeLikeRepository.delete(storeLike);

            // 가게의 좋아요 수 감소
            store.setLikeNum(store.getLikeNum() - 1);
            storeRepository.save(store);

        }
        else{
            //Store 조회 후 추가
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new StoreException(StoreErrorCode.NOT_FOUND));
            StoreLike storeLike = StoreLike.builder()
                    .member(member)
                    .store(store)
                    .build();
            storeLikeRepository.save(storeLike);

            // 가게의 북마크 수 증가
            store.setLikeNum(store.getLikeNum() + 1);
            storeRepository.save(store);
        }
        return StoreConverter.save(storeId,!exists);
    }
    }

