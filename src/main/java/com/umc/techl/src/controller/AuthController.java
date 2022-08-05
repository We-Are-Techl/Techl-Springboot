package com.umc.techl.src.controller;

import com.umc.techl.config.BaseException;
import com.umc.techl.config.BaseResponse;
import com.umc.techl.src.model.user.PostLoginReq;
import com.umc.techl.src.model.user.PostLoginRes;
import com.umc.techl.src.model.user.PostUserReq;
import com.umc.techl.src.model.user.PostUserRes;
import com.umc.techl.src.service.AuthService;
import com.umc.techl.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.umc.techl.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ResponseBody
    @PostMapping("/join")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {

        if(postUserReq.getPhoneNumber().length() != 11){
            return new BaseResponse<>(INVALID_PHONENUMBER);
        }

        try{
            PostUserRes postUserRes = authService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> login(@RequestBody PostLoginReq postLoginReq) {
        try {

            if(postLoginReq.getPhoneNumber() == null){
                return new BaseResponse<>(POST_USERS_EMPTY_PHONENUMBER);
            }

            if(postLoginReq.getPassword() == null){
                return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
            }

            if(postLoginReq.getPhoneNumber().length() != 11){
                return new BaseResponse<>(INVALID_PHONENUMBER);
            }

            PostLoginRes postLoginRes = authService.login(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
