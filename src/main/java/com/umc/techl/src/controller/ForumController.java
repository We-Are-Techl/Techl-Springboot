package com.umc.techl.src.controller;

import com.umc.techl.config.BaseException;
import com.umc.techl.config.BaseResponse;
import com.umc.techl.src.model.forum.GetForumInfoRes;
import com.umc.techl.src.service.ForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forum")
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;

    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetForumInfoRes> getForumInfo(@RequestParam int bookIdx) {
        try{
            GetForumInfoRes forumInfo = forumService.getForumInfo(bookIdx);
            return new BaseResponse<>(forumInfo);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
