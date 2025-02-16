package com.project.nupibe.domain.store.service;

import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.exception.code.MemberErrorCode;
import com.project.nupibe.domain.member.exception.handler.MemberException;
import com.project.nupibe.domain.member.repository.MemberRepository;
import com.project.nupibe.domain.member.repository.MemberRouteRepository;
import com.project.nupibe.domain.member.repository.MemberStoreRepository;
import com.project.nupibe.domain.region.entity.Region;
import com.project.nupibe.domain.region.repository.RegionRepository;
import com.project.nupibe.domain.route.entity.Route;
import com.project.nupibe.domain.route.repository.RouteRepository;
import com.project.nupibe.domain.route.repository.RouteStoreRepository;
import com.project.nupibe.domain.store.converter.HomeConverter;
import com.project.nupibe.domain.store.dto.response.HomeResponseDTO;
import com.project.nupibe.domain.store.entity.Store;
import com.project.nupibe.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class HomeQueryService {
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final RegionRepository regionRepository;
    private final RouteRepository routeRepository;
    private final MemberRouteRepository memberRouteRepository;
    private final RouteStoreRepository routeStoreRepository;
    private final MemberStoreRepository memberStoreRepository;

    private final List<String> regions = List.of("홍대", "성수", "을지로", "안국");
    private final Random random = new Random();

    public HomeResponseDTO.GetHomeResponseDTO getHome(Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        Route upcomming = routeRepository.getLatestRoute(memberRepository.findById(memberId).get(), now);

        List<String> groupNames = storeRepository.findDistinctGroupName();
        List<HomeResponseDTO.groupNameDTO> groupList = HomeConverter.toGroupName(groupNames);

        List<String> regionList = new ArrayList<>();
        regionList.add("내 주변");
        for(int i = 0; i < regions.size(); i++) {
            regionList.add(regions.get(i));
        }

        List<Store> storesWithDescription = storeRepository.findAllWithDescription();
        List<Store> steadySpots = new ArrayList<>();
        random.setSeed(System.currentTimeMillis());
        steadySpots.add(storesWithDescription.get(random.nextInt(storesWithDescription.size())));
        steadySpots.add(storesWithDescription.get(random.nextInt(storesWithDescription.size())));

        return HomeConverter.toGetHome(HomeConverter.toUpcommingSchduleDTO(upcomming), groupList, HomeConverter.toRegionDTOs(regionList), HomeConverter.toSpotDescriptionDTOs(steadySpots));
    }

    public HomeResponseDTO.entertainmentDTO getEntertainment(Long memberId, double latitude, double longitude, int selected, String sort) {
        List<String> categories = List.of("전체", "소품샵", "굿즈샵", "맛집", "카페", "테마카페", "팝업", "전시", "클래스");
        HomeResponseDTO.categoryDTO category = HomeConverter.toCategoryDTO(categories, selected);

        int sortId = 0;
        switch (sort) {
            case "default": sortId = 1; break;
            case "bookmark": sortId = 2; break;
            case "recommend": sortId = 3; break;
        }

        List<Store> stores = getStoreByConditions(categories, selected, sortId, latitude, longitude);

        List<Boolean> isFavors = new ArrayList<>();
        for(Store store : stores) {
            boolean isFavor = memberStoreRepository.existsByMemberIdAndStoreId(memberId, store.getId());
            isFavors.add(isFavor);
        }
        List<HomeResponseDTO.storeDTO> storeList = HomeConverter.toStoreDTO(isFavors, stores);

        return HomeConverter.toEntertainmentDTO(category, sort, storeList);
    }

    public HomeResponseDTO.groupStoreDTO getRegionStore(Long memberId, Long regionId, double latitude, double longitude, int selected, String sort) {
        List<Store> storesWithDescription = storeRepository.findAllWithDescription();
        random.setSeed(System.currentTimeMillis());
        Store best = storesWithDescription.get(random.nextInt(storesWithDescription.size()));
        Store ad = storesWithDescription.get(random.nextInt(storesWithDescription.size()));
        Store newStore = storeRepository.findLatestStoreWithContent();

        List<String> categories = List.of("전체", "소품샵", "굿즈샵", "맛집", "카페", "테마카페", "팝업", "전시", "클래스");
        HomeResponseDTO.categoryDTO category = HomeConverter.toCategoryDTO(categories, selected);

        int sortId = 0;
        switch (sort) {
            case "default": sortId = 1; break;
            case "bookmark": sortId = 2; break;
            case "recommend": sortId = 3; break;
        }

        List<Store> stores = getStoreByConditionsAndRegion(categories, regionId, selected, sortId, latitude, longitude);

        List<Boolean> isFavors = new ArrayList<>();
        for(Store store : stores) {
            boolean isFavor = memberStoreRepository.existsByMemberIdAndStoreId(memberId, store.getId());
            isFavors.add(isFavor);
        }
        List<HomeResponseDTO.storeDTO> storeList = HomeConverter.toStoreDTO(isFavors, stores);
        return HomeConverter.toRegionStoreDTO(HomeConverter.toSpotDescriptionDTO(best), HomeConverter.toSpotDescriptionDTO(ad), HomeConverter.toSpotDescriptionDTO(newStore), storeList);
    }

    private List<Store> getStoreByConditions(List<String> categories, int selected, int sortId, double latitude, double longitude) {
        if(selected == 0) {
            switch(sortId) {
                case 1: return storeRepository.findAllOrderDistance(latitude, longitude);
                case 2: return storeRepository.findAllOrderBookmark();
                case 3: return storeRepository.findAllOrderRecommend();
            }
        }
        else {
            switch(sortId) {
                case 1: return storeRepository.findCategoryOrderDistance(categories.get(selected), latitude, longitude);
                case 2: return storeRepository.findCategoryOrderBookmark(categories.get(selected));
                case 3: return storeRepository.findCategoryOrderRecommend(categories.get(selected));
            }
        }
        return new ArrayList<Store>();
    }

    private List<Store> getStoreByConditionsAndRegion(List<String> categories, long regionId, int selected, int sortId, double latitude, double longitude) {
        if(selected == 0) {
            switch(sortId) {
                case 1: return storeRepository.findAllOrderDistanceAndRegion(latitude, longitude, regionId);
                case 2: return storeRepository.findAllOrderBookmarkAndRegion(regionId);
                case 3: return storeRepository.findAllOrderRecommendAndRegion(regionId);
            }
        }
        else {
            switch(sortId) {
                case 1: return storeRepository.findCategoryOrderDistanceAndRegion(categories.get(selected), latitude, longitude, regionId);
                case 2: return storeRepository.findCategoryOrderBookmarkAndRegion(categories.get(selected), regionId);
                case 3: return storeRepository.findCategoryOrderRecommendAndRegion(categories.get(selected), regionId);
            }
        }
        return new ArrayList<Store>();
    }

    public HomeResponseDTO.myRouteDTO getRoute(Long memberId, String routeType) {
        List<Route> routes = new ArrayList<>();
        switch (routeType) {
            case "created" :
                routes = routeRepository.findByMember(memberRepository.findById(memberId).get());
                break;
            case "saved" :
                routes = memberRouteRepository.findByMember(memberRepository.findById(memberId).get());
        }

        List<String> images = new ArrayList<>();
        for(Route route : routes) {
            String pic = routeStoreRepository.findFirstImage(route);
            images.add(pic);
        }

        return HomeConverter.toMyRouteDTO(routes, images);
    }

    public HomeResponseDTO.groupStoreDTO getGroupStore(Long memberId, String groupName) {
        List<Store> storesWithDescription = storeRepository.findAllWithDescription();
        random.setSeed(System.currentTimeMillis());
        Store best = storesWithDescription.get(random.nextInt(storesWithDescription.size()));
        Store ad = storesWithDescription.get(random.nextInt(storesWithDescription.size()));
        Store newStore = storeRepository.findLatestStoreWithContent();

        List<Store> stores = storeRepository.findByGroupName(groupName);
        List<Boolean> isFavors = new ArrayList<>();
        for(Store store : stores) {
            boolean isFavor = memberStoreRepository.existsByMemberIdAndStoreId(memberId, store.getId());
            isFavors.add(isFavor);
        }
        return HomeConverter.toGroupStoreDTO(HomeConverter.toSpotDescriptionDTO(best), HomeConverter.toSpotDescriptionDTO(ad), HomeConverter.toSpotDescriptionDTO(newStore), stores, isFavors);
    }
}
