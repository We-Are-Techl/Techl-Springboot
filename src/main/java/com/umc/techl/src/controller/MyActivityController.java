package com.umc.techl.src.controller;

import com.umc.techl.config.BaseException;
import com.umc.techl.config.BaseResponse;
import com.umc.techl.src.model.myActivity.GetMyActivityRes;
import com.umc.techl.src.model.postActivity.GetPostActivityRes;
import com.umc.techl.src.service.MyActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("my-activity")
@RequiredArgsConstructor
public class MyActivityController {

    private final MyActivityService myActivityService;

    @ResponseBody
    @GetMapping("{userIdx}")
    public BaseResponse<GetMyActivityRes> getMyActivityInfo(@PathVariable("userIdx") int userIdx) {
        try {
            GetMyActivityRes getMyActivityRes = myActivityService.getMyActivityInfo(userIdx);
            return new BaseResponse<>(getMyActivityRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
