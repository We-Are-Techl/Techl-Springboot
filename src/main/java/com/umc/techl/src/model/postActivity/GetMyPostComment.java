package com.umc.techl.src.model.postActivity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMyPostComment {
    private String title;
    private String nickName;
    private int countPostUpvote;
    private int countPostComment;
    private String createdDate;
}
