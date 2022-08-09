package com.umc.techl.src.model.forum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ForumCommentUpvote {
    private int forumCommentIdx;
    private int userIdx;
}
