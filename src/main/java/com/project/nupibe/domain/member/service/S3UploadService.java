package com.project.nupibe.domain.member.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
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

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 파일 업로드 시 이름을 증가시키기 위한 변수
    private long fileIndex = 1;

    @Transactional
    public String saveFile(MultipartFile multipartFile) throws IOException { // 파일 업로드
        // 증가시킬 파일 이름 생성
        String customFileName = "image_" + fileIndex++ + ".jpg";  // 예: image_1.jpg, image_2.jpg, ...

        // ObjectMetadata 객체를 사용하여 파일 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        // 파일을 S3에 업로드 (새로운 파일명으로 저장)
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
