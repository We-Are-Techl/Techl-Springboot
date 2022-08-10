package com.umc.techl.src.controller;

import com.umc.techl.config.BaseException;
import com.umc.techl.config.BaseResponse;
import com.umc.techl.src.model.userInfo.GetUserInfoRes;
import com.umc.techl.src.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-info")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;

    @ResponseBody
    @GetMapping("{userIdx}")
    public BaseResponse<GetUserInfoRes> getUserInfo(@PathVariable("userIdx") int userIdx) {

        try {
            GetUserInfoRes getUserInfoRes = userInfoService.getUserInfo(userIdx);
            return new BaseResponse<>(getUserInfoRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
