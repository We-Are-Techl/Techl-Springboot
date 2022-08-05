package com.umc.techl.src.service;

import com.umc.techl.config.BaseException;
import com.umc.techl.src.model.forum.GetBookTitleRes;
import com.umc.techl.src.model.forum.GetForumInfoRes;
import com.umc.techl.src.model.forum.GetForumListRes;
import com.umc.techl.src.repository.ForumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.umc.techl.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class ForumService {

    private final ForumRepository forumRepository;

    public GetForumInfoRes getForumInfo(int bookIdx) throws BaseException {
        try {
            GetBookTitleRes bookTitle = forumRepository.getBookTitle(bookIdx);
            List<GetForumListRes> forumListInfo = forumRepository.getForumListInfo(bookIdx);
            GetForumInfoRes forumInfoRes = new GetForumInfoRes(bookTitle.getBookIdx(), bookTitle.getTitle(), forumListInfo);
            return forumInfoRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
