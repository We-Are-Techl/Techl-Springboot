package com.umc.techl.src.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetHomeInfoRes {   // 홈 화면 정보
    private String cover;
    private String title;
    private String authors;
    private int countPost;
    private int countForum;
}
