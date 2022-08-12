package com.umc.techl.src.controller;

import com.umc.techl.config.BaseException;
import com.umc.techl.config.BaseResponse;
import com.umc.techl.src.model.post.GetPostListRes;
import com.umc.techl.src.service.PostService;
import com.umc.techl.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final JwtService jwtService;

    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetPostListRes> getPostListInfo(@RequestParam int bookIdx) {
        try{
            GetPostListRes postListInfo = postService.getPostListInfo(bookIdx);
            return new BaseResponse<>(postListInfo);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
