package com.umc.techl.src.service;

import com.umc.techl.config.BaseException;
import com.umc.techl.src.model.book.GetBookInfoRes;
import com.umc.techl.src.model.forum.GetBookTitleRes;
import com.umc.techl.src.model.forum.GetForumInfoRes;
import com.umc.techl.src.model.forum.GetForumListRes;
import com.umc.techl.src.model.post.GetPostListRes;
import com.umc.techl.src.repository.ForumRepository;
import com.umc.techl.src.repository.PostRepository;
import com.umc.techl.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.umc.techl.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class PostService {

    private final JwtService jwtService;
    private final PostRepository postRepository;

    public GetPostListRes getPostListInfo(int bookIdx) throws BaseException {
        try {
            GetPostListRes getPostListRes = postRepository.getPostListInfo(bookIdx);
            return getPostListRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetBookInfoRes getBookInfo(int bookIdx) throws BaseException {
        try {
            GetBookInfoRes bookInfoRes = postRepository.getBookInfoRes(bookIdx);
            return bookInfoRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
