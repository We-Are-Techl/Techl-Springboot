package com.umc.techl.src.controller;

import com.umc.techl.config.BaseException;
import com.umc.techl.config.BaseResponse;
import com.umc.techl.src.model.bookmark.GetBookmarkRes;
import com.umc.techl.src.model.forumActivity.GetForumActivityRes;
import com.umc.techl.src.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/bookmark")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<GetBookmarkRes> getBookmarkInfo(@PathVariable("userIdx") int userIdx) {
        try {
            GetBookmarkRes getBookmarkRes = bookmarkService.getBookmarkInfo(userIdx);
            return new BaseResponse<>(getBookmarkRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}

