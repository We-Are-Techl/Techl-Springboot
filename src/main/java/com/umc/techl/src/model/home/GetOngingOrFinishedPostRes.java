package com.umc.techl.src.model.home;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetOngingOrFinishedPostRes {
    private int postIdx;
    private String title;
    private String coverImage;
}
