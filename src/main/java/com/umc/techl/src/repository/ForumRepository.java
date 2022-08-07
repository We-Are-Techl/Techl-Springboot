package com.umc.techl.src.repository;

import com.umc.techl.src.model.forum.GetBookInfoRes;
import com.umc.techl.src.model.forum.GetBookTitleRes;
import com.umc.techl.src.model.forum.GetForumListRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ForumRepository {

    private JdbcTemplate jdbcTemplate;

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
}