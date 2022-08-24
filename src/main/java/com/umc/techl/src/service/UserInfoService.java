package com.umc.techl.src.service;

import com.umc.techl.config.BaseException;
import com.umc.techl.src.model.forumActivity.GetForumActivityRes;
import com.umc.techl.src.model.userInfo.GetUserInfoRes;
import com.umc.techl.src.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.umc.techl.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;


    public GetUserInfoRes getUserInfo(int userIdx) throws BaseException {
        try {
            GetUserInfoRes getUserInfoRes = userInfoRepository.getUserInfo(userIdx);
            return getUserInfoRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
