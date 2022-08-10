package com.umc.techl.src.model.forumActivity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetForumActivityRes {
    private int countForum;
    private int countForumComment;
    private List<GetMyForumRes> getMyForumRes;
    private List<GetMyForumCommentRes> getMyForumCommentRes;

}
