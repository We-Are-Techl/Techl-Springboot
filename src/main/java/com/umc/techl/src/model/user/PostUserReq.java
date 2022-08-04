package com.umc.techl.src.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {
    private String userName;
    private String nickName;
    private String phoneNumber;
    private String password;
    private String profileImgUrl;
    private String userJob;
    private List<PostUserInterestReq> postUserInterests;
}
