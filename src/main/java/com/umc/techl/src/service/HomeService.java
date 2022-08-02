package com.umc.techl.src.service;

import com.umc.techl.config.BaseException;
import com.umc.techl.src.model.GetBookInfoRes;
import com.umc.techl.src.model.GetHomeInfoRes;
import com.umc.techl.src.repository.HomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.umc.techl.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final HomeRepository homeRepository;

    public List<GetHomeInfoRes> getHomeInfo() throws BaseException {
        try {
            List<GetHomeInfoRes> getHomeInfoRes = homeRepository.getHomeInfo();
            return getHomeInfoRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public GetBookInfoRes getBookInfo(int bookIdx) throws BaseException {
        try {
            GetBookInfoRes getBookInfoRes = homeRepository.getBookInfo(bookIdx);
            return getBookInfoRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }




}



