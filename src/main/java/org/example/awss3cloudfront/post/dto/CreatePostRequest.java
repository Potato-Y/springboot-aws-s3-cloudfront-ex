package org.example.awss3cloudfront.post.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreatePostRequest(
        @NotNull String title,
        @NotNull String content,
        List<MultipartFile> files
) {

    public boolean isFiles() {
        return files != null && !files.isEmpty();
    }
}
