package com.example.likelionspring.service;

import com.example.likelionspring.domain.*;
import com.example.likelionspring.dto.request.ArticleCreateRequestDto;
import com.example.likelionspring.dto.request.ArticleUpdateRequest;
import com.example.likelionspring.dto.response.ArticleResponseDto;
import com.example.likelionspring.repository.*;
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
    public Long updateArticle(Long articleId, ArticleUpdateRequest request) {
        // 기존 Article 엔티티 조회
        Article existingArticle = articleJpaRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 게시물이 없습니다."));

        // 기존 Article 엔티티의 title과 content 필드만 업데이트 (comments 컬렉션은 변경하지 않음)
        existingArticle.setTitle(request.getTitle());
        existingArticle.setContent(request.getContent());

        // 변경 사항을 저장
        articleJpaRepository.save(existingArticle);

        return existingArticle.getId();
    }

    @Transactional
    public void deleteArticle(Long articleId) {
        Article article = articleJpaRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 게시물이 없습니다."));

        List<CategoryArticle> categoryArticles = categoryArticleJpaRepository.findByArticle(article);

        if (!categoryArticles.isEmpty()) {
            categoryArticleJpaRepository.deleteAll();
        }

        articlelogJpaRepository.deleteByArticle(article);

        articleJpaRepository.delete(article);
    }

}
