package com.umc.techl.src.model.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetSearchResultRes {
    private int countResult;
    private List<SearchBookInfo> searchBookInfo;
}