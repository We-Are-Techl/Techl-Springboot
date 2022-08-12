package com.umc.techl.src.service;

import com.umc.techl.config.BaseException;
import com.umc.techl.src.model.book.GetBookInfoRes;
import com.umc.techl.src.model.book.GetBookTitleRes;
import com.umc.techl.src.model.forum.*;
import com.umc.techl.src.repository.ForumRepository;
import com.umc.techl.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.umc.techl.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class ForumService {

    private final JwtService jwtService;
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

    public GetBookInfoRes getBookInfo(int bookIdx) throws BaseException {
        try {
            GetBookInfoRes bookInfoRes = forumRepository.getBookInfoRes(bookIdx);
            return bookInfoRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostForumContentsRes createForumContents(int bookIdx, PostForumContentsReq contents) throws BaseException {

        try {
            jwtService.getUserIdx();
        } catch (Exception exception) {
            throw new BaseException(INVALID_JWT);
        }

        try {
            int userIdx = jwtService.getUserIdx();
            ForumContents forumContents = new ForumContents(bookIdx, userIdx, contents.getTitle(), contents.getContent(), contents.getContentsImage());
            int forumIdx = forumRepository.createForumContents(forumContents);
            PostForumContentsRes postForumContentsRes = new PostForumContentsRes(forumIdx);
            return postForumContentsRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetForumContentsRes getForumContentsInfo(int forumIdx) throws BaseException {
        try {
            GetForumContentsRes getForumContents = forumRepository.getForumContentsInfo(forumIdx);
            return getForumContents;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostNewCommentRes createNewForumComment(int forumIdx_, PostNewCommentReq postNewCommentReq) throws BaseException {
        try {
            jwtService.getUserIdx();
        } catch (Exception exception) {
            throw new BaseException(INVALID_JWT);
        }

        try {
            int userIdx = jwtService.getUserIdx();
            ForumComment forumComment = new ForumComment(forumIdx_, userIdx, postNewCommentReq.getContent());
            int forumIdx = forumRepository.createForumComment(forumComment);
            PostNewCommentRes postNewCommentRes = new PostNewCommentRes(forumIdx);
            return postNewCommentRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void bookmark(int forumIdx) throws BaseException {
        try {
            jwtService.getUserIdx();
        } catch (Exception exception) {
            throw new BaseException(INVALID_JWT);
        }

        try {
            int userIdx = jwtService.getUserIdx();
            ForumBookmark forum = new ForumBookmark(userIdx, forumIdx, "FORUM");
            forumRepository.bookmark(forum);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void forumUpvote(int forumIdx) throws BaseException {
        try {
            jwtService.getUserIdx();
        } catch (Exception exception) {
            throw new BaseException(INVALID_JWT);
        }

        try {
            int userIdx = jwtService.getUserIdx();
            ForumUpvote forumUpvote = new ForumUpvote(forumIdx, userIdx);
            forumRepository.forumUpvote(forumUpvote);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void forumCommentUpvote(int forumCommentIdx) throws BaseException {
        try {
            jwtService.getUserIdx();
        } catch (Exception exception) {
            throw new BaseException(INVALID_JWT);
        }

        try {
            int userIdx = jwtService.getUserIdx();
            ForumCommentUpvote forumCommentUpvote = new ForumCommentUpvote(forumCommentIdx, userIdx);
            forumRepository.forumCommentUpvote(forumCommentUpvote);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}