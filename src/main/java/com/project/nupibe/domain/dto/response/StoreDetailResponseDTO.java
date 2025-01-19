package com.project.nupibe.domain.dto.response;

import lombok.Builder;

@Builder
public record StoreDetailResponseDTO (
    Long store_id,
    String name,
    String content,
    String image,
    String category,
    Integer like_num,
    Integer bookmark_num){
}
