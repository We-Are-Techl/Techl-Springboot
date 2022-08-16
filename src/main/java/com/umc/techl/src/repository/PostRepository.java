package com.umc.techl.src.repository;

import com.umc.techl.src.model.book.GetBookInfoRes;
import com.umc.techl.src.model.book.GetBookTitleRes;
import com.umc.techl.src.model.post.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PostRepository {

    private JdbcTemplate jdbcTemplate;
    private List<GetRecruitingPostListRes> getRecruitingPostListRes;
    private List<GetOngoingOrFinishedListRes> getOngoingOrFinishedListRes;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetBookTitleRes getBookTitle(int bookIdx) {
        String getBookTitleQuery = "select bookIdx, title from book where bookIdx = ?";

        return this.jdbcTemplate.queryForObject(getBookTitleQuery,
                (rs, rowNum) -> new GetBookTitleRes(
                        rs.getInt("bookIdx"),
                        rs.getString("title")
                ), bookIdx);
    }

    public List<GetRecruitingPostListRes> getRecruitingPostListInfo(int bookIdx) {
        String selectRecruitingPostListQuery = "select p.postIdx as postIdx, title, type, concat(DATE_FORMAT(p.startDate, '%Y.%m.%d'),' ~ ',DATE_FORMAT(p.endDate, '%Y.%m.%d')) as period,\n" +
                "       concat(if(countJoinMember is null, 0, countJoinMember),'/',memberCount) as recruiting\n" +
                "from Post as p\n" +
                "         left join (select postIdx, count(*) as countJoinMember\n" +
                "                    from JoinContents\n" +
                "                    where status = 'ACTIVE'\n" +
                "                    group by postIdx) as jc on p.postIdx = jc.postIdx\n" +
                "where p.status = 'RECRUITING' and bookIdx = ?\n" +
                "order by createdAt desc";

        return this.jdbcTemplate.query(selectRecruitingPostListQuery,
                (ra, rownum) -> new GetRecruitingPostListRes(
                        ra.getInt("postIdx"),
                        ra.getString("title"),
                        ra.getString("type"),
                        ra.getString("period"),
                        ra.getString("recruiting")
                ), bookIdx
        );

    }

    public List<GetOngoingOrFinishedListRes> getOngoingOrFinishedListInfo(int bookIdx) {
        String selectOngoingOrFinishedListQuery = "select p.postIdx as postIdx, title, type, concat(DATE_FORMAT(p.startDate, '%Y.%m.%d'),' ~ ',DATE_FORMAT(p.endDate, '%Y.%m.%d')) as period,\n" +
                "       concat(if(countJoinMember is null, 0, countJoinMember),'/',memberCount) as recruiting\n" +
                "from Post as p\n" +
                "         left join (select postIdx, count(*) as countJoinMember\n" +
                "                    from JoinContents\n" +
                "                    where status = 'ACTIVE'\n" +
                "                    group by postIdx) as jc on p.postIdx = jc.postIdx\n" +
                "where (p.status = 'ONGOING' or p.status = 'FINISHED') and bookIdx = ?\n" +
                "order by createdAt desc";

        return this.jdbcTemplate.query(selectOngoingOrFinishedListQuery,
                (rb, rownum) -> new GetOngoingOrFinishedListRes(
                        rb.getInt("postIdx"),
                        rb.getString("title"),
                        rb.getString("type"),
                        rb.getString("period"),
                        rb.getString("recruiting")
                ), bookIdx
        );

    }

    public GetBookInfoRes getBookInfoRes(int bookIdx) {
        String getBookInfoQuery = "select bookIdx, cover, title, author\n" +
                "from Book\n" +
                "where status = 'ACTIVE' and bookIdx = ?";

        return this.jdbcTemplate.queryForObject(getBookInfoQuery,
                (rs, rowNum) -> new GetBookInfoRes(
                        rs.getInt("bookIdx"),
                        rs.getString("cover"),
                        rs.getString("title"),
                        rs.getString("author")
                ), bookIdx);
    }

    public int createPostContents(PostContentsForm postContentsForm) {
        String createPostQuery = "insert into Post (bookIdx, userIdx, type, title, content, contentsImage, coverImage, confirmMethod, startDate, endDate) VALUES (?,?,?,?,?,?,?,?,?,?)";
        Object[] createPostParams = new Object[]{postContentsForm.getBookIdx(), postContentsForm.getUserIdx(), postContentsForm.getType(), postContentsForm.getTitle(), postContentsForm.getContent(),
                                                    postContentsForm.getContentsImage(), postContentsForm.getCoverImage(), postContentsForm.getConfirmMethod(), postContentsForm.getStartDate(), postContentsForm.getEndDate()};
        this.jdbcTemplate.update(createPostQuery, createPostParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkJoinedMember(int postIdx, int userIdx) {
        String checkJoinedMemberQuery = "select exists(select joinContentsIdx from joincontents where postIdx = ? and userIdx = ?)";
        Object[] checkJoinedMemberParams = new Object[]{postIdx, userIdx};
        return this.jdbcTemplate.queryForObject(checkJoinedMemberQuery,
                int.class,
                checkJoinedMemberParams
        );
    }

    public PostContentsInfo getPostContentsInfo(int postIdx) {
        String postContentsInfoQuery = "select postIdx, p.bookIdx, nickName, title, concat(startDate,' ~ ',endDate) as period,\n" +
                                        "       case\n" +
                                        "           when status = 'RECRUITING' then '모집중'\n" +
                                        "           when status = 'ONGOING' then '진행중'\n" +
                                        "           when status = 'FINISHED' then '완료'\n" +
                                        "           end as status,\n" +
                                        "       B.cover as bookCover, content, contentsImage, confirmMethod\n" +
                                        "from Post as p\n" +
                                        "         left join(select cover, bookIdx from Book) as B on p.bookIdx = B.bookIdx\n" +
                                        "         left join (select userIdx, nickName from User where User.status = 'ACTIVE') as  U on p.userIdx = U.userIdx\n" +
                                        "where p.postIdx = ?";

        return this.jdbcTemplate.queryForObject(postContentsInfoQuery,
                (rs, rowNum) -> new PostContentsInfo(
                        rs.getInt("postIdx"),
                        rs.getInt("bookIdx"),
                        rs.getString("nickName"),
                        rs.getString("title"),
                        rs.getString("period"),
                        rs.getString("status"),
                        rs.getString("bookCover"),
                        rs.getString("content"),
                        rs.getString("contentsImage"),
                        rs.getString("confirmMethod")
                ), postIdx);
    }

    public List<GetPostCommentRes> getPostCommentInfo(int postIdx) {
        String postCommentInfoQuery = "select pc.postCommentIdx, nickName, createdAt, content, countUpvote\n" +
                                        "from postcomment as pc\n" +
                                        "        left join (select useridx, nickName\n" +
                                        "                    from user) as u on pc.userIdx = u.userIdx\n" +
                                        "        left join (select postCommentIdx, count(*) as countUpvote\n" +
                                        "                    from postcommentupvote\n" +
                                        "                    where status = 'ACTIVE'\n" +
                                        "                    group by postCommentUpvoteIdx) as pcu on pc.postCommentIdx = pcu.postCommentIdx\n" +
                                        "where postIdx = ?";

        return this.jdbcTemplate.query(postCommentInfoQuery,
                (rs, rowNum) -> new GetPostCommentRes(
                        rs.getInt("postCommentIdx"),
                        rs.getString("nickName"),
                        rs.getString("createdAt"),
                        rs.getString("content"),
                        rs.getInt("countUpvote")
                ), postIdx);
    }

    public int getCountPostComment(int postIdx) {
        String countPostCommentQuery = "select if(count(*) is null, 0, count(*)) as countPostComment\n" +
                                        "from postcomment\n" +
                                        "where postIdx = ?\n" +
                                        "group by postIdx";

        return this.jdbcTemplate.queryForObject(countPostCommentQuery,
                int.class,
                postIdx
        );
    }

    public int createPostComment(PostComment postComment) {
        String createPostQuery = "insert into postcomment (postIdx, userIdx, content) VALUES (?,?,?)";
        Object[] createPostParams = new Object[]{postComment.getPostIdx(), postComment.getUserIdx(), postComment.getContent()};
        this.jdbcTemplate.update(createPostQuery, createPostParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public void joinContents(JoinPost joinPost) {
        String joinContentsQuery = "SELECT EXISTS(SELECT * from joincontents WHERE postIdx=? and userIdx=?) as RESULT";
        Object[] joinContentsParams = new Object[]{joinPost.getPostIdx(), joinPost.getUserIdx()};

        String joinContentsStatusQuery = "select status from joincontents where postIdx=? and userIdx=?";

        if (this.jdbcTemplate.queryForObject(joinContentsQuery, joinContentsParams, Integer.class) == 0) {     //join이 안돼있을 때
            String newJoinQuery = "insert into joincontents (postIdx, userIdx) VALUES (?,?)";
            this.jdbcTemplate.update(newJoinQuery, joinContentsParams);
        } else {    //join이 돼있을 때
            if (this.jdbcTemplate.queryForObject(joinContentsStatusQuery, joinContentsParams, String.class).equals("ACTIVE")) {
                String joinActiveUpdateQuery = "update joincontents set status='INACTIVE' where postIdx=? and userIdx=?";
                this.jdbcTemplate.update(joinActiveUpdateQuery, joinContentsParams);
            } else {
                String joinInactiveUpdateQuery = "update joincontents set status='ACTIVE' where postIdx=? and userIdx=?";
                this.jdbcTemplate.update(joinInactiveUpdateQuery, joinContentsParams);
            }
        }
    }

    public void bookmark(PostBookmark postBookmark) {
        String bookmarkQuery = "SELECT EXISTS(SELECT * from bookmark WHERE userIdx=? and contentIdx=? and type=?) as RESULT";
        Object[] bookmarkParams = new Object[]{postBookmark.getUserIdx(), postBookmark.getPostIdx(), postBookmark.getType()};

        String bookmarkStatusQuery = "select status from bookmark where userIdx=? and contentIdx=? and type=?";

        if (this.jdbcTemplate.queryForObject(bookmarkQuery, bookmarkParams, Integer.class) == 0) {     //북마크가 안돼있을 때
            String newBookmarkQuery = "insert into bookmark (userIdx, contentIdx, type) VALUES (?,?,?)";
            this.jdbcTemplate.update(newBookmarkQuery, bookmarkParams);
        } else {    //북마크가 돼있을 때
            if (this.jdbcTemplate.queryForObject(bookmarkStatusQuery, bookmarkParams, String.class).equals("ACTIVE")) {
                String bookmarkActiveUpdateQuery = "update bookmark set status='INACTIVE' where userIdx=? and contentIdx=? and type=?";
                this.jdbcTemplate.update(bookmarkActiveUpdateQuery, bookmarkParams);
            } else {
                String bookmarkInactiveUpdateQuery = "update bookmark set status='ACTIVE' where userIdx=? and contentIdx=? and type=?";
                this.jdbcTemplate.update(bookmarkInactiveUpdateQuery, bookmarkParams);
            }
        }
    }

    public void postCommentUpvote(PostCommentUpvote postCommentUpvote) {
        String postCommentUpvoteQuery = "SELECT EXISTS(SELECT * from postcommentupvote WHERE postCommentIdx=? and userIdx=?) as RESULT";
        Object[] postCommentUpvoteParams = new Object[]{postCommentUpvote.getPostCommentIdx(), postCommentUpvote.getUserIdx()};

        String postCommentUpvoteStatusQuery = "select status from postcommentupvote where postCommentIdx=? and userIdx=?";

        if (this.jdbcTemplate.queryForObject(postCommentUpvoteQuery, postCommentUpvoteParams, Integer.class) == 0) {     //좋아요가 안돼있을 때
            String newPostCommentUpvoteQuery = "insert into postcommentupvote (postCommentIdx, userIdx) VALUES (?,?)";
            this.jdbcTemplate.update(newPostCommentUpvoteQuery, postCommentUpvoteParams);
        } else {    //좋아요가 돼있을 때
            if (this.jdbcTemplate.queryForObject(postCommentUpvoteStatusQuery, postCommentUpvoteParams, String.class).equals("ACTIVE")) {
                String postCommentUpvoteActiveUpdateQuery = "update postcommentupvote set status='INACTIVE' where postCommentIdx=? and userIdx=?";
                this.jdbcTemplate.update(postCommentUpvoteActiveUpdateQuery, postCommentUpvoteParams);
            } else {
                String postCommentUpvoteInactiveUpdateQuery = "update postcommentupvote set status='ACTIVE' where postCommentIdx=? and userIdx=?";
                this.jdbcTemplate.update(postCommentUpvoteInactiveUpdateQuery, postCommentUpvoteParams);
            }
        }
    }
}