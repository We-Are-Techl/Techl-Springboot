package com.umc.techl.src.repository;

import com.umc.techl.src.model.bookmark.BookBookmark;
import com.umc.techl.src.model.bookmark.ForumBookmark;
import com.umc.techl.src.model.bookmark.GetBookmarkRes;
import com.umc.techl.src.model.bookmark.PostBookmark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class BookmarkRepository {

    private JdbcTemplate jdbcTemplate;
    private List<BookBookmark> getBookBookmark;
    private List<ForumBookmark> getForumBookmark;
    private List<PostBookmark> getPostBookmark;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public GetBookmarkRes getBookmarkInfo(int userIdx) {
        String getQuery = "select if(countBookBookmark is null, 0, countBookBookmark) as countBookBookmark,\n" +
                        "if(countPostBookmark is null, 0, countPostBookmark) as countPostBookmark,\n" +
                        "if(countForumBookmark is null, 0, countForumBookmark) as countForumBookmark\n" +
                        "from User as u\n" +
                        "    left join (select userIdx,count(*) as countBookBookmark\n" +
                        "        from Bookmark\n" +
                        "        where type = 'BOOK' and status = 'ACTIVE'\n" +
                        "        group by userIdx) as cbb on u.userIdx = cbb.userIdx\n" +
                        "     left join (select userIdx,count(*) as countPostBookmark\n" +
                        "                        from Bookmark\n" +
                        "                        where type = 'POST' and status = 'ACTIVE'\n" +
                        "                        group by userIdx) as cpb on u.userIdx = cpb.userIdx\n" +
                        "            left join (select userIdx,count(*) as countForumBookmark\n" +
                        "                        from Bookmark\n" +
                        "                        where type = 'FORUM' and status = 'ACTIVE'\n" +
                        "                        group by userIdx) as cfb on u.userIdx = cfb.userIdx\n" +
                        "where u.userIdx = ?";

        return this.jdbcTemplate.queryForObject(getQuery,
                (rs, rowNum) -> new GetBookmarkRes(
                        rs.getInt("countBookBookmark"),
                        rs.getInt("countPostBookmark"),
                        rs.getInt("countForumBookmark"),
                        getBookBookmark = this.jdbcTemplate.query("select title, cover, author\n" +
                                        "from Bookmark as bm\n" +
                                        "        left join (select bookIdx, title, cover, author\n" +
                                        "                    from Book) as b on b.bookIdx = bm.contentIdx\n" +
                                        "where type = 'BOOK' and bm.status = 'ACTIVE' and bm.userIdx = ?\n" +
                                        "order by bm.createdAt desc;",
                                (rs1, rownum) -> new BookBookmark(
                                        rs1.getString("title"),
                                        rs1.getString("cover"),
                                        rs1.getString("author")
                                ), userIdx),
                        getPostBookmark = this.jdbcTemplate.query("select title, cover, author\n" +
                                        "from Bookmark as bm\n" +
                                        "        left join (select bookIdx, title, cover, author\n" +
                                        "                    from Book) as b on b.bookIdx = bm.contentIdx\n" +
                                        "where type = 'POST' and bm.status = 'ACTIVE' and bm.userIdx = ?\n" +
                                        "order by bm.createdAt desc;",
                                ((rs2, rownum) -> new PostBookmark(
                                        rs2.getString("title"),
                                        rs2.getString("cover"),
                                        rs2.getString("author")
                                )), userIdx),
                        getForumBookmark = this.jdbcTemplate.query("select title, cover, author\n" +
                                        "from Bookmark as bm\n" +
                                        "        left join (select bookIdx, title, cover, author\n" +
                                        "                    from Book) as b on b.bookIdx = bm.contentIdx\n" +
                                        "where type = 'FORUM' and bm.status = 'ACTIVE' and bm.userIdx = ?\n" +
                                        "order by bm.createdAt desc;",
                                (rs3, rownum) -> new ForumBookmark(
                                        rs3.getString("title"),
                                        rs3.getString("cover"),
                                        rs3.getString("author")
                                ), userIdx)
                ), userIdx);

    }
}
