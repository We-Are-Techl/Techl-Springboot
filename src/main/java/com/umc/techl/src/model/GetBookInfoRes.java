package com.umc.techl.src.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetBookInfoRes {   // 홈에서 책 클릭시 화면
    private String cover;
    private String title;
    private List<GetRecruitingPostRes> getRecruitingPost;
    private List<GetOngingOrFinishedPostRes> getOngingOrFinishedPostRes;
    private List<GetForumInfoRes> getForumInfoRes;
}
