package com.umc.techl.src.model.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetPostListRes {
    private int bookIdx;
    private String title;
    private List<GetRecruitingPostListRes> getRecruitingPostListRes;
    private List<GetOngoingOrFinishedListRes> getOngoingOrFinishedListRes;
}
