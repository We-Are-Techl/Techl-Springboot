package com.umc.techl.src.model.home;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetHomeInfoRes {   // 홈 화면 정보
    private int bookIdx;
    private String cover;
    private String title;
    private String author;
    private int countPost;
    private int countForum;
}
