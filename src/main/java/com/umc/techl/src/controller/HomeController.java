package com.umc.techl.src.controller;

import com.umc.techl.config.BaseException;
import com.umc.techl.config.BaseResponse;
import com.umc.techl.src.model.home.GetBookInfoRes;
import com.umc.techl.src.model.home.GetHomeInfoRes;
import com.umc.techl.src.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/book/{bookIdx}")
    public BaseResponse<GetBookInfoRes> getBookInfo(@PathVariable("bookIdx")int bookIdx) {
        try{
            GetBookInfoRes bookInfo = homeService.getBookInfo(bookIdx);
            return new BaseResponse<>(bookInfo);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
