package com.umc.techl.src.repository;

import com.umc.techl.src.model.myActivity.GetMyActivityRes;
import com.umc.techl.src.model.myActivity.MyBookmarkInfo;
import com.umc.techl.src.model.myActivity.MyForumInfo;
import com.umc.techl.src.model.myActivity.MyPostInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MyActivityRepository {
    private JdbcTemplate jdbcTemplate;

    private List<MyForumInfo> myForumInfo;
    private List<MyBookmarkInfo> myBookmarkInfo;
    private List<MyPostInfo> myPostInfo;

    @Autowired
    public void setDataSource(DataSource dataSource){this.jdbcTemplate = new JdbcTemplate(dataSource);}


    public GetMyActivityRes getMyActivityInfo(int userIdx) {
        String getQuery = "select nickName\n" +
                        "from User as u\n" +
                        "WHERE u.userIdx = ? and status ='ACTIVE'";

        return this.jdbcTemplate.queryForObject(getQuery,
                (rs, rowNum) -> new GetMyActivityRes(
                        rs.getString("nickName"),
                        myForumInfo = this.jdbcTemplate.query("select countForum, countForumComment\n" +
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
                                                                "where u.userIdx = ?;",
                                (ra, rownum) -> new MyForumInfo(
                                        ra.getInt("countForum"),
                                        ra.getInt("countForumComment")
                                ),userIdx),
                        myPostInfo = this.jdbcTemplate.query("select if(participationCount is null,0, participationCount) as participationCount ,\n" +
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
                                                                "where u.userIdx = ?",
                                (rb, rownum) -> new MyPostInfo(
                                        rb.getInt("participationCount"),
                                        rb.getInt("completionCount"),
                                        rb.getInt("announcementCount"),
                                        rb.getInt("commentCount")
                                ),userIdx),

                        myBookmarkInfo = this.jdbcTemplate.query("select if(countBookBookmark is null, 0, countBookBookmark) as countBookBookmark,\n" +
                                                                    "if(countPostBookmark is null, 0, countPostBookmark) as countPostBookmark,\n" +
                                                                    "if(countForumBookmark is null, 0, countForumBookmark) as countForumBookmark\n" +
                                                                    "from User as u\n" +
                                                                    "    left join (select userIdx,count(*) as countBookBookmark\n" +
                                                                    "        from bookmark\n" +
                                                                    "        where type = 'BOOK' and status = 'ACTIVE'\n" +
                                                                    "        group by userIdx) as cbb on u.userIdx = cbb.userIdx\n" +
                                                                    "     left join (select userIdx,count(*) as countPostBookmark\n" +
                                                                    "                        from bookmark\n" +
                                                                    "                        where type = 'POST' and status = 'ACTIVE'\n" +
                                                                    "                        group by userIdx) as cpb on u.userIdx = cpb.userIdx\n" +
                                                                    "            left join (select userIdx,count(*) as countForumBookmark\n" +
                                                                    "                        from bookmark\n" +
                                                                    "                        where type = 'FORUM' and status = 'ACTIVE'\n" +
                                                                    "                        group by userIdx) as cfb on u.userIdx = cfb.userIdx\n" +
                                                                    "where u.userIdx = ?",
                                (rc, rownum) -> new MyBookmarkInfo(
                                        rc.getInt("countBookBookmark"),
                                        rc.getInt("countPostBookmark"),
                                        rc.getInt("countForumBookmark")
                                ),userIdx)
                ),userIdx);

    }
}
