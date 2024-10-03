package com.example.likelionspring.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Articlelog {
    @Id @GeneratedValue
    @Column(name = "article_log_id")
    private Long id;
    private String title;
    private String content;

    @OneToOne(cascade = ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "article_id")
    private Article article;

    @Builder
    public Articlelog(String title, String content, Article article){
        this.title = title;
        this.content = content;
        this.article = article;
    }
}
