package com.umc.techl.src.controller;

import com.umc.techl.config.BaseException;
import com.umc.techl.config.BaseResponse;
import com.umc.techl.src.model.forumActivity.GetForumActivityRes;
import com.umc.techl.src.service.ForumActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forum-activity")
@RequiredArgsConstructor
public class ForumActivityController {
    private final ForumActivityService forumActivityService;

    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<GetForumActivityRes> getForumInfo(@PathVariable("userIdx") int userIdx) {
        try {
            GetForumActivityRes getForumActivityRes = forumActivityService.getForumInfo(userIdx);
            return new BaseResponse<>(getForumActivityRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
