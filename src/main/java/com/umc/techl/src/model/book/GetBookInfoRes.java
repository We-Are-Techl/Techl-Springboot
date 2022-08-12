package com.umc.techl.src.model.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetBookInfoRes {
    private int bookIdx;
    private String cover;
    private String title;
    private String author;
}