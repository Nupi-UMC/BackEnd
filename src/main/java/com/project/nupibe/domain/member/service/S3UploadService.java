package com.project.nupibe.domain.member.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.nupibe.domain.store.repository.StoreImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3Client amazonS3Client;
    private final StoreImageRepository storeImageRepository; // StoreImage 테이블 조회를 위한 Repository

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public String saveFile(MultipartFile multipartFile) throws IOException { // 파일 업로드

        long fileCount = storeImageRepository.count();

        String customFileName = "image_" + (fileCount + 19) + ".jpg";
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3Client.putObject(bucket, customFileName, multipartFile.getInputStream(), metadata);

        return amazonS3Client.getUrl(bucket, customFileName).toString();
    }

    @Transactional
    public ResponseEntity<UrlResource> downloadImage(String originalFilename) { // 파일 다운로드
        UrlResource urlResource = new UrlResource(amazonS3Client.getUrl(bucket, originalFilename));

        String contentDisposition = "attachment; filename=\"" + originalFilename + "\"";

        // header에 CONTENT_DISPOSITION 설정을 통해 클릭 시 다운로드 진행
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }
}
