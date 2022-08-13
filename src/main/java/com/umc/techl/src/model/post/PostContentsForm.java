package com.umc.techl.src.model.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostContentsForm {
    private int bookIdx;
    private int userIdx;
    private String type;
    private String title;
    private String content;
    private String contentsImage;
    private String coverImage;
    private String confirmMethod;
    private String startDate;
    private String endDate;
}
