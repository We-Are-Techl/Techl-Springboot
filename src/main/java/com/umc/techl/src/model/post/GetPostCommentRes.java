package com.umc.techl.src.model.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPostCommentRes {
    private String nickName;
    private String createdAt;
    private String content;
    private int countUpvote;
}
