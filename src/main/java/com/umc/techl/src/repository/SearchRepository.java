package com.umc.techl.src.repository;

import com.umc.techl.src.model.search.SearchBookInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SearchRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int getCountResult(String bookName) {
        String countResultQuery = "select count(*) " +
                                    "from book " +
                                    "where title like ? ";

        String bookKeyword = "%" + bookName + "%";

        return this.jdbcTemplate.queryForObject(countResultQuery,
                int.class,
                bookKeyword
        );
    }

    public List<SearchBookInfo> getSearchBookInfo(String bookName) {
        String searchBookInfoQuery = "select b.bookIdx, cover, title, author, if(countForum is null, 0, countForum) as countForum,\n" +
                                        "       if(countPost is null, 0, countPost) as countPost\n" +
                                        "from book as b\n" +
                                        "        left join (select bookIdx, count(*) as countForum\n" +
                                        "                    from forum\n" +
                                        "                    where status = 'ACTIVE'\n" +
                                        "                    group by bookIdx) as f on b.bookIdx = f.bookIdx\n" +
                                        "        left join (select bookIdx, count(*) as countPost\n" +
                                        "                    from post\n" +
                                        "                    group by bookIdx) as p on b.bookIdx = p.bookIdx\n" +
                                        "where title like ?";

        String bookKeyword = "%" + bookName + "%";

        return this.jdbcTemplate.query(searchBookInfoQuery,
                (rs, rowNum) -> new SearchBookInfo(
                        rs.getInt("bookIdx"),
                        rs.getString("cover"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("countForum"),
                        rs.getInt("countPost")
                ), bookKeyword
        );
    }
}