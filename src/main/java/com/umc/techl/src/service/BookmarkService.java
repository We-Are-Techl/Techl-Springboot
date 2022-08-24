package com.umc.techl.src.service;

import com.umc.techl.config.BaseException;
import com.umc.techl.src.model.bookmark.GetBookmarkRes;
import com.umc.techl.src.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.umc.techl.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    public GetBookmarkRes getBookmarkInfo(int userIdx) throws BaseException {
        try {
            GetBookmarkRes getBookmarkRes = bookmarkRepository.getBookmarkInfo(userIdx);
            return getBookmarkRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}

