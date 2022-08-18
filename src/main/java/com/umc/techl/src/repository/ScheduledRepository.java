package com.umc.techl.src.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ScheduledRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void changeToOngoing(String formatedNow) {
        String changeRecruitingToOngoingQuery = "update post set status='ONGOING' where status='RECRUITING' and startDate=?";
        this.jdbcTemplate.update(changeRecruitingToOngoingQuery, formatedNow);
    }

    public void changeToFinished(String formatedNow) {
        String changeOngoingToFinishedQuery = "update post set status='FINISHED' where status='ONGOING' and endDate=?";
        this.jdbcTemplate.update(changeOngoingToFinishedQuery, formatedNow);
    }
}
