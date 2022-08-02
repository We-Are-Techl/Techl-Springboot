package com.umc.techl;

import com.umc.techl.src.model.GetForumInfoRes;
import com.umc.techl.src.model.GetOngingOrFinishedPostRes;
import com.umc.techl.src.model.GetRecruitingPostRes;
import com.umc.techl.src.repository.HomeRepository;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

@SpringBootTest
class TechlApplicationTests {

    private JdbcTemplate jdbcTemplate;
    private List<GetForumInfoRes> getForumInfoRes;
    private List<GetRecruitingPostRes> getRecruitingPostRes;
    private List<GetOngingOrFinishedPostRes> getOngingOrFinishedPostRes;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    private HomeRepository homeRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testBookInfo() {
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
                ),1
        );
        System.out.println("getForumInfoRes = " + getForumInfoRes);
    }


}
