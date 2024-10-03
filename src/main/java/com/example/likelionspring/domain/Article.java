package com.example.likelionspring.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "article_id")
    private Long id;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
    }

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Article(String title, String content, Member member, List<Comment> comments){
        this.title = title;
        this.content = content;
        this.member = member;
        this.comments = comments != null ? comments : new ArrayList<>();
    }
}
