package com.umc.techl.src.model.forum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetBookTitleRes {
    private int bookIdx;
    private String title;
}
