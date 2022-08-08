package com.umc.techl.src.model.forum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetForumContentsRes {
    private String bookTitle;
    private String nickName;
    private int countComment;
    private String createdDate;
    private String title;
    private String content;
    private String contentsImage;
    private List<GetForumCommentRes> getForumCommentRes;
}
