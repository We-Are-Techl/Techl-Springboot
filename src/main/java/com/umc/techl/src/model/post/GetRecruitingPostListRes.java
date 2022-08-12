package com.umc.techl.src.model.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetRecruitingPostListRes {
    private String title;
    private String type;
    private String period;
    private String recruiting;
}
