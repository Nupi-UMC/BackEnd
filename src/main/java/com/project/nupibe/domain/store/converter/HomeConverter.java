package com.project.nupibe.domain.store.converter;

import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.route.entity.Route;
import com.project.nupibe.domain.store.dto.response.HomeResponseDTO;
import com.project.nupibe.domain.store.entity.Store;

import java.util.ArrayList;
import java.util.List;

public class HomeConverter {
    public static HomeResponseDTO.GetHomeResponseDTO toGetHome(List<HomeResponseDTO.groupNameDTO> groups, List<HomeResponseDTO.regionDTO> regions) {
        return new HomeResponseDTO.GetHomeResponseDTO(groups, regions);
    }

    public static HomeResponseDTO.regionDTO toRegionDTO(int id, String name) {
        return HomeResponseDTO.regionDTO.builder()
                .regionId(id)
                .regionName(name).build();
    }

    public static List<HomeResponseDTO.groupNameDTO> toGroupName(List<String> names) {
        List<HomeResponseDTO.groupNameDTO> list = new ArrayList<>();
        for(String name : names) {
            list.add(new HomeResponseDTO.groupNameDTO(name));
        }
        return list;
    }

    public static HomeResponseDTO.entertainmentDTO toEntertainmentDTO(List<HomeResponseDTO.categoryDTO> category,
                                                                      String sort,
                                                                      List<HomeResponseDTO.storeDTO> stores) {
        return HomeResponseDTO.entertainmentDTO.builder()
                .category(category)
                .sort(sort)
                .stores(stores).build();
    }

    public static List<HomeResponseDTO.categoryDTO> toCategoryDTO(List<String> categories, int selected) {
        List<HomeResponseDTO.categoryDTO> list = new ArrayList<>();

        if(selected == 0) {
            int i = 1;
            for(String category : categories) {
                HomeResponseDTO.categoryDTO temp = HomeResponseDTO.categoryDTO.builder()
                        .cateogoryId(i)
                        .category(category)
                        .selected(true).build();
                list.add(temp);
            }
            return list;
        }
        else {
            int i = 1;
            for(String category : categories) {
                HomeResponseDTO.categoryDTO temp = HomeResponseDTO.categoryDTO.builder()
                        .cateogoryId(i)
                        .category(category)
                        .selected(i == (selected - 1)).build();
                list.add(temp);
            }
            return list;
        }
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
}
