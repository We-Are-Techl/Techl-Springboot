package com.umc.techl.src.repository;

import com.umc.techl.src.model.home.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class HomeRepository {

    private JdbcTemplate jdbcTemplate;
    private List<GetRecruitingPostRes> getRecruitingPostRes;
    private List<GetOngingOrFinishedPostRes> getOngingOrFinishedPostRes;
    private List<GetForumInfoRes> getForumInfoRes;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetHomeInfoRes> getHomeInfo(){
        String getHomeInfoQuery = "select b.bookIdx, cover, title, author,\n" +
                                    "       if(countPost is null, 0, countPost) as countPost,\n" +
                                    "       if(countForum is null, 0, countForum) as countForum\n" +
                                    "from book as b\n" +
                                    "         left join (select bookIdx,count(*) as countPost\n" +
                                    "                    from post\n" +
                                    "                    where status = 'RECRUITING'\n" +
                                    "                    group by bookIdx) as p on b.bookIdx = p.bookIdx\n" +
                                    "         left join (select bookIdx,count(*) as countForum\n" +
                                    "                    from forum\n" +
                                    "                    group by bookIdx) as f on b.bookIdx = f.bookIdx\n" +
                                    "where b.status = 'ACTIVE'\n" +
                                    "order by b.createdAt desc";

        return this.jdbcTemplate.query(getHomeInfoQuery,
                (rs,rowNum) -> new GetHomeInfoRes(
                        rs.getInt("bookIdx"),
                        rs.getString("cover"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("countPost"),
                        rs.getInt("countForum")
                ));
    }

    public GetBookInfoRes getBookInfo(int bookIdx) {
        String selectBookQuery = "select bookIdx, cover, title from book where bookIdx = ?";

        int selectBookIdx = bookIdx;

        return this.jdbcTemplate.queryForObject(selectBookQuery,  // List 형태이면 -> query, List가 아니면 queryForObject
                (rs,rowNum) -> new GetBookInfoRes(
                        rs.getInt("bookIdx"),
                        rs.getString("cover"),
                        rs.getString("title"),
                        getRecruitingPostRes = this.jdbcTemplate.query("select postIdx, book.title as title, coverImage\n" +
                                                                            "from post\n" +
                                                                            "\t\tleft join book as book on post.bookIdx = book.bookIdx\n" +
                                                                            "where post.bookIdx = ? and post.status = 'RECRUITING'\n" +
                                                                            "order by post.createdAt desc\n" +
                                                                            "limit 3;",
                                (ra, rownum) -> new GetRecruitingPostRes(
                                        ra.getInt("postIdx"),
                                        ra.getString("title"),
                                        ra.getString("coverImage")
                                ),selectBookIdx
                        ),
                        getOngingOrFinishedPostRes = this.jdbcTemplate.query("select postIdx, book.title as title, coverImage\n" +
                                                                                "from post\n" +
                                                                                "    left join book as book on post.bookIdx = book.bookIdx\n" +
                                                                                "where post.bookIdx = ? and (post.status = 'ONGOING' or post.status = 'FINISHED')\n" +
                                                                                "order by post.createdAt desc\n" +
                                                                                "limit 3;",
                                (rb, rownum) -> new GetOngingOrFinishedPostRes(
                                        rb.getInt("postIdx"),
                                        rb.getString("title"),
                                        rb.getString("coverImage")
                                ),selectBookIdx
                        ),
                        getForumInfoRes = this.jdbcTemplate.query("select f.forumIdx as forumIdx, title, contentsImage, nickName,if(countUpvote is null, 0, countUpvote) as countUpvote,\n" +
                                                                        "       if(countComment is null, 0, countComment) as countComment,\n" +
                                                                        "       case\n" +
                                                                        "            when timestampdiff(second, createdAt,current_timestamp) < 60 then concat(timestampdiff(second, createdAt,current_timestamp), '초 전')\n" +
                                                                        "            when timestampdiff(minute , createdAt,current_timestamp) < 60 then concat(timestampdiff(minute, createdAt,current_timestamp), '분 전')\n" +
                                                                        "            when timestampdiff(hour, createdAt,current_timestamp) < 24 then concat(timestampdiff(hour, createdAt,current_timestamp), '시간 전')\n" +
                                                                        "            when timestampdiff(year, createdAt,current_timestamp) > 0 then concat(timestampdiff(year, createdAt,current_timestamp), '년 전')\n" +
                                                                        "            else DATE_FORMAT(createdAt, '%m/%d')\n" +
                                                                        "        end as createdDate\n" +
                                                                        "from forum as f\n" +
                                                                        "         left join (select userIdx, nickName\n" +
                                                                        "                    from user) as u on f.forumIdx = u.userIdx\n" +
                                                                        "         left join (select forumIdx,count(*) as countComment\n" +
                                                                        "                    from forumcomment\n" +
                                                                        "                    where forumcomment.status = 'ACTIVE'\n" +
                                                                        "                    group by forumIdx) as fc on f.forumIdx = fc.forumIdx\n" +
                                                                        "         left join (select forumIdx,count(*) as countUpvote\n" +
                                                                        "                    from forumupvote\n" +
                                                                        "                    where forumupvote.status = 'ACTIVE'\n" +
                                                                        "                    group by forumIdx) as fu on f.forumIdx = fu.forumIdx\n" +
                                                                        "         left join (select bookIdx, title as bookTitle\n" +
                                                                        "                    from book) as bk on f.bookIdx = bk.bookIdx\n" +
                                                                        "where f.status = 'ACTIVE' and f.bookIdx = ?\n" +
                                                                        "group by f.forumIdx\n" +
                                                                        "order by createdAt desc\n" +
                                                                        "limit 3;",
                                (rc, rownum) -> new GetForumInfoRes(
                                        rc.getInt("forumIdx"),
                                        rc.getString("title"),
                                        rc.getString("contentsImage"),
                                        rc.getString("nickName"),
                                        rc.getInt("countUpvote"),
                                        rc.getInt("countComment"),
                                        rc.getString("createdDate")
                                ),selectBookIdx
                        )
                ), selectBookIdx);
    }

    public void bookmark(BookBookmark book) {
        String bookmarkQuery = "SELECT EXISTS(SELECT * from bookmark WHERE userIdx=? and contentIdx=? and type=?) as RESULT";
        Object[] bookmarkParams = new Object[]{book.getUserIdx(), book.getBookIdx(), book.getType()};

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
}
