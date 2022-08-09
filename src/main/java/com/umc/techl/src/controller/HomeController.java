package com.umc.techl.src.controller;

import com.umc.techl.config.BaseException;
import com.umc.techl.config.BaseResponse;
import com.umc.techl.config.BaseResponseStatus;
import com.umc.techl.src.model.home.GetBookInfoRes;
import com.umc.techl.src.model.home.GetHomeInfoRes;
import com.umc.techl.src.service.HomeService;
import com.umc.techl.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.umc.techl.config.BaseResponseStatus.EMPTY_JWT;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;
    private final JwtService jwtService;

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetHomeInfoRes>> getHomeInfo() {
        try {
            List<GetHomeInfoRes> homeInfo = homeService.getHomeInfo();
            return new BaseResponse<>(homeInfo);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/book/{bookIdx}")
    public BaseResponse<GetBookInfoRes> getBookInfo(@PathVariable("bookIdx") int bookIdx) {
        try {
            GetBookInfoRes bookInfo = homeService.getBookInfo(bookIdx);
            return new BaseResponse<>(bookInfo);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping("/book/{bookIdx}/bookmark")
    public BaseResponse bookmark(@PathVariable("bookIdx") int bookIdx) {
        try {
            String accessToken = jwtService.getJwt();
            if(accessToken == null || accessToken.length() == 0){
                throw new BaseException(EMPTY_JWT);
            }

            homeService.bookmark(bookIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
