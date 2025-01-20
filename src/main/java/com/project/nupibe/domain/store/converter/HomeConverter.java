package com.project.nupibe.domain.store.converter;

import com.project.nupibe.domain.store.dto.response.HomeResponseDTO;

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
}
