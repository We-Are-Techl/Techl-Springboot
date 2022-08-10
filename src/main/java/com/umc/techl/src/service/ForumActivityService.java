package com.umc.techl.src.service;

import com.umc.techl.config.BaseException;
import com.umc.techl.config.BaseResponse;
import com.umc.techl.config.BaseResponseStatus;
import com.umc.techl.src.model.forumActivity.GetForumActivityRes;
import com.umc.techl.src.repository.ForumActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.umc.techl.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class ForumActivityService {
    private final ForumActivityRepository forumActivityRepository;


    public GetForumActivityRes getForumInfo(int userIdx) throws BaseException {
        try {
            GetForumActivityRes getForumActivityRes = forumActivityRepository.getForumInfo(userIdx);
            return getForumActivityRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
//        GetForumActivityRes getForumActivityRes = forumActivityRepository.getForumInfo(userIdx);
//        return getForumActivityRes;
    }
}
