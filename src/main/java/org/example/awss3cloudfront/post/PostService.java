package org.example.awss3cloudfront.post;

import lombok.RequiredArgsConstructor;
import org.example.awss3cloudfront.aws.S3Service;
import org.example.awss3cloudfront.post.domain.Post;
import org.example.awss3cloudfront.post.domain.PostFile;
import org.example.awss3cloudfront.post.domain.PostFileRepository;
import org.example.awss3cloudfront.post.domain.PostRepository;
import org.example.awss3cloudfront.post.dto.CreatePostRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PostService {

    private final S3Service s3Service;
    private final PostRepository postRepository;
    private final PostFileRepository postFileRepository;

    @Transactional
    public Post save(CreatePostRequest request) {
        Post post = postRepository.save(Post.builder()
                .title(request.title())
                .content(request.content())
                .build());

        if (request.isFiles()) {
            ArrayList<PostFile> postFiles = new ArrayList<>();

            for (MultipartFile file : request.files()) {
                String path = post.getId().toString(); // {post_id}/image1.png 와 같은 형식이 되도록

                try {
                    String savePath = s3Service.uploadPostFile(file, path);

                    postFiles.add(postFileRepository.save(PostFile.builder()
                            .post(post)
                            .filePath(savePath)
                            .build()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            post.updatePostFiles(postFiles);
        }

        return post;
    }

    @Transactional(readOnly = true)
    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
