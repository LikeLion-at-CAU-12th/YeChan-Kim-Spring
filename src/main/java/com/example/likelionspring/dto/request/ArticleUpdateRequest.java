package com.example.likelionspring.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ArticleUpdateRequest {
    private String title;
    private String content;
    private List<Long> categoryIds;
}
