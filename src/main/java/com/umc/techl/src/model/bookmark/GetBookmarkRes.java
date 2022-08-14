package com.umc.techl.src.model.bookmark;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetBookmarkRes {
    private int bookCount;
    private int postCount;
    private int forumCount;
    private List<BookBookmark> bookBookmark;
    private List<PostBookmark> postBookmark;
    private List<ForumBookmark> forumBookmark;
}
