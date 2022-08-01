package com.umc.techl.src.repository;

import com.umc.techl.src.model.GetHomeInfoRes;
import com.umc.techl.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class HomeRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetHomeInfoRes> getHomeInfo(){
        String getUsersQuery = "select cover, title, authors,\n" +
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
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetHomeInfoRes(
                        rs.getString("cover"),
                        rs.getString("title"),
                        rs.getString("authors"),
                        rs.getInt("countPost"),
                        rs.getInt("countForum")
                ));
    }


}
