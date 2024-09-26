package com.example.likelionspring.domain.music;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Artist {
    @Id @GeneratedValue
    @Column(name = "artist_id")
    private Long id;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private List<Song> songs = new ArrayList<>();
}
