package com.umc.techl.src.service;

import com.umc.techl.config.BaseException;
import com.umc.techl.src.model.home.BookBookmark;
import com.umc.techl.src.model.home.GetBookInfoRes;
import com.umc.techl.src.model.home.GetHomeInfoRes;
import com.umc.techl.src.repository.HomeRepository;
import com.umc.techl.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.umc.techl.config.BaseResponseStatus.DATABASE_ERROR;
import static com.umc.techl.config.BaseResponseStatus.INVALID_JWT;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final JwtService jwtService;
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

    public void bookmark(int bookIdx) throws BaseException {
        try {
            jwtService.getUserIdx();
        } catch (Exception exception) {
            throw new BaseException(INVALID_JWT);
        }

        try {
            int userIdx = jwtService.getUserIdx();
            BookBookmark book = new BookBookmark(userIdx, bookIdx, "BOOK");
            homeRepository.bookmark(book);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}



