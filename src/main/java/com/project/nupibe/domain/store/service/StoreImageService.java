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
    private final S3UploadService s3UploadService;

    public void uploadStoreImages(Long storeId, List<MultipartFile> imageFiles) throws IOException {
        if (imageFiles == null || imageFiles.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일이 없습니다.");
        }

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));

        List<StoreImage> mainImages = new ArrayList<>();
        List<StoreImage> tabImages = new ArrayList<>();

        for (int i = 1; i < Math.min(imageFiles.size(), 4); i++) {
            String mainImageUrl = s3UploadService.saveFile(imageFiles.get(i));

            StoreImage mainImage = StoreImage.builder()
                    .store(store)
                    .imageUrl(mainImageUrl)
                    .type(ImageType.MAIN)
                    .build();
            mainImages.add(mainImage);
        }
        storeImageRepository.saveAll(mainImages);

        for (int i = 0; i < imageFiles.size(); i++) {
            String tabImageUrl = s3UploadService.saveFile(imageFiles.get(i));

            StoreImage tabImage = StoreImage.builder()
                    .store(store)
                    .imageUrl(tabImageUrl)
                    .type(ImageType.TAB)
                    .build();
            tabImages.add(tabImage);


            if (i == 0) {
                store.setImage(tabImageUrl);
                storeRepository.save(store);
            }
        }

        // 저장: TAB 이미지 저장
        storeImageRepository.saveAll(tabImages);
    }
}
