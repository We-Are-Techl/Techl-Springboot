package com.umc.techl.src.service;

import com.umc.techl.config.BaseException;
import com.umc.techl.src.model.search.GetSearchResultRes;
import com.umc.techl.src.model.search.SearchBookInfo;
import com.umc.techl.src.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.umc.techl.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;

    public GetSearchResultRes getSearchResult(String bookName) throws BaseException {
        try {
            int countResult = searchRepository.getCountResult(bookName);
            List<SearchBookInfo> getSearchBookInfoList = searchRepository.getSearchBookInfo(bookName);
            GetSearchResultRes searchResult = new GetSearchResultRes(countResult, getSearchBookInfoList);
            return searchResult;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}