package com.umc.techl.src.service;

import com.umc.techl.config.BaseException;
import com.umc.techl.src.model.book.GetBookInfoRes;
import com.umc.techl.src.model.book.GetBookTitleRes;
import com.umc.techl.src.model.post.*;
import com.umc.techl.src.repository.PostRepository;
import com.umc.techl.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.umc.techl.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final JwtService jwtService;
    private final PostRepository postRepository;

    public GetPostListRes getPostListInfo(int bookIdx) throws BaseException {
        try {
            GetBookTitleRes bookTitle = postRepository.getBookTitle(bookIdx);
            List<GetRecruitingPostListRes> getRecruitingPostListRes = postRepository.getRecruitingPostListInfo(bookIdx);
            List<GetOngoingOrFinishedListRes> getOngoingOrFinishedListRes = postRepository.getOngoingOrFinishedListInfo(bookIdx);
            GetPostListRes postInfoRes = new GetPostListRes(bookTitle.getBookIdx(), bookTitle.getTitle(), getRecruitingPostListRes, getOngoingOrFinishedListRes);
            return postInfoRes;
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

    public PostNewPostRes createPostContents(int bookIdx, PostNewPostReq contents) throws BaseException {
        try {
            jwtService.getUserIdx();
        } catch (Exception exception) {
            throw new BaseException(INVALID_JWT);
        }

        try {
            int userIdx = jwtService.getUserIdx();
            PostContentsForm postContentsForm = new PostContentsForm(bookIdx, userIdx, contents.getType(), contents.getTitle(), contents.getContent(),
                    contents.getContentsImage(), contents.getCoverImage(), contents.getConfirmMethod(), contents.getStartDate(), contents.getEndDate());
            int postIdx = postRepository.createPostContents(postContentsForm);
            PostNewPostRes postNewPostRes = new PostNewPostRes(postIdx);
            return postNewPostRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetPostContentsRes getPostContentsInfo(int postIdx) throws BaseException {

        if (jwtService.getJwt() == null) {  // jwt가 없을 때

            try {
                PostContentsInfo postContentsInfo = postRepository.getPostContentsInfo(postIdx);
                GetPostContentsRes getPostContentsRes = new GetPostContentsRes("NO", postContentsInfo, 0, null);
                return getPostContentsRes;
            } catch (Exception exception) {
                throw new BaseException(DATABASE_ERROR);
            }

        } else {  // jwt 있을 때

            int userIdx;

            try {
                userIdx = jwtService.getUserIdx();
            } catch (Exception exception) {
                throw new BaseException(INVALID_JWT);
            }

            if (postRepository.checkJoinedMember(postIdx, userIdx) == 0 ) { // 참여 안하는 회원

                try {
                    PostContentsInfo postContentsInfo = postRepository.getPostContentsInfo(postIdx);
                    GetPostContentsRes getPostContentsRes = new GetPostContentsRes("NO", postContentsInfo, 0, null);
                    return getPostContentsRes;
                } catch (Exception exception) {
                    throw new BaseException(DATABASE_ERROR);
                }

            } else { // 참여 하는 회원일 때

                try {
                    PostContentsInfo postContentsInfo = postRepository.getPostContentsInfo(postIdx);
                    List<GetPostCommentRes> getPostCommentInfo = postRepository.getPostCommentInfo(postIdx);
                    int countPostComment = postRepository.getCountPostComment(postIdx);
                    GetPostContentsRes getPostContentsRes = new GetPostContentsRes("YES", postContentsInfo, countPostComment, getPostCommentInfo);
                    return getPostContentsRes;
                } catch (Exception exception) {
                    throw new BaseException(DATABASE_ERROR);
                }

            }
        }
    }
}
