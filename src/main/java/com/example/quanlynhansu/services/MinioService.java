package com.example.quanlynhansu.services;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.url}") // URL MinIO, ví dụ: http://localhost:9000
    private String minioUrl;

    // Constructor tự tạo thay vì dùng @RequiredArgsConstructor của Lombok
    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    // upload file lên minIO
    public String uploadFile(MultipartFile file) {
        try {
            // Tạo tên file duy nhất
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // Lưu file lên MinIO
            InputStream fileStream = file.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(fileStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            // Trả về URL vĩnh viễn để truy cập file
            return minioUrl + "/" + bucketName + "/" + fileName;

        } catch (MinioException e) {
            throw new RuntimeException("Lỗi khi upload file lên MinIO: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Lỗi không xác định khi upload file lên MinIO", e);
        }
    }

    // xóa file trên minIO
    public void deleteFile(String bucketName, String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error deleting file from MinIO", e);
        }
    }


}
