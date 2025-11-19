package com.waiyannaung.sku.model.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.waiyannaung.sku.model.domain.Article;
import com.waiyannaung.sku.model.domain.Board;
import com.waiyannaung.sku.model.repository.BlogRepository;
import com.waiyannaung.sku.model.repository.BoardRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // 생성자 자동 생성(부분)
public class BlogService {
    @Autowired // 객체 주입 자동화, 생성자 1개면 생략 가능
    private final BlogRepository blogRepository;
    private final BoardRepository blogRepository2;
    // 리포지토리 선언

    // public List<Board> findAll() { // 게시판 전체 목록 조회
    // return blogRepository2.findAll();
    // }

    public Optional<Board> findById(Long id) { // 게시판 특정 글 조회
        return blogRepository2.findById(id);
    }

    // public List<Article> findAll() { // 게시판 전체 목록 조회
    // return blogRepository.findAll();
    // }

    // public Article save(AddArticleRequest request) {
    // // DTO가 없는 경우 이곳에 직접 구현 가능
    // // public ResponseEntity<Article> addArticle(@RequestParam String title,
    // // @RequestParam String content) {
    // // Article article = Article.builder()
    // // .title(title)
    // // .content(content)
    // // .build();
    // return blogRepository.save(request.toEntity());
    // }

    // public Optional<Article> findById(Long id) {
    // return blogRepository.findById(id);
    // }

    public Board save(AddArticleRequest request) {
        // DTO가 없는 경우 이곳에 직접 구현 가능
        return blogRepository2.save(request.toEntity());
    }

    public Page<Board> findAll(Pageable pageable) {
        return blogRepository2.findAll(pageable);
    }

    public Page<Board> searchByKeyword(String keyword, PageRequest pageable) {
        return blogRepository2.findByTitleContainingIgnoreCase(keyword, pageable);
    }

    public void update(Long id, AddArticleRequest request) {
        Optional<Article> optionalArticle = blogRepository.findById(id); // 단일 글 조회
        optionalArticle.ifPresent(article -> { // 값이 있으면
            article.update(request.getTitle(), request.getContent()); // 값을 수정
            blogRepository.save(article); // Article 객체에 저장
        });
    }

    public void delete(Long id) {
        blogRepository.deleteById(id);
    }

}
