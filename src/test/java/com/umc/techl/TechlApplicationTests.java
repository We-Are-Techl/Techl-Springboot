package com.umc.techl;

import com.umc.techl.src.model.home.GetForumInfoRes;
import com.umc.techl.src.model.home.GetOngingOrFinishedPostRes;
import com.umc.techl.src.model.home.GetRecruitingPostRes;
import com.umc.techl.src.repository.HomeRepository;
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
    void testUserJoin() {
        String createUserInterestQuery = "insert into InterestField (userIdx, interest) VALUES (?,?)";
        Object[] createUserInterestParams = new Object[]{7, "java"};
        this.jdbcTemplate.update(createUserInterestQuery, createUserInterestParams);

        String lastInserIdQuery = "select last_insert_id()";
        Integer integer = this.jdbcTemplate.queryForObject(lastInserIdQuery, int.class);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(integer);
    }

    @Test
    void testQueryResult() {
        String lastInserIdQuery = "SELECT EXISTS(SELECT * from bookmark WHERE userIdx=1 and contentIdx=1 and type='BOOK') as RESULT;";
        Integer integer = this.jdbcTemplate.queryForObject(lastInserIdQuery, int.class);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(integer);
    }


}
