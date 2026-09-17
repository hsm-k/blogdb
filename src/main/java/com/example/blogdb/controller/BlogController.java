package com.example.blogdb.controller;

import com.example.blogdb.dto.BlogTitleDTO;
import com.example.blogdb.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/")
    public String home(Model model) {
        BlogTitleDTO blogTitleDTO = blogService.getLatestTitle();
        model.addAttribute("blog", blogTitleDTO);

        return "home";
    }

    @GetMapping("/admin/login")
    public String adminLogin() {
        return "admin-login";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/save")
    public String save(@RequestParam String title) {

        blogService.saveTitle(title);

        return "save-complete";
    }

    @GetMapping("/latest")
    public String latest(Model model) {

        BlogTitleDTO blogTitleDTO = blogService.getLatestTitle();

        model.addAttribute("blog", blogTitleDTO);

        return "latest-title";
    }
}
