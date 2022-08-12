package com.umc.techl.src.model.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostNewPostReq {
    private String type;
    private String title;
    private String content;
    private String contentsImage;
    private String coverImage;
    private String confirmMethod;
    private String startDate;
    private String endDate;
}
