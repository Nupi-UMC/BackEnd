package com.project.nupibe.domain.store.converter;

import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.region.entity.Region;
import com.project.nupibe.domain.route.entity.Route;
import com.project.nupibe.domain.store.dto.response.HomeResponseDTO;
import com.project.nupibe.domain.store.entity.Store;

import java.util.ArrayList;
import java.util.List;

public class HomeConverter {
    public static HomeResponseDTO.GetHomeResponseDTO toGetHome(HomeResponseDTO.UpcommingScheduleDTO upcomming, List<HomeResponseDTO.groupNameDTO> groups, List<HomeResponseDTO.regionDTO> regions, List<HomeResponseDTO.SpotDescription> steadySpots) {
        return new HomeResponseDTO.GetHomeResponseDTO(upcomming, groups, regions, steadySpots);
    }

    public static HomeResponseDTO.UpcommingScheduleDTO toUpcommingSchduleDTO(Route route) {
        if(route == null) return null;
        return HomeResponseDTO.UpcommingScheduleDTO.builder()
                .title(route.getRouteName())
                .date(route.getCreatedAt().toString())
                .build();
    }

    public static List<HomeResponseDTO.regionDTO> toRegionDTOs(List<String> regionNameList) {
        List<HomeResponseDTO.regionDTO> regionDTOList = new ArrayList<>();
        int i = 0;
        for(String region : regionNameList) {
            HomeResponseDTO.regionDTO temp = HomeResponseDTO.regionDTO.builder()
                    .regionId(i).regionName(region).build();
            i++;
            regionDTOList.add(temp);
        }
        return regionDTOList;
    }

    public static List<HomeResponseDTO.groupNameDTO> toGroupName(List<String> names) {
        List<HomeResponseDTO.groupNameDTO> list = new ArrayList<>();
        for(String name : names) {
            list.add(new HomeResponseDTO.groupNameDTO(name));
        }
        return list;
    }

    public static HomeResponseDTO.entertainmentDTO toEntertainmentDTO(HomeResponseDTO.categoryDTO category,
                                                                      String sort,
                                                                      List<HomeResponseDTO.storeDTO> stores) {
        return HomeResponseDTO.entertainmentDTO.builder()
                .category(category)
                .sort(sort)
                .stores(stores).build();
    }

    public static HomeResponseDTO.categoryDTO toCategoryDTO(List<String> categories, int selected) {
        return HomeResponseDTO.categoryDTO.builder()
                .category(categories.get(selected))
                .cateogoryId(selected).build();
    }

    public static List<HomeResponseDTO.storeDTO> toStoreDTO(List<Boolean> isFavor, List<Store> stores) {
        List<HomeResponseDTO.storeDTO> list = new ArrayList<>();

        int i = 0;
        for(Store store : stores) {
            HomeResponseDTO.storeDTO temp = HomeResponseDTO.storeDTO.builder()
                    .storeId(store.getId())
                    .storeName(store.getName())
                    .storePic(store.getImage())
                    .storePlace(store.getLocation())
                    .saved(isFavor.get(i)).build();
            list.add(temp);
            i++;
        }
        return list;
    }

    public static HomeResponseDTO.myRouteDTO toMyRouteDTO(List<Route> routes, List<String> images) {
        List<HomeResponseDTO.routesDTO> list = new ArrayList<>();

        for(int i = 0; i < routes.size(); i++) {
            HomeResponseDTO.routesDTO route = HomeResponseDTO.routesDTO.builder()
                    .routeId(routes.get(i).getId())
                    .routeName(routes.get(i).getRouteName())
                    .routeLocation(routes.get(i).getLocation())
                    .routePic(images.get(i)).build();
            list.add(route);
        }

        return HomeResponseDTO.myRouteDTO.builder()
                .routes(list).build();
    }

    public static HomeResponseDTO.groupStoreDTO toGroupStoreDTO(List<Store> stores, List<Boolean> isFavors) {
        List<HomeResponseDTO.storeDTO> list = new ArrayList<>();

        for(int i = 0; i < stores.size(); i++) {
            HomeResponseDTO.storeDTO store = HomeResponseDTO.storeDTO.builder()
                    .storeId(stores.get(i).getId())
                    .storeName(stores.get(i).getName())
                    .storePlace(stores.get(i).getLocation())
                    .storePic(stores.get(i).getImage())
                    .saved(isFavors.get(i)).build();
            list.add(store);
        }
        return HomeResponseDTO.groupStoreDTO.builder()
                .stores(list).build();
    }

    public static HomeResponseDTO.savedDTO save(Long storeId, boolean save) {
        return HomeResponseDTO.savedDTO.builder()
                .storeId(storeId)
                .saved(save).build();
    }

    public static HomeResponseDTO.groupStoreDTO toRegionStoreDTO(List<HomeResponseDTO.storeDTO> stores) {
        return HomeResponseDTO.groupStoreDTO.builder()
                .stores(stores)
                .build();
    }

    public static List<HomeResponseDTO.SpotDescription> toSpotDescriptionDTOs(List<Store> stores) {
        List<HomeResponseDTO.SpotDescription> list = new ArrayList<>();
        for(Store store : stores) {
            HomeResponseDTO.SpotDescription temp = HomeResponseDTO.SpotDescription.builder()
                    .name(store.getName())
                    .place(store.getRegion().getName())
                    .location(store.getLocation())
                    .description(store.getContent()).build();
            list.add(temp);
        }
        return list;
    }
}
