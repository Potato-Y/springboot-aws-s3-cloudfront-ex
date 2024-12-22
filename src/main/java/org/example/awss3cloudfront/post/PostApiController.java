package org.example.awss3cloudfront.post;

import lombok.RequiredArgsConstructor;
import org.example.awss3cloudfront.aws.CloudFrontService;
import org.example.awss3cloudfront.post.domain.Post;
import org.example.awss3cloudfront.post.dto.CreatePostRequest;
import org.example.awss3cloudfront.post.dto.PostResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostApiController {

    private final PostService postService;
    private final CloudFrontService cloudFrontService;

    @PostMapping("")
    public ResponseEntity<PostResponse> createPost(@Validated @ModelAttribute CreatePostRequest request) {
        Post post = postService.save(request);

        List<String> urls = getFileUrls(post);

        return ResponseEntity.status(HttpStatus.CREATED).body(PostResponse.from(post, urls));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        Post post = postService.findById(postId);
        List<String> urls = getFileUrls(post);

        return ResponseEntity.status(HttpStatus.OK).body(PostResponse.from(post, urls));
    }

    private List<String> getFileUrls(Post post) {
        List<String> urls = new ArrayList<>();
        post.getPostFiles().forEach(file -> {
            try {
                urls.add(cloudFrontService.generateSignedUrl(file.getFilePath()));
            } catch (InvalidKeySpecException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        return urls;
    }
}
