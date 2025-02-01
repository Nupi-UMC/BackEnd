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
    @PostMapping("/{storeId}/image")
    public ResponseEntity<String> uploadStoreImages(@PathVariable Long storeId,
                                                    @RequestParam("image") List<MultipartFile> imageFiles) throws IOException {
        storeImageService.uploadStoreImages(storeId, imageFiles);
        return ResponseEntity.ok("등록 완료");
    }
}
