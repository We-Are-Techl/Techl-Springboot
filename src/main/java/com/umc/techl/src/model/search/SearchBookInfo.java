package com.umc.techl.src.model.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchBookInfo {
    private int bookIdx;
    private String cover;
    private String title;
    private String author;
    private int countForum;
    private int countPost;
}