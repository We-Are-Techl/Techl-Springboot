package com.umc.techl.src.model.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetOngoingOrFinishedListRes {
    private int postIdx;
    private String title;
    private String type;
    private String period;
    private String recruiting;
}