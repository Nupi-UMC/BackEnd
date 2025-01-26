package com.project.nupibe.domain.store.service;

import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.entity.MemberStore;
import com.project.nupibe.domain.member.exception.code.MemberErrorCode;
import com.project.nupibe.domain.member.exception.handler.MemberException;
import com.project.nupibe.domain.member.repository.MemberRepository;
import com.project.nupibe.domain.member.repository.MemberStoreRepository;
import com.project.nupibe.domain.store.converter.HomeConverter;
import com.project.nupibe.domain.store.dto.response.HomeResponseDTO;
import com.project.nupibe.domain.store.entity.Store;
import com.project.nupibe.domain.store.exception.code.StoreErrorCode;
import com.project.nupibe.domain.store.exception.handler.StoreException;
import com.project.nupibe.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class HomeCommandService {
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final MemberStoreRepository memberStoreRepository;

    public HomeResponseDTO.savedDTO saveStore(Long memberId, Long storeId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        boolean saved = memberStoreRepository.existsByMemberIdAndStoreId(memberId, storeId);
        if(saved) {
            Store store = storeRepository.findById(storeId).orElseThrow(() -> new StoreException(StoreErrorCode.NOT_FOUND));
            store.setBookmarkNum(store.getBookmarkNum() - 1);
            storeRepository.save(store);

            MemberStore memberStore = memberStoreRepository.findByMemberandStore(member, store);
            memberStoreRepository.delete(memberStore);
        }
        else {
            Store store = storeRepository.findById(storeId).orElseThrow(() -> new StoreException(StoreErrorCode.NOT_FOUND));
            store.setBookmarkNum(store.getBookmarkNum() + 1storeRepository.save(store);

            MemberStore memberStore = MemberStore.builder().member(member).store(store).build();
            memberStoreRepository.save(memberStore);
        }
        boolean save = !saved;

        return HomeConverter.save(storeId, save);
    }
}
