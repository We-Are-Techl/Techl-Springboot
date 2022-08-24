package com.umc.techl.src.model.myActivity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetMyActivityRes {
    private String nickName;
    private List<MyForumInfo> myForumInfo;
    private List<MyPostInfo> myPostInfo;
    private List<MyBookmarkInfo> myBookmarkInfo;
}
