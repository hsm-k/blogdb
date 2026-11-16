package com.example.blogdb.controller;

import com.example.blogdb.dto.CommentRequestDTO;
import com.example.blogdb.dto.PostDTO;
import com.example.blogdb.dto.PostRequestDTO;
import com.example.blogdb.service.CommentService;
import com.example.blogdb.service.ContentModuleService;
import com.example.blogdb.service.PostService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class PostController {
    private final PostService postService;
    private final ContentModuleService contentModuleService;
    private final CommentService commentService;

    public PostController(PostService postService,
                          ContentModuleService contentModuleService,
                          CommentService commentService) {
        this.postService = postService;
        this.contentModuleService = contentModuleService;
        this.commentService = commentService;
    }

    // 일반 사용자용 게시글 목록 화면
    @GetMapping("/posts")
    public String publicPostList(Model model) {
        model.addAttribute("posts", postService.getPosts());
        return "post-list";
    }

    // 일반 사용자용 게시글 상세 화면 및 댓글 목록 조회
    @GetMapping("/posts/{id}")
    public String publicPostDetail(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("post", postService.getPost(id));
            model.addAttribute("contentModules", contentModuleService.getContentModules(id));
            model.addAttribute("comments", commentService.getComments(id));
            return "post-detail";
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.", e);
        }
    }

    // 댓글 작성 요청 처리
    @PostMapping("/posts/{id}/comments")
    public String addComment(@PathVariable Long id,
                             @ModelAttribute CommentRequestDTO request,
                             RedirectAttributes redirectAttributes) {
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "댓글 내용을 입력해주세요.");
            return "redirect:/posts/" + id;
        }

        try {
            commentService.createComment(id, request);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/posts/" + id;
    }

    @GetMapping("/admin/posts")
    public String postList(Model model) {
        model.addAttribute("posts", postService.getPosts());
        return "admin-post-list";
    }

    @GetMapping("/admin/posts/create")
    public String postCreateForm() {
        return "admin-post-form";
    }

    @GetMapping("/content/image/{id}")
    public ResponseEntity<byte[]> showImage(@PathVariable Long id) {
        PostDTO post = postService.getPost(id);

        if (!post.isImageDataPresent()) {
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType = MediaType.parseMediaType(post.getImageContentType());
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(post.getImageData());
    }

    @PostMapping("/admin/posts/create")
    public String createPost(@ModelAttribute PostRequestDTO postRequestDTO,
                             @RequestParam(required = false) MultipartFile imageFile,
                             @RequestParam(required = false) String imageUrl,
                             @RequestParam(required = false, name = "moduleTypes") java.util.List<String> moduleTypes,
                             @RequestParam(required = false, name = "moduleTexts") java.util.List<String> moduleTexts,
                             @RequestParam(required = false, name = "moduleFiles") java.util.List<MultipartFile> moduleFiles) {
        setImageContent(postRequestDTO, imageFile, imageUrl);
        PostDTO post = postService.createPost(postRequestDTO);

        // 동적으로 추가된 콘텐츠 모듈 블록들을 순서대로 저장
        if (moduleTypes != null) {
            int fileIndex = 0;
            for (int i = 0; i < moduleTypes.size(); i++) {
                String type = moduleTypes.get(i);
                if ("TEXT".equals(type)) {
                    String text = (moduleTexts != null && i < moduleTexts.size()) ? moduleTexts.get(i) : "";
                    if (text != null && !text.trim().isEmpty()) {
                        contentModuleService.createTextModule(post.getId(), text);
                    }
                } else if ("IMAGE".equals(type)) {
                    if (moduleFiles != null && fileIndex < moduleFiles.size()) {
                        MultipartFile file = moduleFiles.get(fileIndex);
                        fileIndex++;
                        if (file != null && !file.isEmpty()) {
                            contentModuleService.createImageModule(post.getId(), file);
                        }
                    }
                }
            }
        }

        return "redirect:/admin/posts/" + post.getId();
    }

    @GetMapping("/admin/posts/{id}")
    public String postDetail(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.getPost(id));
        model.addAttribute("contentModules", contentModuleService.getContentModules(id));
        model.addAttribute("comments", commentService.getComments(id));
        return "admin-post-detail";
    }

    @PostMapping("/admin/posts/{id}/contents/text")
    public String createTextModule(@PathVariable Long id, @RequestParam String text) {
        contentModuleService.createTextModule(id, text);
        return "redirect:/admin/posts/" + id;
    }

    @PostMapping("/admin/posts/{id}/contents/image")
    public String createImageModule(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        contentModuleService.createImageModule(id, file);
        return "redirect:/admin/posts/" + id;
    }

    @PostMapping("/admin/posts/{id}/contents/{moduleId}/up")
    public String moveContentModuleUp(@PathVariable Long id, @PathVariable Long moduleId) {
        contentModuleService.moveContentModule(id, moduleId, -1);
        return "redirect:/admin/posts/" + id;
    }

    @PostMapping("/admin/posts/{id}/contents/{moduleId}/down")
    public String moveContentModuleDown(@PathVariable Long id, @PathVariable Long moduleId) {
        contentModuleService.moveContentModule(id, moduleId, 1);
        return "redirect:/admin/posts/" + id;
    }

    @GetMapping("/admin/posts/{id}/edit")
    public String postEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.getPost(id));
        return "admin-post-edit";
    }

    @PostMapping("/admin/posts/{id}/edit")
    public String updatePost(@PathVariable Long id,
                             @ModelAttribute PostRequestDTO postRequestDTO,
                             @RequestParam(required = false) MultipartFile imageFile,
                             @RequestParam(required = false) String imageUrl) {
        setImageContent(postRequestDTO, imageFile, imageUrl);
        postService.updatePost(id, postRequestDTO);
        return "redirect:/admin/posts/" + id;
    }

    @PostMapping("/admin/posts/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/admin/posts";
    }

    private void setImageContent(PostRequestDTO postRequestDTO, MultipartFile imageFile, String imageUrl) {
        if (!"IMAGE".equals(postRequestDTO.getContentType())) {
            return;
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            setImageData(postRequestDTO, imageFile);
        } else {
            postRequestDTO.setContent(imageUrl);
        }
    }

    private void setImageData(PostRequestDTO postRequestDTO, MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일을 선택해야 합니다.");
        }

        if (imageFile.getContentType() == null || !imageFile.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }

        try {
            postRequestDTO.setContent(imageFile.getOriginalFilename());
            postRequestDTO.setImageData(imageFile.getBytes());
            postRequestDTO.setImageContentType(imageFile.getContentType());
        } catch (IOException e) {
            throw new IllegalStateException("이미지 파일을 읽지 못했습니다.", e);
        }
    }
}
