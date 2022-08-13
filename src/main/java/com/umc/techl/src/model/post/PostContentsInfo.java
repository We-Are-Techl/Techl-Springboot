package com.umc.techl.src.model.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostContentsInfo {
    private int postIdx;
    private int bookIdx;
    private String nickName;
    private String title;
    private String period;
    private String status;
    private String bookCover;
    private String content;
    private String contentsImage;
    private String confirmMethod;
}