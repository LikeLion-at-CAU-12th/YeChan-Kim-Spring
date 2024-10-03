package com.example.likelionspring.repository;

import com.example.likelionspring.domain.Article;
import com.example.likelionspring.domain.Articlelog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticlelogJpaRepository extends JpaRepository<Articlelog, Long> {
    Optional<Articlelog> findByArticle(Article article);
}
