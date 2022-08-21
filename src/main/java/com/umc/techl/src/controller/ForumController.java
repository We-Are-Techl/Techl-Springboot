package com.umc.techl.src.controller;

import com.umc.techl.config.BaseException;
import com.umc.techl.config.BaseResponse;
import com.umc.techl.config.BaseResponseStatus;
import com.umc.techl.src.model.book.GetBookInfoRes;
import com.umc.techl.src.model.forum.*;
import com.umc.techl.src.service.ForumService;
import com.umc.techl.utils.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import static com.umc.techl.config.BaseResponseStatus.*;

@Slf4j
@RestController
@RequestMapping("/forum")
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;
    private final JwtService jwtService;

    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetForumInfoRes> getForumInfo(@RequestParam int bookIdx) {
        try{
            GetForumInfoRes forumInfo = forumService.getForumInfo(bookIdx);
            return new BaseResponse<>(forumInfo);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/new-forum")
    public BaseResponse<GetBookInfoRes> getBookInfo(@RequestParam int bookIdx) {

        try{
            String accessToken = jwtService.getJwt();
            if(accessToken == null || accessToken.length() == 0){
                throw new BaseException(EMPTY_JWT);
            }

            GetBookInfoRes bookInfo = forumService.getBookInfo(bookIdx);
            return new BaseResponse<>(bookInfo);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/new-forum/create")
    public BaseResponse<PostForumContentsRes> createForumContents(@RequestParam int bookIdx,
                                                                  @RequestPart PostForumContentsReq postForumContentsReq,
                                                                  @RequestPart(required = false) MultipartFile multipartFile) {

        try{
            String accessToken = jwtService.getJwt();
            if(accessToken == null || accessToken.length() == 0){
                throw new BaseException(EMPTY_JWT);
            }

            if (postForumContentsReq.getTitle() == null || postForumContentsReq.getTitle().length() == 0) {
                throw new BaseException(POST_EMPTY_TITLE);
            }

            if (postForumContentsReq.getContent() == null || postForumContentsReq.getContent().length() == 0) {
                throw new BaseException(POST_EMPTY_CONTENTS);
            }

            PostForumContentsRes postForumContentsRes = forumService.createForumContents(bookIdx, postForumContentsReq, multipartFile);
            return new BaseResponse<>(postForumContentsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{forumIdx}")
    public BaseResponse<GetForumContentsRes> getForumContentsInfo(@PathVariable("forumIdx")int forumIdx) {
        try{
            GetForumContentsRes getForumContentsInfo = forumService.getForumContentsInfo(forumIdx);
            return new BaseResponse<>(getForumContentsInfo);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/{forumIdx}/new-forum-comment")
    public BaseResponse<PostNewCommentRes> createNewForumComment(@PathVariable("forumIdx")int forumIdx,
                                                                @RequestBody PostNewCommentReq postNewCommentReq) {

        try{
            String accessToken = jwtService.getJwt();
            if(accessToken == null || accessToken.length() == 0){
                throw new BaseException(EMPTY_JWT);
            }

            if (postNewCommentReq.getContent() == null || postNewCommentReq.getContent().length() == 0) {
                throw new BaseException(POST_EMPTY_CONTENTS);
            }

            PostNewCommentRes postNewCommentRes = forumService.createNewForumComment(forumIdx, postNewCommentReq);
            return new BaseResponse<>(postNewCommentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping("/{forumIdx}/bookmark")
    public BaseResponse bookmark(@PathVariable("forumIdx") int forumIdx) {
        try {
            String accessToken = jwtService.getJwt();
            if(accessToken == null || accessToken.length() == 0){
                throw new BaseException(EMPTY_JWT);
            }

            forumService.bookmark(forumIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping("/{forumIdx}/forum-upvote")
    public BaseResponse forumUpvote(@PathVariable("forumIdx") int forumIdx) {
        try {
            String accessToken = jwtService.getJwt();
            if(accessToken == null || accessToken.length() == 0){
                throw new BaseException(EMPTY_JWT);
            }

            forumService.forumUpvote(forumIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping("/forum-comment-upvote/{forumCommentIdx}")
    public BaseResponse forumCommentUpvote(@PathVariable("forumCommentIdx") int forumCommentIdx) {
        try {
            String accessToken = jwtService.getJwt();
            if(accessToken == null || accessToken.length() == 0){
                throw new BaseException(EMPTY_JWT);
            }

            forumService.forumCommentUpvote(forumCommentIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @DeleteMapping("/{forumIdx}/delete-forum")
    public BaseResponse deleteForum(@PathVariable("forumIdx")int forumIdx) {
        try {
            String accessToken = jwtService.getJwt();
            if(accessToken == null || accessToken.length() == 0){
                throw new BaseException(EMPTY_JWT);
            }

            forumService.forumDelete(forumIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @DeleteMapping("/delete-forumComment/{forumCommentIdx}")
    public BaseResponse deleteForumComment(@PathVariable("forumCommentIdx")int forumCommentIdx) {
        try {
            String accessToken = jwtService.getJwt();
            if(accessToken == null || accessToken.length() == 0){
                throw new BaseException(EMPTY_JWT);
            }

            forumService.deleteForumComment(forumCommentIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}