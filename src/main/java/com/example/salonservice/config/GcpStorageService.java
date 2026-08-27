package com.example.salonservice.config;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GcpStorageService {

    private final Storage storage;

    @Value("${gcp.storage.bucket.name}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null || originalFileName.isBlank()) {
            originalFileName = "file";
        }

        // Generate unique filename
        String fileName = UUID.randomUUID() + "_" + originalFileName;

        // Create Blob ID
        BlobId blobId = BlobId.of(bucketName, fileName);

        // Create Blob information
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(
                        file.getContentType() != null
                                ? file.getContentType()
                                : "application/octet-stream"
                )
                .build();

        // Upload file to Google Cloud Storage
        storage.create(blobInfo, file.getBytes());

        // Return public/storage URL
        return String.format(
                "https://storage.googleapis.com/%s/%s",
                bucketName,
                fileName
        );
    }
}

