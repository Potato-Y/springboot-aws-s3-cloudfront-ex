package org.example.awss3cloudfront.post.dto;

import org.example.awss3cloudfront.post.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        Long postId,
        String title,
        String content,
        LocalDateTime createAt,
        LocalDateTime updateAt,
        List<String> files
) {

    public static PostResponse from(Post post, List<String> files) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreateAt(),
                post.getUpdateAt(),
                files
        );
    }
}
