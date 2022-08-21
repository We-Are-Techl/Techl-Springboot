package com.umc.techl.src.model.forum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetForumCommentRes {
    private int forumCommentIdx;
    private String nickName;
    private String createdAt;
    private String content;
    private int countUpvote;
}
