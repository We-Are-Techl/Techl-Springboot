package com.umc.techl.src.repository;

import com.umc.techl.src.model.forum.*;
import com.umc.techl.src.model.home.BookBookmark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ForumRepository {

    private JdbcTemplate jdbcTemplate;
    private List<GetForumCommentRes> getForumCommentRes;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetForumListRes> getForumListInfo(int bookIdx) {
        String getForumInfoQuery = "select f.forumidx as forumIdx, title, nickName, if(countUpvote is null, 0, countUpvote) as countUpvote,\n" +
                "       if(countComment is null, 0, countComment) as countComment,\n" +
                "       case\n" +
                "           when timestampdiff(second, createdAt, current_timestamp) < 60 then concat(timestampdiff(second, createdAt, current_timestamp), '초 전')\n" +
                "           when timestampdiff(minute , createdAt, current_timestamp) < 60 then concat(timestampdiff(minute, createdAt, current_timestamp), '분 전')\n" +
                "           when timestampdiff(hour, createdAt, current_timestamp) < 24 then concat(timestampdiff(hour, createdAt, current_timestamp), '시간 전')\n" +
                "           when timestampdiff(year, createdAt, current_timestamp) > 0 then concat(timestampdiff(year, createdAt, current_timestamp), '년 전')\n" +
                "           else DATE_FORMAT(createdAt, '%m/%d')\n" +
                "           end as createdDate\n" +
                "from forum as f\n" +
                "         left join (select userIdx, nickName\n" +
                "                    from user) as u on f.userIdx = u.userIdx\n" +
                "         left join (select forumIdx, count(*) as countComment\n" +
                "                    from forumcomment\n" +
                "                    where forumcomment.status = 'ACTIVE'\n" +
                "                    group by forumIdx) as fc on f.forumIdx = fc.forumIdx\n" +
                "         left join (select forumIdx, count(*) as countUpvote\n" +
                "                    from forumupvote\n" +
                "                    where forumupvote.status = 'ACTIVE'\n" +
                "                    group by forumIdx) as fu on f.forumIdx = fu.forumIdx\n" +
                "         left join (select bookIdx, title as bookTitle\n" +
                "                    from book) as bk on f.bookIdx = bk.bookIdx\n" +
                "where f.status = 'ACTIVE' and bk.bookIdx = ?\n" +
                "group by f.forumIdx\n" +
                "order by createdAt desc";

        return this.jdbcTemplate.query(getForumInfoQuery,
                (rs, rowNum) -> new GetForumListRes(
                        rs.getInt("forumIdx"),
                        rs.getString("title"),
                        rs.getString("nickName"),
                        rs.getInt("countUpvote"),
                        rs.getInt("countComment"),
                        rs.getString("createdDate")
                ), bookIdx);
    }

    public GetBookTitleRes getBookTitle(int bookIdx) {
        String getBookTitleQuery = "select bookIdx, title from book where bookIdx = ?";

        return this.jdbcTemplate.queryForObject(getBookTitleQuery,
                (rs, rowNum) -> new GetBookTitleRes(
                        rs.getInt("bookIdx"),
                        rs.getString("title")
                ), bookIdx);
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

    public int createForumContents(ForumContents forumContents) {
        String createForumQuery = "insert into Forum (bookIdx, title, content, userIdx, contentsImage) VALUES (?,?,?,?,?)";
        Object[] createForumParams = new Object[]{forumContents.getBookIdx(), forumContents.getTitle(), forumContents.getContent(), forumContents.getUserIdx(), forumContents.getContentsImage()};
        this.jdbcTemplate.update(createForumQuery, createForumParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public GetForumContentsRes getForumContentsInfo(int forumIdx) {
        String selectForumContentsQuery = "select bookTitle, nickName,if(countComment is null, 0, countComment) as countComment,\n" +
                                            "       case\n" +
                                            "           when timestampdiff(second, createdAt, current_timestamp) < 60 then concat(timestampdiff(second, createdAt, current_timestamp), '초 전')\n" +
                                            "           when timestampdiff(minute , createdAt, current_timestamp) < 60 then concat(timestampdiff(minute, createdAt, current_timestamp), '분 전')\n" +
                                            "           when timestampdiff(hour, createdAt, current_timestamp) < 24 then concat(timestampdiff(hour, createdAt, current_timestamp), '시간 전')\n" +
                                            "           when timestampdiff(year, createdAt, current_timestamp) > 0 then concat(timestampdiff(year, createdAt, current_timestamp), '년 전')\n" +
                                            "           else DATE_FORMAT(createdAt, '%Y.%m.%d %H:%i')\n" +
                                            "           end as createdDate,\n" +
                                            "       f.title, content, contentsImage\n" +
                                            "from forum as f\n" +
                                            "         left join (select userIdx, nickName\n" +
                                            "                    from user) as u on f.userIdx = u.userIdx\n" +
                                            "         left join (select forumIdx,count(*) as countComment\n" +
                                            "                    from forumcomment\n" +
                                            "                    where forumcomment.status = 'ACTIVE'\n" +
                                            "                    group by forumIdx) as fc on f.forumIdx = fc.forumIdx\n" +
                                            "         left join (select bookIdx, title as bookTitle\n" +
                                            "                    from book) as bk on f.bookIdx = bk.bookIdx\n" +
                                            "where f.status = 'ACTIVE' and f.forumIdx = ?";

        int selectForumIdx = forumIdx;

        return this.jdbcTemplate.queryForObject(selectForumContentsQuery,  // List 형태이면 -> query, List가 아니면 queryForObject
                (rs,rowNum) -> new GetForumContentsRes(
                        rs.getString("bookTitle"),
                        rs.getString("nickName"),
                        rs.getInt("countComment"),
                        rs.getString("createdDate"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("contentsImage"),
                        getForumCommentRes = this.jdbcTemplate.query("select nickName, DATE_FORMAT(createdAt, '%Y.%m.%d %H:%i') as createdAt, content, countUpvote\n" +
                                                                        "from forumcomment as fc\n" +
                                                                        "         left join (select userIdx, nickName\n" +
                                                                        "                    from user) as u on fc.userIdx = u.userIdx\n" +
                                                                        "         left join (select forumCommentIdx,count(*) as countUpvote\n" +
                                                                        "                    from forumcommentupvote\n" +
                                                                        "                    group by forumCommentIdx) as fcu on fc.forumCommentIdx = fcu.forumCommentIdx\n" +
                                                                        "where forumIdx = ?\n",
                                (ra, rownum) -> new GetForumCommentRes(
                                        ra.getString("nickName"),
                                        ra.getString("createdAt"),
                                        ra.getString("content"),
                                        ra.getInt("countUpvote")
                                ),selectForumIdx
                        )
                ), selectForumIdx);
    }

    public int createForumComment(ForumComment forumComment) {
        String createForumQuery = "insert into forumcomment (forumIdx, userIdx, content) VALUES (?,?,?)";
        Object[] createForumParams = new Object[]{forumComment.getForumIdx(), forumComment.getUserIdx(), forumComment.getContent()};
        this.jdbcTemplate.update(createForumQuery, createForumParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public void bookmark(ForumBookmark forum) {
        String bookmarkQuery = "SELECT EXISTS(SELECT * from bookmark WHERE userIdx=? and contentIdx=? and type=?) as RESULT";
        Object[] bookmarkParams = new Object[]{forum.getUserIdx(), forum.getForumIdx(), forum.getType()};

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

    public void forumUpvote(ForumUpvote forumUpvote) {
        String forumUpvoteQuery = "SELECT EXISTS(SELECT * from forumupvote WHERE forumIdx=? and userIdx=?) as RESULT";
        Object[] forumUpvoteParams = new Object[]{forumUpvote.getForumIdx(), forumUpvote.getUserIdx()};

        String forumUpvoteStatusQuery = "select status from forumupvote where forumIdx=? and userIdx=?";

        if (this.jdbcTemplate.queryForObject(forumUpvoteQuery, forumUpvoteParams, Integer.class) == 0) {     //좋아요가 안돼있을 때
            String newForumUpvoteQuery = "insert into forumupvote (forumIdx, userIdx) VALUES (?,?)";
            this.jdbcTemplate.update(newForumUpvoteQuery, forumUpvoteParams);
        } else {    //좋아요가 돼있을 때
            if (this.jdbcTemplate.queryForObject(forumUpvoteStatusQuery, forumUpvoteParams, String.class).equals("ACTIVE")) {
                String forumUpvoteActiveUpdateQuery = "update forumupvote set status='INACTIVE' where forumIdx=? and userIdx=?";
                this.jdbcTemplate.update(forumUpvoteActiveUpdateQuery, forumUpvoteParams);
            } else {
                String forumUpvoteInactiveUpdateQuery = "update forumupvote set status='ACTIVE' where forumIdx=? and userIdx=?";
                this.jdbcTemplate.update(forumUpvoteInactiveUpdateQuery, forumUpvoteParams);
            }
        }
    }
}