package com.umc.techl.src.controller;

import com.umc.techl.config.BaseException;
import com.umc.techl.config.BaseResponse;
import com.umc.techl.src.model.search.GetSearchResultRes;
import com.umc.techl.src.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetSearchResultRes> getSearchResult(@RequestParam String bookName) {
        try{
            GetSearchResultRes searchResultInfo = searchService.getSearchResult(bookName);
            return new BaseResponse<>(searchResultInfo);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}