package com.umc.techl.src.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int userIdx;
    private String userName;
    private String nickName;
    private String phoneNumber;
    private String password;
    private String profileImgUrl;
    private String userJob;
}
