package com.example.likelionspring.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name = "article_id")
    private Article article;
}
