package com.umc.techl.src.model.forum;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ForumContents {
    private int bookIdx;
    private int userIdx;
    private String title;
    private String content;
    private String contentsImage;
}
