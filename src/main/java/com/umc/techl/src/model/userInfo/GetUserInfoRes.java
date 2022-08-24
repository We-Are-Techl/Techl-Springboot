package com.umc.techl.src.model.userInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserInfoRes {
    private String nickName;
    private String phoneNumber;
    private String userJob;
}
