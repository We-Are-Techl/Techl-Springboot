package com.umc.techl.src.service;

import com.umc.techl.config.BaseException;
import com.umc.techl.src.model.myActivity.GetMyActivityRes;
import com.umc.techl.src.repository.MyActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.umc.techl.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class MyActivityService {

    private final MyActivityRepository myActivityRepository;

    public GetMyActivityRes getMyActivityInfo(int userIdx) throws BaseException {
        try {
            GetMyActivityRes getMyActivityRes  = myActivityRepository.getMyActivityInfo(userIdx);
            return getMyActivityRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
