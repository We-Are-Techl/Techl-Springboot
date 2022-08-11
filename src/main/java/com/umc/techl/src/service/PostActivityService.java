package com.umc.techl.src.service;

import com.umc.techl.config.BaseException;
import com.umc.techl.src.model.forumActivity.GetForumActivityRes;
import com.umc.techl.src.model.postActivity.GetPostActivityRes;
import com.umc.techl.src.repository.ForumActivityRepository;
import com.umc.techl.src.repository.PostActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.umc.techl.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class PostActivityService {

    private final PostActivityRepository postActivityRepository;

    public GetPostActivityRes getPostInfo(int userIdx) throws BaseException {
        try {
            GetPostActivityRes getPostActivityRes = postActivityRepository.getPostInfo(userIdx);
            return getPostActivityRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
