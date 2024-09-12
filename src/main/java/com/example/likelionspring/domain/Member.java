package com.example.likelionspring.domain;

import com.example.likelionspring.domain.music.Likes;
import com.example.likelionspring.domain.music.Playlist;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String username;
    private String email;
    private int age;

    @Builder
    public Member(String username, String email, int age) {
        this.username = username;
        this.email = email;
        this.age = age;
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Playlist> playlists = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Likes> likes = new ArrayList<>();

}
