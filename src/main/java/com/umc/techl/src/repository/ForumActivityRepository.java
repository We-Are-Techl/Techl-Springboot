package com.umc.techl.src.repository;

import com.umc.techl.src.model.forumActivity.GetForumActivityRes;
import com.umc.techl.src.model.forumActivity.GetMyForumCommentRes;
import com.umc.techl.src.model.forumActivity.GetMyForumRes;
import com.umc.techl.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ForumActivityRepository {

    private JdbcTemplate jdbcTemplate;
    private List<GetMyForumRes> getMyForumRes;
    private List<GetMyForumCommentRes> getMyForumCommentRes;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public GetForumActivityRes getForumInfo(int userIdx) {
        String getUsersQuery = "select countForum, countForumComment\n" +
                                "from user as u\n" +
                                "        left join (select u_.userIdx, if(countForum is null, 0, countForum) as countForum\n" +
                                "                    from user as u_\n" +
                                "                            left join(select userIdx, count(*) as countForum\n" +
                                "                                        from forum as f\n" +
                                "                                        where f.status = 'ACTIVE'\n" +
                                "                                        group by f.userIdx) as cf on u_.userIdx = cf.userIdx)\n" +
                                "                    as cf_ on u.userIdx = cf_.userIdx\n" +
                                "\n" +
                                "        left join (select u_.userIdx, if(countForumComment is null, 0, countForumComment) as countForumComment\n" +
                                "                    from user as u_\n" +
                                "                            left join (select userIdx, count(*) as countForumComment\n" +
                                "                                        from ForumComment as fc\n" +
                                "                                        where fc.status = 'ACTIVE'\n" +
                                "                                        group by fc.userIdx) as cfc on u_.userIdx = cfc.userIdx)\n" +
                                "                    as cfc_ on u.userIdx = cfc_.userIdx\n" +
                                "where u.userIdx = ?;";

        return this.jdbcTemplate.queryForObject(getUsersQuery,
                (rs,rowNum) -> new GetForumActivityRes(
                        rs.getInt("countForum"),
                        rs.getInt("countForumComment"),
                        getMyForumRes = this.jdbcTemplate.query("select title, nickName, if(countUpvote is null, 0, countUpvote) as countUpvote,\n" +
                                                                    "        if(countComment is null, 0, countComment) as countComment,\n" +
                                                                    "        case\n" +
                                                                    "            when timestampdiff(second, createdAt, current_timestamp) < 60 then concat(timestampdiff(second, createdAt, current_timestamp), '초 전')\n" +
                                                                    "            when timestampdiff(minute , createdAt, current_timestamp) < 60 then concat(timestampdiff(minute, createdAt, current_timestamp), '분 전')\n" +
                                                                    "            when timestampdiff(hour, createdAt, current_timestamp) < 24 then concat(timestampdiff(hour, createdAt, current_timestamp), '시간 전')\n" +
                                                                    "            when timestampdiff(year, createdAt, current_timestamp) > 0 then concat(timestampdiff(year, createdAt, current_timestamp), '년 전')\n" +
                                                                    "            else DATE_FORMAT(createdAt, '%m/%d')\n" +
                                                                    "        end as createdDate\n" +
                                                                    "from forum as f\n" +
                                                                    "        left join (select userIdx, nickName\n" +
                                                                    "                    from user) as u on f.forumIdx = u.userIdx\n" +
                                                                    "        left join (select forumIdx, count(*) as countComment\n" +
                                                                    "                    from forumcomment\n" +
                                                                    "                    where forumcomment.status = 'ACTIVE'\n" +
                                                                    "                    group by forumIdx) as fc on f.forumIdx = fc.forumIdx\n" +
                                                                    "        left join (select forumIdx, count(*) as countUpvote\n" +
                                                                    "                    from forumupvote\n" +
                                                                    "                    where forumupvote.status = 'ACTIVE'\n" +
                                                                    "                    group by forumIdx) as fu on f.forumIdx = fu.forumIdx\n" +
                                                                    "where f.status = 'ACTIVE' and f.userIdx = ?\n" +
                                                                    "group by f.forumIdx\n" +
                                                                    "order by createdAt desc;",
                                (ra,rownum) -> new GetMyForumRes(
                                        ra.getString("title"),
                                        ra.getString("nickName"),
                                        ra.getInt("countUpvote"),
                                        ra.getInt("countComment"),
                                        ra.getString("createdDate")

                                ),userIdx),
                        getMyForumCommentRes = this.jdbcTemplate.query("select distinct title, countComment, countUpvote, createdDate, nickName#글제목,댓글수,좋아요 수,작성일,작성자\n" +
                                                                            "from (\n" +
                                                                            "        select forumIdx, userIdx\n" +
                                                                            "        from forumcomment) as countForumComment\n" +
                                                                            "                left join (select f.forumIdx, title, nickName,if(countUpvote is null, 0, countUpvote) as countUpvote,\n" +
                                                                            "if(countComment is null, 0, countComment) as countComment,\n" +
                                                                            "                                    case\n" +
                                                                            "                                        when timestampdiff(second, createdAt,current_timestamp) < 60 then concat(timestampdiff(second, createdAt,current_timestamp), '초 전')\n" +
                                                                            "                                        when timestampdiff(minute , createdAt,current_timestamp) < 60 then concat(timestampdiff(minute, createdAt,current_timestamp), '분 전')\n" +
                                                                            "                                        when timestampdiff(hour, createdAt,current_timestamp) < 24 then concat(timestampdiff(hour, createdAt,current_timestamp), '시간 전')\n" +
                                                                            "                                        when timestampdiff(year, createdAt,current_timestamp) > 0 then concat(timestampdiff(year, createdAt,current_timestamp), '년 전')\n" +
                                                                            "                                        else DATE_FORMAT(createdAt, '%m/%d')\n" +
                                                                            "                                    end as createdDate\n" +
                                                                            "                            from forum as f\n" +
                                                                            "                                    left join (select userIdx, nickName\n" +
                                                                            "                                                from user) as u on f.forumIdx = u.userIdx\n" +
                                                                            "                                    left join (select forumIdx,count(userIdx) as countComment\n" +
                                                                            "                                                from (\n" +
                                                                            "                                                       select forumIdx, userIdx\n" +
                                                                            "                                                        from forumcomment\n" +
                                                                            "                                                        where status = 'ACTIVE') as countForumComment\n" +
                                                                            "                                                group by forumIdx) as cfc on f.forumIdx = cfc.forumIdx\n" +
                                                                            "                                    left join (select forumIdx,count(*) as countUpvote\n" +
                                                                            "                                                from forumupvote\n" +
                                                                            "                                                where forumupvote.status = 'ACTIVE'\n" +
                                                                            "                                                group by forumIdx) as fu on f.forumIdx = fu.forumIdx\n" +
                                                                            "                                    left join (select bookIdx, title as bookTitle\n" +
                                                                            "                                                from book) as bk on f.bookIdx = bk.bookIdx\n" +
                                                                            "                            where f.status = 'ACTIVE'\n" +
                                                                            "                            group by f.forumIdx) as forumList on countForumComment.forumIdx = forumList.forumIdx\n" +
                                                                            "where countForumComment.userIdx = ?\n" +
                                                                            "order by createdDate desc",
                                (rb,rownum) -> new GetMyForumCommentRes(
                                        rb.getString("title"),
                                        rb.getString("nickName"),
                                        rb.getInt("countUpvote"),
                                        rb.getInt("countComment"),
                                        rb.getString("createdDate")

                                ),userIdx)
                ),userIdx);
    }
}
