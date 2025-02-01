package com.project.nupibe.domain.store.controller;

import com.project.nupibe.domain.store.service.StoreImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreImageController {

    private final StoreImageService storeImageService;

    // 1. storeId로 Store의 image 필드에 이미지 1장 추가
    @PostMapping("/{storeId}/i")
    public ResponseEntity<String> updateStoreImage(@PathVariable Long storeId,
                                                   @RequestParam("image") MultipartFile imageFile) throws IOException {
        storeImageService.updateStoreImage(storeId, imageFile);
        return ResponseEntity.ok("Store image updated successfully.");
    }

    // 2. StoreImage에 MAIN 사진 추가
    @PostMapping("/{storeId}/m")
    public ResponseEntity<String> addMainImage(@PathVariable Long storeId,
                                               @RequestParam("image") List<MultipartFile> mainImageFile) throws IOException {
        storeImageService.addMainImages(storeId, mainImageFile);
        return ResponseEntity.ok("Main image added successfully.");
    }

    // 3. StoreImage에 TAB 사진 여러 장 추가
    @PostMapping("/{storeId}/t")
    public ResponseEntity<String> addTabImages(@PathVariable Long storeId,
                                               @RequestParam("image") List<MultipartFile> tabImageFiles) throws IOException {
        storeImageService.addTabImages(storeId, tabImageFiles);
        return ResponseEntity.ok("Tab images added successfully.");
    }
}


