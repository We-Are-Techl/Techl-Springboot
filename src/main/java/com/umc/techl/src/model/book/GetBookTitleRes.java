package com.umc.techl.src.model.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetBookTitleRes {
    private int bookIdx;
    private String title;
}
