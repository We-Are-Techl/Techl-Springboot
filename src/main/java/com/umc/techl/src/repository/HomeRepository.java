package com.umc.techl.src.repository;

import com.umc.techl.src.model.*;
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
        String getHomeInfoQuery = "select cover, title, authors,\n" +
                                "if(countPost is null, 0, countPost) as countPost,\n" +
                                "if(countForum is null, 0, countForum) as countForum\n" +
                                "from book as b\n" +
                                "        left join (select bookIdx,count(*) as countPost\n" +
                                "                    from post\n" +
                                "                    where status = 'RECRUITING'\n" +
                                "                    group by bookIdx) as p on b.bookIdx = p.bookIdx\n" +
                                "        left join (select bookIdx,group_concat(author) as authors\n" +
                                "                    from bookauthor\n" +
                                "                    group by bookIdx) as ba on b.bookIdx = ba.bookIdx\n" +
                                "        left join (select bookIdx,count(*) as countForum\n" +
                                "                    from forum\n" +
                                "                    group by bookIdx) as f on b.bookIdx = f.bookIdx\n" +
                                "where b.status = 'ACTIVE'\n" +
                                "order by b.createdAt desc";

        return this.jdbcTemplate.query(getHomeInfoQuery,
                (rs,rowNum) -> new GetHomeInfoRes(
                        rs.getString("cover"),
                        rs.getString("title"),
                        rs.getString("authors"),
                        rs.getInt("countPost"),
                        rs.getInt("countForum")
                ));
    }

    public GetBookInfoRes getBookInfo(int bookIdx) {
        String selectBookQuery = "select cover, title from book where bookIdx = ?";

        int selectBookIdx = bookIdx;

        return this.jdbcTemplate.queryForObject(selectBookQuery,  // List 형태이면 -> query, List가 아니면 queryForObject
                (rs,rowNum) -> new GetBookInfoRes(
                        rs.getString("cover"),
                        rs.getString("title"),
                        getRecruitingPostRes = this.jdbcTemplate.query("select book.title as title, cover\n" +
                                                                            "from post\n" +
                                                                            "\t\tleft join book as book on post.bookIdx = book.bookIdx\n" +
                                                                            "where post.bookIdx = ? and post.status = 'RECRUITING'\n" +
                                                                            "order by post.createdAt desc\n" +
                                                                            "limit 3;",
                                (ra, rownum) -> new GetRecruitingPostRes(
                                        ra.getString("title"),
                                        ra.getString("cover")
                                ),selectBookIdx
                        ),
                        getOngingOrFinishedPostRes = this.jdbcTemplate.query("select book.title as title, cover\n" +
                                                                                "from post\n" +
                                                                                "    left join book as book on post.bookIdx = book.bookIdx\n" +
                                                                                "where post.bookIdx = ? and (post.status = 'ONGOING' or post.status = 'FINISHED')\n" +
                                                                                "order by post.createdAt desc\n" +
                                                                                "limit 3;",
                                (rb, rownum) -> new GetOngingOrFinishedPostRes(
                                        rb.getString("title"),
                                        rb.getString("cover")
                                ),selectBookIdx
                        ),
                        getForumInfoRes = this.jdbcTemplate.query("select title, nickName,if(countUpvote is null, 0, countUpvote) as countUpvote,\n" +
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
                                        rc.getString("title"),
                                        rc.getString("nickName"),
                                        rc.getInt("countUpvote"),
                                        rc.getInt("countComment"),
                                        rc.getString("createdDate")
                                ),selectBookIdx
                        )
                ), selectBookIdx);
    }


}
