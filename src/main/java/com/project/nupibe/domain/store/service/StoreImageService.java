package com.project.nupibe.domain.store.service;

import com.project.nupibe.domain.member.service.S3UploadService;
import com.project.nupibe.domain.store.entity.ImageType;
import com.project.nupibe.domain.store.entity.Store;
import com.project.nupibe.domain.store.entity.StoreImage;
import com.project.nupibe.domain.store.repository.StoreImageRepository;
import com.project.nupibe.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class StoreImageService {

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    private final S3UploadService s3UploadService;  // S3UploadService 주입

    // 1. storeId로 Store의 image 필드에 이미지 1장 추가
    public void updateStoreImage(Long storeId, MultipartFile imageFile) throws IOException {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));

        // S3에 파일 업로드하고 URL을 가져옵니다.
        String imageUrl = s3UploadService.saveFile(imageFile);

        // Store의 image 필드에 S3 URL 저장
        store.setImage(imageUrl);

        // 변경 사항 저장
        storeRepository.save(store);
    }

    // 2. StoreImage에 MAIN 사진 여러 장 추가
    public void addMainImages(Long storeId, List<MultipartFile> mainImageFiles) throws IOException {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));

        List<StoreImage> mainImages = new ArrayList<>();
        for (MultipartFile mainImageFile : mainImageFiles) {
            // S3에 파일 업로드하고 URL을 가져옵니다.
            String mainImageUrl = s3UploadService.saveFile(mainImageFile);

            StoreImage mainImage = StoreImage.builder()
                    .store(store)
                    .imageUrl(mainImageUrl) // S3 URL을 저장
                    .type(ImageType.MAIN)
                    .build();
            mainImages.add(mainImage);
        }

        storeImageRepository.saveAll(mainImages);
    }

    // 3. StoreImage에 TAB 사진 여러 장 추가
    public void addTabImages(Long storeId, List<MultipartFile> tabImageFiles) throws IOException {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));

        List<StoreImage> tabImages = new ArrayList<>();
        for (MultipartFile tabImageFile : tabImageFiles) {
            // S3에 파일 업로드하고 URL을 가져옵니다.
            String tabImageUrl = s3UploadService.saveFile(tabImageFile);

            StoreImage tabImage = StoreImage.builder()
                    .store(store)
                    .imageUrl(tabImageUrl) // S3 URL을 저장
                    .type(ImageType.TAB)
                    .build();
            tabImages.add(tabImage);
        }

        storeImageRepository.saveAll(tabImages);
    }
}

