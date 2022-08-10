package com.umc.techl.src.model.forumActivity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMyForumCommentRes {
    private String title;
    private String nickName;
    private int countForumUpvote;
    private int countForumComment;
    private String createdDate;
}
