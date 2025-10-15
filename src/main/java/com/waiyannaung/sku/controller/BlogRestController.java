package com.waiyannaung.sku.controller;

import org.springframework.web.bind.annotation.RestController;
import com.waiyannaung.sku.model.domain.Article;
import com.waiyannaung.sku.model.service.AddArticleRequest;
import com.waiyannaung.sku.model.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController // @Controller + @ResponseBody
public class BlogRestController {
    private final BlogService blogService;

    @PostMapping("/api/articles") // post 요청
    public ResponseEntity<Article> addArticle(@ModelAttribute AddArticleRequest request) { // 아직 없음(에러)
        Article saveArticle = blogService.save(request); // 게시글 저장
        return ResponseEntity.status(HttpStatus.CREATED) // 상태 코드 및 게시글 정보 반환
                .body(saveArticle);
    }
}
