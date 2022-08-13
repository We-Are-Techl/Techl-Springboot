package com.umc.techl.src.model.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetPostContentsRes {
    private String joinYorN;
    private PostContentsInfo postContentsInfo;
    private int countPostComment;
    private List<GetPostCommentRes> getPostCommentRes;
}