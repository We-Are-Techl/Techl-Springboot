package com.umc.techl.src.controller;

import com.umc.techl.config.BaseException;
import com.umc.techl.config.BaseResponse;
import com.umc.techl.src.model.GetBookInfoRes;
import com.umc.techl.src.model.GetHomeInfoRes;
import com.umc.techl.src.service.HomeService;
import com.umc.techl.src.user.model.GetUserRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.umc.techl.config.BaseResponseStatus.POST_USERS_EMPTY_EMAIL;
import static com.umc.techl.config.BaseResponseStatus.POST_USERS_INVALID_EMAIL;
import static com.umc.techl.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetHomeInfoRes>> getHomeInfo() {
        try{
            List<GetHomeInfoRes> homeInfo = homeService.getHomeInfo();
            return new BaseResponse<>(homeInfo);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/book")
    public BaseResponse<GetBookInfoRes> getBookInfo(@RequestParam int bookIdx) {
        try{
            GetBookInfoRes bookInfo = homeService.getBookInfo(bookIdx);
            return new BaseResponse<>(bookInfo);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
