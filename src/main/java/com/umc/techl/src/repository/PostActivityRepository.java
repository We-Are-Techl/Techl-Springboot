package com.umc.techl.src.repository;

import com.umc.techl.src.model.postActivity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PostActivityRepository {

    private JdbcTemplate jdbcTemplate;

    private List<GetMyPostParticipation> getMyPostParticipation;
    private List<GetMyPostCompletion> getMyPostCompletion;
    private List<GetMyPostAnnouncememt> getMyPostAnnouncememt;
    private List<GetMyPostComment> getMyPostComment;

    @Autowired
    public void setDataSource(DataSource dataSource){this.jdbcTemplate = new JdbcTemplate(dataSource);}

    public GetPostActivityRes getPostInfo(int userIdx) {
        String getCountQuery = "select if(participationCount is null,0, participationCount) as participationCount ,\n" +
                                "        if(completionCount is null,0, completionCount) as completionCount,\n" +
                                "       if(announcementCount is null,0, announcementCount) as announcementCount,\n" +
                                "       if(commentCount is null,0, commentCount) as commentCount\n" +
                                "from User as u\n" +
                                "    left join (select jc.userIdx, count(*) as participationCount\n" +
                                "                from JoinContents as jc\n" +
                                "                left join (select postIdx from Post as p where p.status != 'FINISHED' ) as p on p.postIdx = jc.postIdx\n" +
                                "                left join (select userIdx, status from User as u where u.status='ACTIVE') as U on jc.userIdx = U.userIdx\n" +
                                "                where jc.status = 'ACTIVE' and p.postIdx = jc.postIdx and U.status is not null\n" +
                                "                group by jc.userIdx\n" +
                                "                )  as pc on pc.userIdx = u.userIdx\n" +
                                "\n" +
                                "    left join (select jc.userIdx, count(*) as completionCount\n" +
                                "                from JoinContents as jc\n" +
                                "                left join (select postIdx from Post as p where p.status = 'FINISHED' ) as p on p.postIdx = jc.postIdx\n" +
                                "                left join (select userIdx, status from User as u where u.status='ACTIVE') as U on jc.userIdx = U.userIdx\n" +
                                "                where jc.status = 'ACTIVE' and p.postIdx = jc.postIdx and U.status is not null\n" +
                                "                group by jc.userIdx\n" +
                                "                ) as cc on cc.userIdx = u.userIdx\n" +
                                "\n" +
                                "    left join (select p.userIdx, count(*) as announcementCount\n" +
                                "                from Post as p\n" +
                                "                left join (select userIdx, status from User as u where u.status='ACTIVE') as U on p.userIdx = U.userIdx\n" +
                                "                where U.status = 'ACTIVE'\n" +
                                "                group by p.userIdx\n" +
                                "    ) as ac on ac.userIdx = u.userIdx\n" +
                                "\n" +
                                "    left join (select pc.userIdx, count(*) as commentCount\n" +
                                "                from PostComment as pc\n" +
                                "                left join (select userIdx, status from User as u where u.status='ACTIVE') as U on pc.userIdx = U.userIdx\n" +
                                "                where U.status = 'ACTIVE'\n" +
                                "                group by pc.userIdx\n" +
                                "    ) as cc_ on cc_.userIdx = u.userIdx\n" +
                                "where u.userIdx = ?";

        return this.jdbcTemplate.queryForObject(
               getCountQuery,
                (rs, rowNum) -> new GetPostActivityRes(
                        rs.getInt("participationCount"),
                        rs.getInt("completionCount"),
                        rs.getInt("announcementCount"),
                        rs.getInt("commentCount"),
                        getMyPostParticipation = this.jdbcTemplate.query("select title,concat(DATE_FORMAT(startDate, '%m/%d'), ' ~ ',DATE_FORMAT(endDate, '%m/%d')) as period,\n" +
                                        "concat(joinCount, '/', p.memberCount) as joinCount\n" +
                                        "from JoinContents as j\n" +
                                        "        left join (select postIdx, title, status, memberCount, startDate, endDate\n" +
                                        "                    from Post) as p on j.postIdx = p.postIdx\n" +
                                        "        left join (select postIdx,count(*) as joinCount\n" +
                                        "                    from JoinContents\n" +
                                        "                    group by postIdx) as cj on j.postIdx = cj.postIdx\n" +
                                        "where (p.status = 'ONGOING' or p.status = 'RECRUITING') and j.userIdx = ?",
                                        (re, rownum) -> new GetMyPostParticipation(
                                        re.getString("title"),
                                        re.getString("period"),
                                        re.getString("joinCount")
                                        ), userIdx),

                        getMyPostCompletion = this.jdbcTemplate.query("select title,concat(DATE_FORMAT(startDate, '%m/%d'), ' ~ ',DATE_FORMAT(endDate, '%m/%d')) as period,\n" +
                                        "concat(joinCount, '/', p.memberCount) as joinCount\n" +
                                        "from JoinContents as j\n" +
                                        "        left join (select postIdx, title, status, memberCount, startDate, endDate\n" +
                                        "                    from Post) as p on j.postIdx = p.postIdx\n" +
                                        "        left join (select postIdx,count(*) as joinCount\n" +
                                        "                    from JoinContents\n" +
                                        "                    group by postIdx) as jc on j.postIdx = jc.postIdx\n" +
                                        "where p.status = 'FINISHED' and j.userIdx = ?;",
                                (ra, rownum) -> new GetMyPostCompletion(
                                        ra.getString("title"),
                                        ra.getString("period"),
                                        ra.getString("joinCount")
                                ), userIdx),
                        getMyPostAnnouncememt = this.jdbcTemplate.query("select title,concat(DATE_FORMAT(startDate, '%m/%d'), ' ~ ',DATE_FORMAT(endDate, '%m/%d')) as period,\n" +
                                        " concat(if(joinCount is null, 0, joinCount), '/', p.memberCount) as joinCount\n" +
                                        "from Post as p\n" +
                                        "        left join (select postIdx,count(*) joinCount\n" +
                                        "                    from JoinContents as j\n" +
                                        "                    group by postIdx) as jc on p.postIdx = jc.postIdx\n" +
                                        "where  userIdx = ?;",
                                (rb, rownum) -> new GetMyPostAnnouncememt(
                                        rb.getString("title"),
                                        rb.getString("period"),
                                        rb.getString("joinCount")
                                ), userIdx),
                        getMyPostComment = this.jdbcTemplate.query("select distinct title, nickName,countUpvote, countComment,createdDate\n" +
                                        "from (  select postIdx, userIdx\n" +
                                        "        from PostComment) as countPostComment\n" +
                                        "                left join (select p.postIdx, title, nickName,if(countUpvote is null, 0, countUpvote) as countUpvote,\n" +
                                        "if(countComment is null, 0, countComment) as countComment,\n" +
                                        "                                    case\n" +
                                        "                                        when timestampdiff(second, createdAt,current_timestamp) < 60 then concat(timestampdiff(second, createdAt,current_timestamp), '초 전')\n" +
                                        "                                        when timestampdiff(minute , createdAt,current_timestamp) < 60 then concat(timestampdiff(minute, createdAt,current_timestamp), '분 전')\n" +
                                        "                                        when timestampdiff(hour, createdAt,current_timestamp) < 24 then concat(timestampdiff(hour, createdAt,current_timestamp), '시간 전')\n" +
                                        "                                        when timestampdiff(year, createdAt,current_timestamp) > 0 then concat(timestampdiff(year, createdAt,current_timestamp), '년 전')\n" +
                                        "                                        else DATE_FORMAT(createdAt, '%m/%d')\n" +
                                        "                                    end as createdDate\n" +
                                        "                            from Post as p\n" +
                                        "                                    left join (select userIdx, nickName\n" +
                                        "                                                from User) as u on p.userIdx = u.userIdx\n" +
                                        "                                    left join (select postIdx,count(userIdx) as countComment\n" +
                                        "                                                from (\n" +
                                        "                                                       select postIdx, userIdx\n" +
                                        "                                                        from PostComment\n" +
                                        "                                                        where status = 'ACTIVE') as countPostComment\n" +
                                        "                                                group by postIdx) as cfc on p.postIdx = cfc.postIdx\n" +
                                        "                                    left join (select postIdx,count(*) as countUpvote\n" +
                                        "                                                from PostUpvote\n" +
                                        "                                                where PostUpvote.status = 'ACTIVE'\n" +
                                        "                                                group by postIdx) as fu on p.postIdx = fu.postIdx\n" +
                                        "                                    left join (select bookIdx, title as bookTitle\n" +
                                        "                                                from Book) as bk on p.bookIdx = bk.bookIdx\n" +
                                        "                            group by p.postIdx) as postList on countPostComment.postIdx = postList.postIdx\n" +
                                        "where countPostComment.userIdx = ?\n" +
                                        "order by createdDate desc",
                                (rc, rownum) -> new GetMyPostComment(
                                        rc.getString("title"),
                                        rc.getString("nickName"),
                                        rc.getInt("countUpvote"),
                                        rc.getInt("countComment"),
                                        rc.getString("createdDate")
                                ), userIdx)
                ), userIdx);

    }
}
