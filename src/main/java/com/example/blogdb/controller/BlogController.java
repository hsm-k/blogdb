package com.example.blogdb.controller;

import com.example.blogdb.dto.BlogTitleDTO;
import com.example.blogdb.service.BlogService;
import com.example.blogdb.service.GameProjectService;
import com.example.blogdb.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BlogController {

    private final BlogService blogService;
    private final PostService postService;
    private final GameProjectService gameProjectService;

    public BlogController(BlogService blogService, PostService postService, GameProjectService gameProjectService) {
        this.blogService = blogService;
        this.postService = postService;
        this.gameProjectService = gameProjectService;
    }

    @GetMapping("/")
    public String home(Model model) {
        BlogTitleDTO blogTitle = blogService.getLatestTitle();
        model.addAttribute("blog", blogTitle);
        model.addAttribute("posts", postService.getPosts());
        model.addAttribute("projects", gameProjectService.getProjects());
        return "home";
    }

    @GetMapping("/save")
    public String save(@RequestParam String title) {
        blogService.saveTitle(title);
        return "save-complete";
    }

    @GetMapping("/latest")
    public String latest(Model model) {
        model.addAttribute("blog", blogService.getLatestTitle());
        return "latest-title";
    }
}
