package com.umc.techl.src.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetForumInfoRes {
    private String title;
    private String nickName;
    private int countUpvote;
    private int countComment;
    private String createdDate;
}
