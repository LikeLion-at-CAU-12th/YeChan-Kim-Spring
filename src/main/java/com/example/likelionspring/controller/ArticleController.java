package com.example.likelionspring.controller;

import com.example.likelionspring.dto.request.ArticleCreateRequestDto;
import com.example.likelionspring.dto.request.ArticleUpdateRequest;
import com.example.likelionspring.dto.response.ArticleResponseDto;
import com.example.likelionspring.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller

@RestController
@RequestMapping("/api/v1/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("")
    public ResponseEntity<Long> createArticle(@RequestBody ArticleCreateRequestDto requestDto){
        Long articleId = articleService.createArticle(requestDto);
        return new ResponseEntity<>(articleId, HttpStatus.CREATED);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ArticleResponseDto>> getArticlesByMemberId(@PathVariable Long memberId){
        List<ArticleResponseDto> articles = articleService.findArticlesByMemberId(memberId);
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<Long> updateArticle(@PathVariable Long articleId, @RequestBody ArticleUpdateRequest request){
        Long updatedArticleId = articleService.updateArticle(articleId, request);
        return ResponseEntity.ok(updatedArticleId);
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long articleId) {
        articleService.deleteArticle(articleId);
        return ResponseEntity.noContent().build();
    }
}
