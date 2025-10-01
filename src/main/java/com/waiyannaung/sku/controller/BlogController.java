package com.waiyannaung.sku.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

import com.waiyannaung.sku.model.domain.Article;
import com.waiyannaung.sku.model.service.BlogService;

@Controller
public class BlogController {
    @Autowired
    BlogService blogService; // 서비스 객체 주입

    @GetMapping("/article_list") // 게시판 링크 지정
    public String article_list(Model model) {
        List<Article> list = blogService.findAll(); // 게시판 리스트
        model.addAttribute("articles", list); // 모델에 추가
        return "article_list"; // .HTML 연결
    }
}
