package com.umc.techl.src.repository;

import com.umc.techl.src.model.user.PostLoginReq;
import com.umc.techl.src.model.user.PostUserInterestReq;
import com.umc.techl.src.model.user.PostUserReq;
import com.umc.techl.src.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class AuthRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into User (userName, nickName, phoneNumber, password, profileImgUrl, userJob) VALUES (?,?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getUserName(), postUserReq.getNickName(), postUserReq.getPhoneNumber(), postUserReq.getPassword(), postUserReq.getProfileImgUrl(), postUserReq.getUserJob()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int insertUserInterest(int userIdx, PostUserInterestReq postUserInterestReq) {
        String createUserInterestQuery = "insert into InterestField (userIdx, interest) VALUES (?,?)";
        Object[] createUserInterestParams = new Object[]{userIdx, postUserInterestReq.getInterest()};
        this.jdbcTemplate.update(createUserInterestQuery, createUserInterestParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkPhoneNumber(String phoneNumber){
        String checkPhoneNumberQuery = "select exists(select phoneNumber from User where phoneNumber = ?)";
        String checkPhoneNumberParams = phoneNumber;
        return this.jdbcTemplate.queryForObject(checkPhoneNumberQuery,
                int.class,
                checkPhoneNumberParams);

    }

    public User getPwd(String phoneNumber) {
        String getPwdQuery = "select userIdx, userName, nickName, phoneNumber, password, profileImgUrl, userJob from User where phoneNumber = ?";

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum) -> new User(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("nickName"),
                        rs.getString("phoneNumber"),
                        rs.getString("password"),
                        rs.getString("profileImgUrl"),
                        rs.getString("userJob")
                ), phoneNumber
        );
    }
}