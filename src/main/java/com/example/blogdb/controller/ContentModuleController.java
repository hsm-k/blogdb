package com.example.blogdb.controller;

import com.example.blogdb.service.ContentModuleService;
import com.example.blogdb.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
public class ContentModuleController {
    private final ContentModuleService contentModuleService;
    private final PostService postService;

    public ContentModuleController(ContentModuleService contentModuleService, PostService postService) {
        this.contentModuleService = contentModuleService;
        this.postService = postService;
    }

    @GetMapping("/content/write")
    public String contentWriteForm(Model model) {
        model.addAttribute("posts", postService.getPosts());
        return "content-write";
    }

    @PostMapping("/content/text")
    @ResponseBody
    public ResponseEntity<Map<String, String>> saveTextContent(@RequestParam Long postId, @RequestParam String text) {
        try {
            contentModuleService.createTextModule(postId, text);
            return ResponseEntity.ok(Map.of("message", "텍스트 콘텐츠가 content_module 테이블에 저장되었습니다."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "텍스트 저장에 실패했습니다: " + e.getMessage()));
        }
    }

    @PostMapping("/content/image")
    @ResponseBody
    public ResponseEntity<Map<String, String>> saveImageContent(@RequestParam Long postId, @RequestParam("file") MultipartFile file) {
        try {
            contentModuleService.createImageModule(postId, file);
            return ResponseEntity.ok(Map.of("message", "이미지 콘텐츠가 content_module 테이블에 저장되었습니다."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "이미지 저장에 실패했습니다: " + e.getMessage()));
        }
    }
}
