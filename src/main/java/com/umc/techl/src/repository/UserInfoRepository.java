package com.umc.techl.src.repository;

import com.umc.techl.src.model.userInfo.GetUserInfoRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;


@Repository
public class UserInfoRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public GetUserInfoRes getUserInfo(int userIdx) {
        String getUsersQuery = "select nickName, phoneNumber, userJob\n" +
                "from user as u\n" +
                "WHERE u.userIdx = ? and status ='ACTIVE'";

        return this.jdbcTemplate.queryForObject(getUsersQuery,
                ((rs, rowNum) -> new GetUserInfoRes(
                        rs.getString("nickName"),
                        rs.getString("phoneNumber"),
                        rs.getString("userJob")
                )), userIdx);
    }
}
