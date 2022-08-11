package com.umc.techl.src.controller;

import com.umc.techl.config.BaseException;
import com.umc.techl.config.BaseResponse;
import com.umc.techl.src.model.postActivity.GetPostActivityRes;
import com.umc.techl.src.service.PostActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post-activity")
@RequiredArgsConstructor
public class PostActivityController {
    private final PostActivityService postActivityService;

    @ResponseBody
    @GetMapping("{userIdx}")

    public BaseResponse<GetPostActivityRes> getPostInfo(@PathVariable("userIdx") int userIdx) {
        try {
            GetPostActivityRes getPostActivityRes = postActivityService.getPostInfo(userIdx);
            return new BaseResponse<>(getPostActivityRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
