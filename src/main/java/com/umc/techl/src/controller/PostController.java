package com.umc.techl.src.controller;

import com.umc.techl.config.BaseException;
import com.umc.techl.config.BaseResponse;
import com.umc.techl.src.model.book.GetBookInfoRes;
import com.umc.techl.src.model.post.GetPostListRes;
import com.umc.techl.src.service.PostService;
import com.umc.techl.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.umc.techl.config.BaseResponseStatus.EMPTY_JWT;

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

    @ResponseBody
    @GetMapping("/new-forum")
    public BaseResponse<GetBookInfoRes> getBookInfo(@RequestParam int bookIdx) {

        try{
            String accessToken = jwtService.getJwt();
            if(accessToken == null || accessToken.length() == 0){
                throw new BaseException(EMPTY_JWT);
            }

            GetBookInfoRes bookInfo = postService.getBookInfo(bookIdx);
            return new BaseResponse<>(bookInfo);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
