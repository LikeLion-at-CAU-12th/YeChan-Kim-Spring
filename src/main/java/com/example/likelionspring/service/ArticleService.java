package com.example.likelionspring.service;

import com.example.likelionspring.domain.*;
import com.example.likelionspring.dto.request.ArticleCreateRequestDto;
import com.example.likelionspring.dto.request.ArticleUpdateRequest;
import com.example.likelionspring.dto.response.ArticleResponseDto;
import com.example.likelionspring.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ArticleJpaRepository articleJpaRepository;
    @Autowired
    private CategoryArticleJpaRepository categoryArticleJpaRepository;
    @Autowired
    private ArticlelogJpaRepository articlelogJpaRepository;
    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Transactional
    public Long createArticle(ArticleCreateRequestDto requestDto){
        Member member = memberRepository.findById(requestDto.getMemberId()).orElseThrow(() -> new RuntimeException("해당 아이디를 가진 회원이 존재하지 않습니다."));
        Article article = Article.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .member(member)
                .comments(new ArrayList<>())
                .build();
        articleJpaRepository.save(article);

        Articlelog articlelog = Articlelog.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .article(article)
                .build();
        articlelogJpaRepository.save(articlelog);

        List<Long> categoryIds = requestDto.getCategoryIds();
        if (categoryIds != null && !categoryIds.isEmpty()){
            for (Long categoryId : categoryIds) {
                Category category = categoryJpaRepository.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("해당 ID를 가진 카테고리가 없습니다."));

                CategoryArticle categoryArticle = CategoryArticle.builder()
                        .category(category)
                        .article(article)
                        .build();

                categoryArticleJpaRepository.save(categoryArticle);
            }
        }
        return article.getId();
    }

    public List<ArticleResponseDto> findArticlesByMemberId(Long memberId){
        List<Article> articles = articleJpaRepository.findByMemberId(memberId);
        return articles.stream()
                .map(article -> new ArticleResponseDto(article.getId(), article.getTitle(), article.getContent()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ArticleResponseDto updateArticle(Long articleId, ArticleUpdateRequest request) {
        // 기존 Article 엔티티 조회
        Article existingArticle = articleJpaRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 게시물이 없습니다."));

        // 로그 생성 (변경 전 데이터 사용)
        Articlelog articlelog = Articlelog.builder()
                .title(existingArticle.getTitle())  // 수정 전 제목
                .content(existingArticle.getContent())  // 수정 전 내용
                .article(existingArticle)
                .build();
        articlelogJpaRepository.save(articlelog);

        // Article 업데이트
        if (request.getTitle() != null) {
            existingArticle.updateTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            existingArticle.updateContent(request.getContent());
        }

        // Article 저장 (한 번만 수행)
        articleJpaRepository.save(existingArticle);

        // 카테고리 관계 업데이트
        List<CategoryArticle> existingCategoryArticles = categoryArticleJpaRepository.findByArticle(existingArticle);
        categoryArticleJpaRepository.deleteAll(existingCategoryArticles);

        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            for (Long categoryId : request.getCategoryIds()) {
                Category category = categoryJpaRepository.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("해당 ID를 가진 카테고리가 없습니다."));
                CategoryArticle categoryArticle = CategoryArticle.builder()
                        .category(category)
                        .article(existingArticle)
                        .build();
                categoryArticleJpaRepository.save(categoryArticle);
            }
        }

        return new ArticleResponseDto(existingArticle.getId(), existingArticle.getTitle(), existingArticle.getContent());
    }


    @Transactional
    public void deleteArticle(Long articleId) {
        Article article = articleJpaRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 게시물이 없습니다."));

        // CategoryArticle 삭제
        categoryArticleJpaRepository.deleteByArticle(article);

        // Articlelog 삭제
        articlelogJpaRepository.deleteByArticle(article);

        // Article 삭제
        articleJpaRepository.delete(article);
    }

}
