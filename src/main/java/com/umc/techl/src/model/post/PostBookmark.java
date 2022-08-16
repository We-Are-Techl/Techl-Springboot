package com.umc.techl.src.model.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostBookmark {
    private int userIdx;
    private int postIdx;
    private String type;
}
