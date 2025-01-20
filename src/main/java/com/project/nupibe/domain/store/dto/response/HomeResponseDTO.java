package com.project.nupibe.domain.store.dto.response;

import lombok.Builder;
import java.util.List;

public class HomeResponseDTO {
    @Builder
    public record GetHomeResponseDTO(
            List<groupNameDTO> groupList,
            List<regionDTO> regions
    ) {}

    @Builder
    public record groupNameDTO(
            String groupName
    ) {}

    @Builder
    public record regionDTO(
            int regionId,
            String regionName
    ){};
}
