package com.umc.techl.src.repository;

import com.umc.techl.src.model.book.GetBookInfoRes;
import com.umc.techl.src.model.book.GetBookTitleRes;
import com.umc.techl.src.model.post.GetOngoingOrFinishedListRes;
import com.umc.techl.src.model.post.GetRecruitingPostListRes;
import com.umc.techl.src.model.post.PostContents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PostRepository {

    private JdbcTemplate jdbcTemplate;
    private List<GetRecruitingPostListRes> getRecruitingPostListRes;
    private List<GetOngoingOrFinishedListRes> getOngoingOrFinishedListRes;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetBookTitleRes getBookTitle(int bookIdx) {
        String getBookTitleQuery = "select bookIdx, title from book where bookIdx = ?";

        return this.jdbcTemplate.queryForObject(getBookTitleQuery,
                (rs, rowNum) -> new GetBookTitleRes(
                        rs.getInt("bookIdx"),
                        rs.getString("title")
                ), bookIdx);
    }

    public List<GetRecruitingPostListRes> getRecruitingPostListInfo(int bookIdx) {
        String selectRecruitingPostListQuery = "select p.postIdx as postIdx, title, type, concat(DATE_FORMAT(p.startDate, '%Y.%m.%d'),' ~ ',DATE_FORMAT(p.endDate, '%Y.%m.%d')) as period,\n" +
                "       concat(if(countJoinMember is null, 0, countJoinMember),'/',memberCount) as recruiting\n" +
                "from Post as p\n" +
                "         left join (select postIdx, count(*) as countJoinMember\n" +
                "                    from JoinContents\n" +
                "                    where status = 'ACTIVE'\n" +
                "                    group by postIdx) as jc on p.postIdx = jc.postIdx\n" +
                "where p.status = 'RECRUITING' and bookIdx = ?\n" +
                "order by createdAt desc";

        return this.jdbcTemplate.query(selectRecruitingPostListQuery,
                (ra, rownum) -> new GetRecruitingPostListRes(
                        ra.getInt("postIdx"),
                        ra.getString("title"),
                        ra.getString("type"),
                        ra.getString("period"),
                        ra.getString("recruiting")
                ), bookIdx
        );

    }

    public List<GetOngoingOrFinishedListRes> getOngoingOrFinishedListInfo(int bookIdx) {
        String selectOngoingOrFinishedListQuery = "select p.postIdx as postIdx, title, type, concat(DATE_FORMAT(p.startDate, '%Y.%m.%d'),' ~ ',DATE_FORMAT(p.endDate, '%Y.%m.%d')) as period,\n" +
                "       concat(if(countJoinMember is null, 0, countJoinMember),'/',memberCount) as recruiting\n" +
                "from Post as p\n" +
                "         left join (select postIdx, count(*) as countJoinMember\n" +
                "                    from JoinContents\n" +
                "                    where status = 'ACTIVE'\n" +
                "                    group by postIdx) as jc on p.postIdx = jc.postIdx\n" +
                "where (p.status = 'ONGOING' or p.status = 'FINISHED') and bookIdx = ?\n" +
                "order by createdAt desc";

        return this.jdbcTemplate.query(selectOngoingOrFinishedListQuery,
                (rb, rownum) -> new GetOngoingOrFinishedListRes(
                        rb.getInt("postIdx"),
                        rb.getString("title"),
                        rb.getString("type"),
                        rb.getString("period"),
                        rb.getString("recruiting")
                ), bookIdx
        );

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

    public int createPostContents(PostContents postContents) {
        String createPostQuery = "insert into Post (bookIdx, userIdx, type, title, content, contentsImage, coverImage, confirmMethod, startDate, endDate) VALUES (?,?,?,?,?,?,?,?,?,?)";
        Object[] createPostParams = new Object[]{postContents.getBookIdx(), postContents.getUserIdx(), postContents.getType(), postContents.getTitle(), postContents.getContent(),
                                                    postContents.getContentsImage(), postContents.getCoverImage(), postContents.getConfirmMethod(), postContents.getStartDate(), postContents.getEndDate()};
        this.jdbcTemplate.update(createPostQuery, createPostParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }
}
