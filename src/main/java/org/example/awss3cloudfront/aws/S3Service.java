package org.example.awss3cloudfront.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String uploadPostFile(MultipartFile file, String path) throws IOException {
        String fileName = this.getFileName(file, path);
        ObjectMetadata objectMetadata = this.getObjectMetadata(file);

        PutObjectRequest request = new PutObjectRequest(
                bucketName, fileName, file.getInputStream(), objectMetadata
        );

        s3Client.putObject(request);
        return fileName;
    }

    private String getFileName(MultipartFile file, String path) {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        return path + "/" + UUID.randomUUID() + extension;
    }

    private ObjectMetadata getObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();

        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        return objectMetadata;
    }

    public void deleteFolder(String folderPath) {
        if (!folderPath.endsWith("/")) {
            folderPath += "/";
        }

        ObjectListing objectListing = s3Client.listObjects(bucketName, folderPath); // 목록 가져오기

        // 모든 객체 삭제
        while (true) {
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                s3Client.deleteObject(bucketName, objectSummary.getKey());
            }

            // 다음 페이지가 있으면 계속 진행
            if (objectListing.isTruncated()) {
                objectListing = s3Client.listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
        }
    }
}
