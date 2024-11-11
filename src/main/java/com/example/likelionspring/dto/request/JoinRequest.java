package com.example.likelionspring.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class JoinRequest {
    private String username;
    private String password;
    private String email;
}
