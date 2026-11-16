package com.example.blogdb.service;

import com.example.blogdb.Post;
import com.example.blogdb.dto.PostDTO;
import com.example.blogdb.dto.PostRequestDTO;
import com.example.blogdb.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public List<PostDTO> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(this::toPostDTO).toList();
    }

    @Transactional(readOnly = true)
    public PostDTO getPost(Long id) {
        return toPostDTO(findPost(id));
    }

    public PostDTO createPost(PostRequestDTO request) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setCategory(request.getCategory());
        post.setContentType(request.getContentType());
        post.setContent(request.getContent());
        post.setImageData(request.getImageData());
        post.setImageContentType(request.getImageContentType());
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        return toPostDTO(postRepository.save(post));
    }

    public void updatePost(Long id, PostRequestDTO request) {
        Post post = findPost(id);
        post.setTitle(request.getTitle());
        post.setCategory(request.getCategory());
        post.setContentType(request.getContentType());
        post.setContent(request.getContent());
        post.setImageData(request.getImageData());
        post.setImageContentType(request.getImageContentType());
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.delete(findPost(id));
    }

    private Post findPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }

    private PostDTO toPostDTO(Post post) {
        return new PostDTO(post.getId(), post.getTitle(), post.getCategory(), post.getContentType(), post.getContent(), post.getImageData(), post.getImageContentType(), post.getCreatedAt(), post.getUpdatedAt());
    }
}
